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

class UsersService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
	val dbConfig = dbConfigProvider.get[JdbcProfile]
	val db = dbConfig.db
	import dbConfig.driver.api._

  def viewFilter(user: UserRow, callingUsername: String): UserRow = {
    if (user.eMail != callingUsername) {
      new UserRow(user.id, "", "", user.firstName, user.lastName, new Timestamp(0), "")
    } else {
      user
    }
  }

	def getUser(id: Long, callingUsername: String): Future[UserRow] = {
	  db.run {
	    User.filter(_.id === id.intValue).result.head
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
}