package controllers

import play.api.mvc._
import javax.inject._
import models.Tables._
import services.{TeamsService}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class Teams @Inject() (teamsService: TeamsService) extends Controller {

	import services.Converters._
  import controllers.common.TippaJsonSerializer._

//  case class TeamRow(id: Int, shortName: String, longName: Option[String], description: Option[String], `type`: String = "football", country: String = "INTERNATIONAL", createdDate: java.sql.Timestamp)

  

  def getTeams = AuthenticatedAction.async { implicit request =>
    teamsService.getTeams.map(data => Ok(Json.toJson(data)(Writes.seq(teamRowWrites))))
  }

  def getTeam(id: Long) = AuthenticatedAction.async { implicit request =>
    teamsService.getTeam(id).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
}