package controllers

import play.api.mvc._
import javax.inject._
import models.Tables._
import services.{TournamentsService, RoundsService, FixturesService, BetsService, UsersService}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


@Singleton
class Tournaments @Inject() (tournamentsService: TournamentsService, roundsService: RoundsService, fixturesService: FixturesService, betsService: BetsService, usersService: UsersService) extends Controller {

	import services.Converters._
	import controllers.common.TippaJsonSerializer._

//case class TournamentRow(id: Int, shortName: String, fullName: Option[String], `type`: String = "football", createdDate: java.sql.Timestamp)
	implicit val tournamentRowWrites: Writes[TournamentRow] = new Writes[TournamentRow] {
		def writes(row: TournamentRow) = Json.obj(
		  "id" -> row.id,
		  "shortName" -> row.shortName,
		  "fullName" -> row.fullName,
		  "type" -> row.`type`,
		  "createdDate" -> formatter.print(row.createdDate)
		  )
	}

//  case class RoundRow(id: Int, idTournament: Int, number: Int, designatedDate: java.sql.Timestamp, createdDate: java.sql.Timestamp)
	implicit val roundRowWrites: Writes[RoundRow] = new Writes[RoundRow] {
		def writes(row: RoundRow) = Json.obj(
		  "id" -> row.id,
		  "idTournament" -> row.idTournament,
		  "designatedDate" -> formatter.print(row.designatedDate),
		  "createdDate" -> formatter.print(row.createdDate)
		  )
	}

//  case class FixtureRow(id: Int, idRound: Int, idTeamHome: Int, idTeamAway: Int, homePoints: Option[Int], awayPoints: Option[Int], homePointsAwarded: Option[Int], awayPointsAwarded: Option[Int], startTime: Option[java.sql.Timestamp], status: Option[String], createdDate: java.sql.Timestamp)
	implicit val fixtureRowWrites: Writes[FixtureRow] = new Writes[FixtureRow] {
		def writes(row: FixtureRow) = Json.obj(
		  "id"                -> row.id,
		  "idRound"           -> row.idRound,
		  "idTeamHome"        -> row.idTeamHome,
		  "idTeamAway"        -> row.idTeamAway,
		  "homePoints"        -> row.homePoints,
		  "awayPoints"        -> row.awayPoints,
		  "homePointsAwarded" -> row.homePointsAwarded,
		  "awayPointsAwarded" -> row.awayPointsAwarded,
		  "startTime"         -> {
		    row.startTime.map { s =>
          formatter.print(s)
	      }
		  },
		  "status"            -> row.status,
		  "createdDate"       -> formatter.print(row.createdDate)
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
  
  def getTournamentRounds(idTournament: Long) = AuthenticatedAction.async { implicit request =>
    roundsService.getRoundsByTournamentId(idTournament).map {
      rounds => Ok(Json.toJson(rounds)(Writes.seq(roundRowWrites)))
    }
  }

  def getTournamentRoundFixtures(idTournament: Long, idRound: Long) = AuthenticatedAction.async { implicit request =>
    fixturesService.getFixturesByTournamentAndRoundId(idTournament, idRound).map {
      fixtures => Ok(Json.toJson(fixtures)(Writes.seq(fixtureRowWrites)))
    }
  }

  def getTournamentRoundFixture(idTournament: Long, idRound: Long, idFixture: Long) = AuthenticatedAction.async { implicit request =>
    fixturesService.getFixtureByTournamentAndRoundAndFixtureId(idTournament, idRound, idFixture).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }

  def getTournamentRound(idTournament: Long, idRound: Long) = AuthenticatedAction.async { implicit request =>
    roundsService.getRoundByTournamentAndRoundId(idTournament, idRound).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
  
  def getTournamentRoundFixtureBets(idTournament: Long, idRound: Long, idFixture: Long) = AuthenticatedAction.async { implicit request =>
    betsService.getBetsByTournamentAndRoundAndFixtureId(idTournament, idRound, idFixture).map {
      bets => Ok(Json.toJson(bets)(Writes.seq(betRowWrites)))
    }
  }
  
  def getUsers(idTournament: Long) = AuthenticatedAction.async { implicit request =>
    usersService.getTournamentUsers(idTournament).map {
      users => Ok(Json.toJson(users)(Writes.seq(userRowWrites)))
    }
  }
}