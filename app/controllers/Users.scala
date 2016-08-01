package controllers

import play.api.mvc._
import javax.inject._
import models.Tables._
import services.UsersService
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.h2.jdbc.JdbcSQLException

@Singleton
class Users @Inject() (usersService: UsersService) extends Controller {

  import services.Converters._
  import controllers.common.TippaJsonSerializer._

  def getUser(id: Long) = AuthenticatedAction.async { implicit request =>
    usersService.getUser(id, request.username).map {data =>
      data match {
        case Some(value) => Ok(Json.toJson(data))
        case _ => NotFound
      }
    }
  }
  
  /*
  curl --include --request POST --header "Content-type: application/json" --data '{"id":-1,"firstName":"Jane","lastName":"Doe","eMail":"jane@email.com","password":"asdf2","createdDate":"2016-01-02 23:59:01.00000","status":"N/A"}' http://localhost:9000/users
  
  */
  
  def putUser = Action.async(BodyParsers.parse.json) { request =>
    val userResult = request.body.validate[UserRow]
    userResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      userRow => {
        usersService.putUser(userRow).map { result =>
          result match {
            case x: Int if (x > 0) => Ok(Json.obj("status" -> "OK", "message" -> ("User '" + userRow.eMail + "' saved with id " + x)))
            case _ => InternalServerError(Json.obj("status" -> "KO", "message" -> "User creation failed"))
          }
        }
      }
    )
  }
}