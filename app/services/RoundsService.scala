package services

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import models.Tables._
//import maps.Converters._
//import maps.OrderMap
import org.joda.time.DateTime
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.libs.json.Json
import play.api.Logger
import play.api.mvc._
import java.sql.Timestamp
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import java.lang.IllegalArgumentException
import java.util.Calendar


class RoundsService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._

  def getRoundsByTournamentId(idTournament: Long): Future[Seq[RoundRow]] = {
    db.run{
      Round.filter(_.idTournament === idTournament.intValue).result
    }
  }
  
  def getRoundByTournamentAndRoundNumber(idTournament: Long, roundNumber: Long): Future[Option[RoundRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.number === roundNumber.intValue
    } yield (r)

    db.run{
      q.result.headOption
    }
  }
  
  def validateTournamentIds(idTournament: Int, round: RoundRow): Either[String, RoundRow] = {
    if (idTournament == round.idTournament) {
      Right(round)
    } else {
      Left("Tournament ids do not match in round POST input")
    }
  }
  
  def postRound(idTournament: Long, round: RoundRow): Future[Either[String, Int]] = {
    val r = round.copy(id = 0, createdDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))
    val rId = (Round returning Round.map(_.id)) += r
    
    validateTournamentIds(idTournament.intValue, round).fold(
      error => {
        Future.successful(Left(error))
      },
      success => {
        db.run(rId.asTry).map { result =>
          result match {
            case Success(res) =>
              Logger.error("Round inserted successfully. Id: " + res)
              Right(res)
            case Failure(e) => {
              Logger.error("Round insertion failed. Message: " + e.getMessage())
              Left(e.getMessage())
            }
          }
        }
      }
    )
  }
}