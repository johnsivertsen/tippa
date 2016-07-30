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

class UsersService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
	val dbConfig = dbConfigProvider.get[JdbcProfile]
	val db = dbConfig.db
	import dbConfig.driver.api._

  def viewFilter(user: Option[UserRow], callingUsername: String): Option[UserRow] = {
    if (user.isDefined) {
      if (user.get.eMail != callingUsername) {
        new Some(UserRow(user.get.id, "", "", user.get.firstName, user.get.lastName, new Timestamp(0), ""))
      } else {
        user
      }
    } else {
      user
    }
  }

	def getUser(id: Long, callingUsername: String): Future[Option[UserRow]] = {
	  db.run {
	    User.filter(_.id === id.intValue).result.headOption
	  }.map(viewFilter(_, callingUsername))
	}
	
	def authenticate(username: String, password: String): Future[Boolean] = {
	  db.run {
	    User.filter(user => user.eMail === username).result.headOption.map { user =>
	      user match {
	        case Some(user) => BCrypt.checkpw(password, user.password)
	        case _ => false
	      }
	    }
	  }
	}
	
//  case class UserRow(id: Int, eMail: String, password: String, firstName: String, lastName: String, createdDate: java.sql.Timestamp, status: String)

	def getTournamentUsers(idTournament: Long): Future[Seq[UserRow]] = {
    val q = for {
      ut <- UserTournament if ut.idTournament === idTournament.intValue
      u <- User if u.id === ut.idUser
    } yield u
    
    /*val q2 = q.map {
      case User => new UserRow(_.id, "", "", _.firstName, _.lastName, new Timestamp(0), "")
    }*/

    db.run{
      q.result
    }
  }
}