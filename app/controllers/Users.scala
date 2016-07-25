package controllers

import play.api.mvc._
import javax.inject._
import db.Tables._
import services.UsersService
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class Users @Inject() (usersService: UsersService) extends Controller {

	import services.Converters._

	implicit val userRowWrites: Writes[UserRow] = new Writes[UserRow] {
		def writes(row: UserRow) = Json.obj(
	    "id" -> row.id,
	    "eMail" -> row.eMail,
	    "password" -> row.password,
	    "firstName" -> row.firstName,
	    "lastName" -> row.lastName,
	    "createdDate" -> formatter.print(row.createdDate),
	    "status" -> row.status
    )
	}

	def show(id: Long) = AuthenticatedAction.async { implicit request => //implicit userRowWrites =>
    usersService.getUser(id, request.username).map {data =>
      data match {
        case Some(value) => Ok(Json.toJson(data))
        case _ => NotFound
      }
    }
	}
}