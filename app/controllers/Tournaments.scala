package controllers

import play.api.mvc._
import javax.inject._
import db.Tables._
import services.TournamentsService
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class Tournaments @Inject() (tournamentsService: TournamentsService) extends Controller {

//case class TournamentRow(id: Int, shortName: String, fullName: Option[String], `type`: String = "football", createdDate: java.sql.Timestamp)

	import services.Converters._

	implicit val tournamentRowWrites: Writes[TournamentRow] = new Writes[TournamentRow] {
		def writes(row: TournamentRow) = Json.obj(
		  "id" -> row.id,
		  "shortName" -> row.shortName,
		  "fullName" -> row.fullName,
		  "type" -> row.`type`,
		  "createdDate" -> formatter.print(row.createdDate)
		  )
	}

  def getTournaments = AuthenticatedAction.async { implicit request =>
    tournamentsService.getTournaments.map(data => Ok(Json.toJson(data)(Writes.seq(tournamentRowWrites))))
  }
  
  def getTournamentsById(id: Long) = AuthenticatedAction.async { implicit request =>
    tournamentsService.getTournamentsById(id).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
}