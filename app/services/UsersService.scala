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

  def authenticate(username: String, password: String): Future[(Boolean, Option[String])] = {
    val q = for {
      (u, r) <- User.filter(user => user.eMail === username) joinLeft
        UserRole.filter(_.role === "ADMIN") on (_.id === _.idUser)
    } yield (u, r)
    
    db.run {
      q.result.headOption
    }.map { ur =>
      ur match {
        case Some(ur) => {
          ur match {
            case (u, Some(r)) => (BCrypt.checkpw(password, u.password), Some(r.role))
            case (u, None) => (BCrypt.checkpw(password, u.password), None)
            case _ => (false, None)
          }
        }
        case _ => (false, None)
      }
    }
  }

  def getTournamentUsers(idTournament: Long): Future[Seq[UserRow]] = {
    val q = sql"""select u.id, null, null, u.first_name, u.last_name, PARSEDATETIME('1970-01-01', 'yyyy-MM-dd'), null from
      user u, user_tournament ut
      where u.id = ut.id_user and
        ut.id_tournament = $idTournament
    """.as[UserRow]

    db.run{
      q
    }
  }
  
  def postUser(userInput: UserRow): Future[Int] = {
    val salt = BCrypt.gensalt(12);
    val hashed_password = BCrypt.hashpw(userInput.password, salt);

    import java.util.Calendar
    
    val u = userInput.copy(id = 0, eMail = userInput.eMail.toLowerCase, password = hashed_password, createdDate = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()), status = "CREATED")
    val uId = (User returning User.map(_.id)) += u

    import scala.util.Try
    import scala.util.Success
    import scala.util.Failure

    db.run(uId.asTry).map { result =>
      result match {
        case Success(res) =>
          Logger.error("Successfully created new user. Id: " + res)
          res
        case Failure(e) => {
          e match {
            case ex: org.h2.jdbc.JdbcSQLException => {
              Logger.error("User creation failed: JdbcSQLException: " + ex.getMessage)
              -1
            }
            case _: AnyRef => {
              Logger.error("User creation failed: Unkown error: " + e.getMessage)
              -2
            }
          }
        }
      }
    }
  }
  
  def getUserIdFromEmail(eMail: String): Future[Option[Int]] = {
    val q = for {
        u <- User.filter(_.eMail === eMail.toLowerCase)
      } yield u.id

    db.run {
      q.result.headOption
    }
  }
}