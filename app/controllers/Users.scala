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
	
	/*
	curl --include --request POST --header "Content-type: application/json" --data '{"id":"-1",firstName":"Jane","lastName":"Doe","eMail":"jane@email.com","password":"asdf2","createdDate":"2016-01-02 23:59:01.00000","status":"N/A"}' http://localhost:9000/users
	
	*/
	
	def putUser = Action(BodyParsers.parse.json) { request =>
	  val userResult = request.body.validate[UserRow]
	  userResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      userRow => {
        usersService.putUser(userRow)
        Ok(Json.obj("status" -> "OK", "message" -> ("User '" + userRow.eMail + "' saved.")))
        //val user = new UserRow(0, userInput.password, userInput.firstName, userInput, )
      }
    )
	}
	
	/*def savePlace = Action(BodyParsers.parse.json) { request =>
  val placeResult = request.body.validate[Place]
  placeResult.fold(
    errors => {
      BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
    },
    place => {
      Place.save(place)
      Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
    }
  )*/
}