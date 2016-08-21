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

class BetsService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._

  def getBetsByTournamentAndRoundNumberAndFixtureId(idTournament: Long, roundNumber: Long, idFixture: Long): Future[Seq[BetRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.number === roundNumber.intValue
      f <- Fixture if f.idRound === r.id && f.id === idFixture.intValue
      b <- Bet if b.idFixture === f.id
    } yield (b)

    db.run{
      q.result
    }
  }

  def getBet(id: Long): Future[Option[BetRow]] = {
    db.run {
      Bet.filter(_.id === id.intValue).result.headOption
    }
  }
  
  def putBet(betInput: BetRow, userId: Int): Future[Int] = {
    import java.util.Calendar

    val b = betInput.copy(id = 0, idUser = userId, createdDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()))
    val bId = (Bet returning Bet.map(_.id)) += b
    db.run {
      bId
    }
  }
}