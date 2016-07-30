package controllers.common

import play.api.mvc._
import javax.inject._
import models.Tables._
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
}