package controllers

import play.api.mvc._
import javax.inject._
import models.Tables._
import services.{BetsService, UsersService}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Logger


@Singleton
class Bets @Inject() (betsService: BetsService, usersService: UsersService) extends Controller {

  import controllers.common.TippaJsonSerializer._

  def getBet(id: Long) = AuthenticatedAction.async { implicit request =>
    betsService.getBet(id).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
  
  /*
  curl --include --request POST --cookie "PLAY_SESSION=c7c8fd34da5fefbca70aaeec4c857ef0802d466a-username=jane%40email.com" --header "Content-type: application/json" --data '{"id":-1,"idFixture":1,"idUser":-1,"homePoints":0,"awayPoints":0,"createdDate":"1970-01-01 00:00:00.0000"}' http://localhost:9000/bets

  */
  
  def putBet() = AuthenticatedAction.async(BodyParsers.parse.json) { implicit request =>
    val betResult = request.body.validate[BetRow]
    betResult.fold (
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      betRow => {
        usersService.getUserIdFromEmail(request.username).flatMap { i =>
          i match {
            case Some(value) => betsService.putBet(betRow, value).map { _ =>
              Ok(Json.obj("status" -> "OK", "message" -> "Bet saved"))
            }
            case None => Future.successful(NotFound)
          }
        }
      }
    )
  }
}