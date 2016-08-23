package services

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import models.Tables._
import org.joda.time.DateTime
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.libs.json.Json
import play.api.Logger
import play.api.mvc._
import java.sql.Timestamp
import org.mindrot.jbcrypt.BCrypt
import java.util.Calendar
import scala.util.Try
import scala.util.Success
import scala.util.Failure


class TournamentsService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._

  def getTournaments: Future[Seq[TournamentRow]] = {
    db.run{
      Tournament.result
    }
  }
  
  def getTournamentsById(id: Long): Future[Option[TournamentRow]] = {
    db.run{
      Tournament.filter(_.id === id.intValue).result.headOption
    }
  }
  
  def postTournament(tournamentInput: TournamentRow): Future[Int] = {
    val t = tournamentInput.copy(id = 0, createdDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))
    val tId = (Tournament returning Tournament.map(_.id)) += t

    db.run(tId.asTry).map { result =>
      result match {
        case Success(res) =>
          Logger.error("Successfully created new tournament. Id: " + res)
          res
        case Failure(e) => {
          e match {
            case ex: org.h2.jdbc.JdbcSQLException => {
              Logger.error("Tournament creation failed: JdbcSQLException: " + ex.getMessage)
              -1
            }
            case _: AnyRef => {
              Logger.error("Tournament creation failed: Unkown error: " + e.getMessage)
              -2
            }
          }
        }
      }
    }
  }

}