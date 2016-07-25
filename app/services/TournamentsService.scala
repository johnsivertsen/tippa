package services

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import db.Tables._
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
import org.mindrot.jbcrypt.BCrypt

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
}