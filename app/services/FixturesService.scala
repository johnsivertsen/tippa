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
import java.util.Calendar
import scala.util.Try
import scala.util.Success
import scala.util.Failure


class FixturesService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._

  def getFixturesByTournamentAndRoundNumber(idTournament: Long, roundNumber: Long): Future[Seq[FixtureRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.number === roundNumber.intValue
      f <- Fixture if f.idRound === r.id
    } yield (f)

    db.run{
      q.result
    }
  }
  
  def getFixtureByTournamentAndRoundNumberAndFixtureId(idTournament: Long, roundNumber: Long, idFixture: Long): Future[Option[FixtureRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.number === roundNumber.intValue
      f <- Fixture if f.idRound === r.id && f.id === idFixture.intValue
    } yield (f)

    db.run{
      q.result.headOption
    }
  }
  
  def validateTournamentIdAndRoundNumber(idTournament: Long, roundNumber: Long): Future[Either[String, Int]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.number === roundNumber.intValue
    } yield (r)

    db.run{
      q.result.headOption
    }.map { result =>
      result match {
        case Some(_) => Right(0)
        case None => Left("TournamentId or round number invalid")
      }
    }
  }

  def postFixture(idTournament: Long, roundNumber: Long, fixture: FixtureRow): Future[Either[String, Int]] = {
    val f = fixture.copy(id = 0, createdDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))
    val fId = (Fixture returning Fixture.map(_.id)) += f
    
    val validateInputsFuture: Future[Either[String, Int]] = validateTournamentIdAndRoundNumber(idTournament, roundNumber)
    
    val insertFixtureFuture: Future[Either[String, Int]] = validateInputsFuture.flatMap { validation =>
      validation match {
        case Right(value) =>
          db.run(fId.asTry).map( fixtureInsertResult =>
            fixtureInsertResult match {
              case Success(v) => Right(v)
              case Failure(e) => Left(e.getMessage())
            })
        case Left(s) =>
          Future{Left(s)}
      }
    }

    insertFixtureFuture
 }
}