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

}