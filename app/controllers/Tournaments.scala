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

  def getTournamentRoundFixtures(idTournament: Long, roundNumber: Long) = AuthenticatedAction.async { implicit request =>
    fixturesService.getFixturesByTournamentAndRoundNumber(idTournament, roundNumber).map {
      fixtures => Ok(Json.toJson(fixtures)(Writes.seq(fixtureRowWrites)))
    }
  }

  def getTournamentRoundFixture(idTournament: Long, roundNumber: Long, idFixture: Long) = AuthenticatedAction.async { implicit request =>
    fixturesService.getFixtureByTournamentAndRoundNumberAndFixtureId(idTournament, roundNumber, idFixture).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }

  def getTournamentRound(idTournament: Long, roundNumber: Long) = AuthenticatedAction.async { implicit request =>
    roundsService.getRoundByTournamentAndRoundNumber(idTournament, roundNumber).map { data =>
      data match {
        case Some(value) => Ok(Json.toJson(value))
        case _ => NotFound
      }
    }
  }
  
  def getTournamentRoundFixtureBets(idTournament: Long, roundNumber: Long, idFixture: Long) = AuthenticatedAction.async { implicit request =>
    betsService.getBetsByTournamentAndRoundNumberAndFixtureId(idTournament, roundNumber, idFixture).map {
      bets => Ok(Json.toJson(bets)(Writes.seq(betRowWrites)))
    }
  }
  
  def getUsers(idTournament: Long) = AuthenticatedAction.async { implicit request =>
    usersService.getTournamentUsers(idTournament).map {
      users => Ok(Json.toJson(users)(Writes.seq(userRowWrites)))
    }
  }
  
  /*

  curl --include --request POST --header "Content-type: application/json" --data '{"id":-1,"shortName":"FM 2016","fullName":"Effordeildin 2016","type":"football","password":"asdf2","createdDate":"2016-01-02 23:59:01.00000"}' http://localhost:9000/tournaments
  curl --include --request POST --cookie "PLAY_SESSION=c7c8fd34da5fefbca70aaeec4c857ef0802d466a-username=jane%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"shortName":"FM 2016","fullName":"Effordeildin 2016","type":"football","password":"asdf2","createdDate":"2016-01-02 23:59:01.00000"}' http://localhost:9000/tournaments
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"shortName":"FM 2016","fullName":"Effordeildin 2016","type":"football","password":"asdf2","createdDate":"2016-01-02 23:59:01.00000"}' http://localhost:9000/tournaments
  
  */

  
  def postTournament = (AuthenticatedAction andThen HasAdminRoleAction).async(BodyParsers.parse.json) { request =>
    val tournamentResult = request.body.validate[TournamentRow]
    tournamentResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      tournamentRow => {
        tournamentsService.postTournament(tournamentRow).map { result =>
          Ok(Json.obj("status" -> "OK", "message" -> "Tournament saved"))
        }
      }
    )
  }
  
  
  /*

  //  case class RoundRow(id: Int, idTournament: Int, number: Int, designatedDate: java.sql.Timestamp, createdDate: java.sql.Timestamp)

  curl --include --request POST --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":1,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=c7c8fd34da5fefbca70aaeec4c857ef0802d466a-username=jane%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":1,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":1,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":2,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":3,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":4,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"number":5,"designatedDate":"2016-08-02 23:59:01.00000","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds
  
  */

  
  def postTournamentRound(idTournament: Long) = (AuthenticatedAction andThen HasAdminRoleAction).async(BodyParsers.parse.json) { request =>
    val roundResult = request.body.validate[RoundRow]
    roundResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      roundRow => {
        roundsService.postRound(idTournament, roundRow).map { result =>
          result.fold(
            error => BadRequest(Json.obj("status" -> "KO", "message" -> error)),
            success => Ok(Json.obj("status" -> "OK", "message" -> "Round saved"))
          )
        }
      }
    )
  }
  
  /*
  
                                                                                                                                                                                            case class FixtureRow(id: Int, idRound: Int, idTeamHome: Int, idTeamAway: Int, homePoints: Option[Int], awayPoints: Option[Int], homePointsAwarded: Option[Int], awayPointsAwarded: Option[Int], startTime: Option[java.sql.Timestamp], status: Option[String], createdDate: java.sql.Timestamp)

  curl --include --request POST --cookie "PLAY_SESSION=9415c88f606f7fee4389e880a0dfb0922ce71c53-username=email%40email.com&role=ADMIN" --header "Content-type: application/json" --data '{"id":-1,"idTournament":1,"idRound":1,"idTeamHome":1,"idTeamAway":2,"homePoints":0,"awayPoints":0,"homePointsAwarded":0,"awayPointsAwarded":0,"startTime":"2016-08-02 23:59:01.00000","status":"CREATED","createdDate":"1970-08-02 23:59:01.00000"}' http://localhost:9000/tournaments/1/rounds/1/fixtures
  */
  
  def postTournamentRoundFixture(idTournament: Long, roundNumber: Long) = (AuthenticatedAction andThen HasAdminRoleAction).async(BodyParsers.parse.json) { request =>
    val fixtureValidator = request.body.validate[FixtureRow]
    fixtureValidator.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors))))
      },
      fixtureRow => {
        if (roundNumber != fixtureRow.idRound) {
          Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> "Bad input paramenters. Tournament and round ids in JSON and URL don't match")))
        } else {
          fixturesService.postFixture(idTournament, roundNumber, fixtureRow).map { result =>
            result.fold(
              error => BadRequest(Json.obj("status" -> "KO", "message" -> error)),
              success => Ok(Json.obj("status" -> "OK", "message" -> "Fixture saved"))
            )
          }
        }
      }
    )
  }
}