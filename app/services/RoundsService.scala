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

class RoundsService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
	val dbConfig = dbConfigProvider.get[JdbcProfile]
	val db = dbConfig.db
	import dbConfig.driver.api._

  def getRoundsByTournamentId(idTournament: Long): Future[Seq[RoundRow]] = {
    db.run{
      Round.filter(_.idTournament === idTournament.intValue).result
    }
  }
  
  def getRoundByTournamentAndRoundId(idTournament: Long, idRound: Long): Future[Option[RoundRow]] = {
    val q = for {
      t <- Tournament if t.id === idTournament.intValue
      r <- Round if r.idTournament === t.id && r.id === idRound.intValue
    } yield (r)

    db.run{
      q.result.headOption
    }
  }
}