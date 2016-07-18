package controllers

import models.Users
import play.api.mvc._
import play.api.mvc.Results.Redirect
import play.api.data.Form
import play.api.data.Forms.{tuple, nonEmptyText}
import play.api.i18n.{I18nSupport, MessagesApi, Messages}
import scala.concurrent.Future
import javax.inject._

@Singleton
class Authentication @Inject() (users: Users, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  //(users: Users, val messagesApi: MessagesApi)  with I18nSupport
  import AuthenticationHelper._

  //val users = new Users

  def login(returnTo: String) = Action { implicit request =>
    Ok(views.html.login(Login.form, returnTo))
  }

  def authenticate(returnTo: String) = Action { implicit request =>
    val submission = Login.form.bindFromRequest()
    submission.fold(
      errors => BadRequest(views.html.login(errors, returnTo)),
      {
        case (username, password) =>
          if (users.authenticate(username, password)) {
            Redirect(returnTo).addingToSession("username" -> username)
          } else {
            val erroneousSubmission = submission.withGlobalError("Invalid username and/or password")
            BadRequest(views.html.login(erroneousSubmission, returnTo))
          }
      }
    )
  }
  
  val logout = Action { implicit request =>
    Redirect(routes.AppController.index).removingFromSession(UserKey)
  }

}

object AuthenticationHelper {

  val UserKey = "username"
  
  type Login = (String, String)
  object Login {
    val form = Form(tuple(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    ))
  }

  def authenticated[A](f: String => A, g: => A)(implicit request: RequestHeader): A =
    request.session.get(UserKey) match {
      case Some(username) => f(username)
      case None => g
    }
  
}


class AuthenticatedRequest[A](val username: String, request: Request[A]) extends WrappedRequest[A](request)

object AuthenticatedAction extends ActionBuilder[AuthenticatedRequest] {
  def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]) =
    AuthenticationHelper.authenticated(
      username => block(new AuthenticatedRequest(username, request)),
      Future.successful(Redirect(routes.Authentication.login(request.uri)))
    )(request)
}