package controllers

import play.api.mvc._
import javax.inject._
import models.Tables._
import services.{BetsService}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class Bets @Inject() (betsService: BetsService) extends Controller {

  import controllers.common.TippaJsonSerializer._

  def getBet(id: Long) = AuthenticatedAction.async { implicit request =>
    betsService.getBet(id).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
}