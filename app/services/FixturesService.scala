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

class FixturesService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val db = dbConfig.db
  import dbConfig.driver.api._

  def getFixturesByTournamentAndRoundId(idTournament: Long, idRound: Long): Future[Seq[FixtureRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.id === idRound.intValue
      f <- Fixture if f.idRound === r.id
    } yield (f)

    db.run{
      q.result
    }
  }
  
  def getFixtureByTournamentAndRoundAndFixtureId(idTournament: Long, idRound: Long, idFixture: Long): Future[Option[FixtureRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.id === idRound.intValue
      f <- Fixture if f.idRound === r.id && f.id === idFixture.intValue
    } yield (f)

    db.run{
      q.result.headOption
    }
  }
}