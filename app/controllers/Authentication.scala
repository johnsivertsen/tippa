package controllers

import services.UsersService
import play.api.mvc._
import play.api.mvc.Results._
import play.api.data.Form
import play.api.data.Forms.{tuple, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi, Messages}
import scala.concurrent.Future
import javax.inject._
import play.api.libs.concurrent.Execution.Implicits.defaultContext


@Singleton
class Authentication @Inject() (usersService: UsersService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  import AuthenticationHelper._

  def login(returnTo: String) = Action { implicit request =>
    Ok(views.html.login(Login.form, returnTo))
  }


  // curl -H "Content-Type: application/json" -X POST -d '{"username":"xyz","password":"xyz"}' http://localhost:9000/authenticate
  
  def authenticate(returnTo: String) = Action.async { implicit request =>
    val submission = Login.form.bindFromRequest()
    submission.fold(
      errors => Future.successful(BadRequest(views.html.login(errors, returnTo))),
      {
        case (username, password) =>
          usersService.authenticate(username, password).map { ur =>
            ur match {
              case (true, Some(r)) => Redirect(returnTo).addingToSession(UsernameCookie -> username).addingToSession(RoleCookie -> r)
              case (true, None) => Redirect(returnTo).addingToSession(UsernameCookie -> username)
              case _ => {
                val erroneousSubmission = submission.withGlobalError("Invalid username and/or password")
                BadRequest(views.html.login(erroneousSubmission, returnTo))
              }
            }
          }
      }
    )
  }
  
  val logout = Action { implicit request =>
    Redirect(routes.AppController.index).removingFromSession(UsernameCookie).removingFromSession(RoleCookie)
  }

}

object AuthenticationHelper {

  val UsernameCookie = "username"
  val RoleCookie = "role"
  
  type Login = (String, String)
  object Login {
    val form = Form(tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ))
  }

  def authenticated[A](f: (String,String) => A, g: => A)(implicit request: RequestHeader): A =
    request.session.get(UsernameCookie) match {
      case Some(username) => f(username, request.session.get(RoleCookie).getOrElse(""))
      case None => g
    }
  
  object Privilege extends Enumeration {
    type Privilege = Value
    val ReadFull, ReadLimited, Write = Value
  }
  
}

class AuthenticatedRequest[A](val username: String, val role: String, request: Request[A]) extends WrappedRequest[A](request)

object HasAdminRoleAction extends ActionFilter[AuthenticatedRequest] {
  def filter[A](input: AuthenticatedRequest[A]) = Future.successful {
    if (input.role != "ADMIN")
      Some(Forbidden)
    else
      None
  }
}

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]) =
    AuthenticationHelper.authenticated(
      (username, role) => block(new AuthenticatedRequest(username, role, request)),
      Future.successful(Redirect(routes.Authentication.login(request.uri)))
    )(request)
}