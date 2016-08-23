package controllers.common

import play.api.mvc._
import javax.inject._
import models.Tables._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.sql.Timestamp
import org.joda.time.DateTime

object TippaJsonSerializer {

  import services.Converters._

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

  implicit val fixtureRowReads: Reads[FixtureRow] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "idRound").read[Int] and
    (JsPath \ "idTeamHome").read[Int] and
    (JsPath \ "idTeamAway").read[Int] and
    (JsPath \ "homePoints").readNullable[Int] and
    (JsPath \ "awayPoints").readNullable[Int] and
    (JsPath \ "homePointsAwarded").readNullable[Int] and
    (JsPath \ "awayPointsAwarded").readNullable[Int] and
    (JsPath \ "startTime").readNullable[String].map(s =>
      s match {
        case Some(value) => Some(dateTimeToTimestamp(stringToDateTime(value)))
        case _ => None
      }) and
    (JsPath \ "status").readNullable[String] and
    (JsPath \ "createdDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s)))
  )(FixtureRow.apply _)

  implicit val teamRowWrites: Writes[TeamRow] = new Writes[TeamRow] {
    def writes(row: TeamRow) = Json.obj(
      "id" -> row.id,
      "shortName" -> row.shortName,
      "fullName" -> row.longName,
      "description" -> row.description,
      "type" -> row.`type`,
      "country" -> row.country,
      "createdDate" -> formatter.print(row.createdDate)
      )
  }
  
  implicit val betRowWrites: Writes[BetRow] = new Writes[BetRow] {
    def writes(row: BetRow) = Json.obj(
      "id" -> row.id,
      "idFixture" -> row.idFixture,
      "idUser" -> row.idUser,
      "homePoints" -> row.homePoints,
      "awayPoints" -> row.awayPoints,
      "createdDate" -> formatter.print(row.createdDate)
    )
  }

  implicit val betRowReads: Reads[BetRow] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "idFixture").read[Int] and
    (JsPath \ "idUser").read[Int] and
    (JsPath \ "homePoints").readNullable[Int] and
    (JsPath \ "awayPoints").readNullable[Int] and
    (JsPath \ "createdDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s)))
  )(BetRow.apply _)

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
  
  implicit val userRowReads: Reads[UserRow] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "eMail").read[String] and
    (JsPath \ "password").read[String] and
    (JsPath \ "firstName").read[String] and
    (JsPath \ "lastName").read[String] and
    (JsPath \ "createdDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s))) and
    (JsPath \ "status").read[String]
  )(UserRow.apply _)

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

  implicit val tournamentRowReads: Reads[TournamentRow] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "shortName").read[String] and
    (JsPath \ "fullName").readNullable[String] and
    (JsPath \ "type").read[String] and
    (JsPath \ "createdDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s)))
  )(TournamentRow.apply _)
  
  //  case class RoundRow(id: Int, idTournament: Int, number: Int, designatedDate: java.sql.Timestamp, createdDate: java.sql.Timestamp)
  implicit val roundRowWrites: Writes[RoundRow] = new Writes[RoundRow] {
    def writes(row: RoundRow) = Json.obj(
      "id" -> row.id,
      "idTournament" -> row.idTournament,
      "designatedDate" -> formatter.print(row.designatedDate),
      "createdDate" -> formatter.print(row.createdDate)
      )
  }

  implicit val roundRowReads: Reads[RoundRow] = (
    (JsPath \ "id").read[Int] and
    (JsPath \ "idTournament").read[Int] and
    (JsPath \ "number").read[Int] and
    (JsPath \ "designatedDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s))) and
    (JsPath \ "createdDate").read[String].map(s => dateTimeToTimestamp(stringToDateTime(s)))
  )(RoundRow.apply _)
}