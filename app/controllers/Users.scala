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
}