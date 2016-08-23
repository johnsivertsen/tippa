package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.H2Driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Bet.schema, Fixture.schema, FriendGroup.schema, FriendGroupUser.schema, Log.schema, Message.schema, PlayEvolutions.schema, Round.schema, Team.schema, Tournament.schema, TournamentTeam.schema, User.schema, UserRole.schema, UserTournament.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Bet
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idFixture Database column ID_FIXTURE SqlType(INTEGER)
   *  @param idUser Database column ID_USER SqlType(INTEGER)
   *  @param homePoints Database column HOME_POINTS SqlType(INTEGER)
   *  @param awayPoints Database column AWAY_POINTS SqlType(INTEGER)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class BetRow(id: Int, idFixture: Int, idUser: Int, homePoints: Option[Int], awayPoints: Option[Int], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching BetRow objects using plain SQL queries */
  implicit def GetResultBetRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[java.sql.Timestamp]): GR[BetRow] = GR{
    prs => import prs._
    BetRow.tupled((<<[Int], <<[Int], <<[Int], <<?[Int], <<?[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table BET. Objects of this class serve as prototypes for rows in queries. */
  class Bet(_tableTag: Tag) extends Table[BetRow](_tableTag, "BET") {
    def * = (id, idFixture, idUser, homePoints, awayPoints, createdDate) <> (BetRow.tupled, BetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idFixture), Rep.Some(idUser), homePoints, awayPoints, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> BetRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_FIXTURE SqlType(INTEGER) */
    val idFixture: Rep[Int] = column[Int]("ID_FIXTURE")
    /** Database column ID_USER SqlType(INTEGER) */
    val idUser: Rep[Int] = column[Int]("ID_USER")
    /** Database column HOME_POINTS SqlType(INTEGER) */
    val homePoints: Rep[Option[Int]] = column[Option[Int]]("HOME_POINTS")
    /** Database column AWAY_POINTS SqlType(INTEGER) */
    val awayPoints: Rep[Option[Int]] = column[Option[Int]]("AWAY_POINTS")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing Fixture (database name CONSTRAINT_10) */
    lazy val fixtureFk = foreignKey("CONSTRAINT_10", idFixture, Fixture)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing User (database name CONSTRAINT_100) */
    lazy val userFk = foreignKey("CONSTRAINT_100", idUser, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idFixture,idUser) (database name CONSTRAINT_INDEX_100) */
    val index1 = index("CONSTRAINT_INDEX_100", (idFixture, idUser), unique=true)
  }
  /** Collection-like TableQuery object for table Bet */
  lazy val Bet = new TableQuery(tag => new Bet(tag))

  /** Entity class storing rows of table Fixture
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idRound Database column ID_ROUND SqlType(INTEGER)
   *  @param idTeamHome Database column ID_TEAM_HOME SqlType(INTEGER)
   *  @param idTeamAway Database column ID_TEAM_AWAY SqlType(INTEGER)
   *  @param homePoints Database column HOME_POINTS SqlType(INTEGER)
   *  @param awayPoints Database column AWAY_POINTS SqlType(INTEGER)
   *  @param homePointsAwarded Database column HOME_POINTS_AWARDED SqlType(INTEGER)
   *  @param awayPointsAwarded Database column AWAY_POINTS_AWARDED SqlType(INTEGER)
   *  @param startTime Database column START_TIME SqlType(TIMESTAMP)
   *  @param status Database column STATUS SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class FixtureRow(id: Int, idRound: Int, idTeamHome: Int, idTeamAway: Int, homePoints: Option[Int], awayPoints: Option[Int], homePointsAwarded: Option[Int], awayPointsAwarded: Option[Int], startTime: Option[java.sql.Timestamp], status: Option[String], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching FixtureRow objects using plain SQL queries */
  implicit def GetResultFixtureRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[java.sql.Timestamp]], e3: GR[Option[String]], e4: GR[java.sql.Timestamp]): GR[FixtureRow] = GR{
    prs => import prs._
    FixtureRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[java.sql.Timestamp], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table FIXTURE. Objects of this class serve as prototypes for rows in queries. */
  class Fixture(_tableTag: Tag) extends Table[FixtureRow](_tableTag, "FIXTURE") {
    def * = (id, idRound, idTeamHome, idTeamAway, homePoints, awayPoints, homePointsAwarded, awayPointsAwarded, startTime, status, createdDate) <> (FixtureRow.tupled, FixtureRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idRound), Rep.Some(idTeamHome), Rep.Some(idTeamAway), homePoints, awayPoints, homePointsAwarded, awayPointsAwarded, startTime, status, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> FixtureRow.tupled((_1.get, _2.get, _3.get, _4.get, _5, _6, _7, _8, _9, _10, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_ROUND SqlType(INTEGER) */
    val idRound: Rep[Int] = column[Int]("ID_ROUND")
    /** Database column ID_TEAM_HOME SqlType(INTEGER) */
    val idTeamHome: Rep[Int] = column[Int]("ID_TEAM_HOME")
    /** Database column ID_TEAM_AWAY SqlType(INTEGER) */
    val idTeamAway: Rep[Int] = column[Int]("ID_TEAM_AWAY")
    /** Database column HOME_POINTS SqlType(INTEGER) */
    val homePoints: Rep[Option[Int]] = column[Option[Int]]("HOME_POINTS")
    /** Database column AWAY_POINTS SqlType(INTEGER) */
    val awayPoints: Rep[Option[Int]] = column[Option[Int]]("AWAY_POINTS")
    /** Database column HOME_POINTS_AWARDED SqlType(INTEGER) */
    val homePointsAwarded: Rep[Option[Int]] = column[Option[Int]]("HOME_POINTS_AWARDED")
    /** Database column AWAY_POINTS_AWARDED SqlType(INTEGER) */
    val awayPointsAwarded: Rep[Option[Int]] = column[Option[Int]]("AWAY_POINTS_AWARDED")
    /** Database column START_TIME SqlType(TIMESTAMP) */
    val startTime: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("START_TIME")
    /** Database column STATUS SqlType(VARCHAR) */
    val status: Rep[Option[String]] = column[Option[String]]("STATUS")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing Team (database name CONSTRAINT_F88) */
    lazy val teamFk1 = foreignKey("CONSTRAINT_F88", idTeamHome, Team)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Team (database name CONSTRAINT_F885) */
    lazy val teamFk2 = foreignKey("CONSTRAINT_F885", idTeamAway, Team)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idRound,idTeamHome) (database name CONSTRAINT_INDEX_F885) */
    val index1 = index("CONSTRAINT_INDEX_F885", (idRound, idTeamHome), unique=true)
    /** Uniqueness Index over (idRound,idTeamAway) (database name CONSTRAINT_INDEX_F8858) */
    val index2 = index("CONSTRAINT_INDEX_F8858", (idRound, idTeamAway), unique=true)
  }
  /** Collection-like TableQuery object for table Fixture */
  lazy val Fixture = new TableQuery(tag => new Fixture(tag))

  /** Entity class storing rows of table FriendGroup
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param adminUser Database column ADMIN_USER SqlType(INTEGER)
   *  @param name Database column NAME SqlType(VARCHAR)
   *  @param description Database column DESCRIPTION SqlType(VARCHAR)
   *  @param status Database column STATUS SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class FriendGroupRow(id: Int, adminUser: Int, name: String, description: Option[String], status: Option[String], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching FriendGroupRow objects using plain SQL queries */
  implicit def GetResultFriendGroupRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[FriendGroupRow] = GR{
    prs => import prs._
    FriendGroupRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table FRIEND_GROUP. Objects of this class serve as prototypes for rows in queries. */
  class FriendGroup(_tableTag: Tag) extends Table[FriendGroupRow](_tableTag, "FRIEND_GROUP") {
    def * = (id, adminUser, name, description, status, createdDate) <> (FriendGroupRow.tupled, FriendGroupRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(adminUser), Rep.Some(name), description, status, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> FriendGroupRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ADMIN_USER SqlType(INTEGER) */
    val adminUser: Rep[Int] = column[Int]("ADMIN_USER")
    /** Database column NAME SqlType(VARCHAR) */
    val name: Rep[String] = column[String]("NAME")
    /** Database column DESCRIPTION SqlType(VARCHAR) */
    val description: Rep[Option[String]] = column[Option[String]]("DESCRIPTION")
    /** Database column STATUS SqlType(VARCHAR) */
    val status: Rep[Option[String]] = column[Option[String]]("STATUS")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing User (database name CONSTRAINT_C5) */
    lazy val userFk = foreignKey("CONSTRAINT_C5", adminUser, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table FriendGroup */
  lazy val FriendGroup = new TableQuery(tag => new FriendGroup(tag))

  /** Entity class storing rows of table FriendGroupUser
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idUser Database column ID_USER SqlType(INTEGER)
   *  @param idFriendGroup Database column ID_FRIEND_GROUP SqlType(INTEGER)
   *  @param status Database column STATUS SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class FriendGroupUserRow(id: Int, idUser: Int, idFriendGroup: Int, status: Option[String], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching FriendGroupUserRow objects using plain SQL queries */
  implicit def GetResultFriendGroupUserRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[java.sql.Timestamp]): GR[FriendGroupUserRow] = GR{
    prs => import prs._
    FriendGroupUserRow.tupled((<<[Int], <<[Int], <<[Int], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table FRIEND_GROUP_USER. Objects of this class serve as prototypes for rows in queries. */
  class FriendGroupUser(_tableTag: Tag) extends Table[FriendGroupUserRow](_tableTag, "FRIEND_GROUP_USER") {
    def * = (id, idUser, idFriendGroup, status, createdDate) <> (FriendGroupUserRow.tupled, FriendGroupUserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idUser), Rep.Some(idFriendGroup), status, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> FriendGroupUserRow.tupled((_1.get, _2.get, _3.get, _4, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_USER SqlType(INTEGER) */
    val idUser: Rep[Int] = column[Int]("ID_USER")
    /** Database column ID_FRIEND_GROUP SqlType(INTEGER) */
    val idFriendGroup: Rep[Int] = column[Int]("ID_FRIEND_GROUP")
    /** Database column STATUS SqlType(VARCHAR) */
    val status: Rep[Option[String]] = column[Option[String]]("STATUS")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing FriendGroup (database name CONSTRAINT_329) */
    lazy val friendGroupFk = foreignKey("CONSTRAINT_329", idFriendGroup, FriendGroup)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing User (database name CONSTRAINT_32) */
    lazy val userFk = foreignKey("CONSTRAINT_32", idUser, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idUser,idFriendGroup) (database name CONSTRAINT_INDEX_329) */
    val index1 = index("CONSTRAINT_INDEX_329", (idUser, idFriendGroup), unique=true)
  }
  /** Collection-like TableQuery object for table FriendGroupUser */
  lazy val FriendGroupUser = new TableQuery(tag => new FriendGroupUser(tag))

  /** Entity class storing rows of table Log
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param key1 Database column KEY_1 SqlType(INTEGER)
   *  @param key2 Database column KEY_2 SqlType(INTEGER)
   *  @param `type` Database column TYPE SqlType(VARCHAR)
   *  @param message Database column MESSAGE SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class LogRow(id: Int, key1: Option[Int], key2: Option[Int], `type`: Option[String], message: Option[String], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching LogRow objects using plain SQL queries */
  implicit def GetResultLogRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[LogRow] = GR{
    prs => import prs._
    LogRow.tupled((<<[Int], <<?[Int], <<?[Int], <<?[String], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table LOG. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Log(_tableTag: Tag) extends Table[LogRow](_tableTag, "LOG") {
    def * = (id, key1, key2, `type`, message, createdDate) <> (LogRow.tupled, LogRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), key1, key2, `type`, message, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> LogRow.tupled((_1.get, _2, _3, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column KEY_1 SqlType(INTEGER) */
    val key1: Rep[Option[Int]] = column[Option[Int]]("KEY_1")
    /** Database column KEY_2 SqlType(INTEGER) */
    val key2: Rep[Option[Int]] = column[Option[Int]]("KEY_2")
    /** Database column TYPE SqlType(VARCHAR)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Option[String]] = column[Option[String]]("TYPE")
    /** Database column MESSAGE SqlType(VARCHAR) */
    val message: Rep[Option[String]] = column[Option[String]]("MESSAGE")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")
  }
  /** Collection-like TableQuery object for table Log */
  lazy val Log = new TableQuery(tag => new Log(tag))

  /** Entity class storing rows of table Message
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param recipientId Database column RECIPIENT_ID SqlType(INTEGER)
   *  @param `type` Database column TYPE SqlType(VARCHAR)
   *  @param message Database column MESSAGE SqlType(VARCHAR)
   *  @param status Database column STATUS SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class MessageRow(id: Int, recipientId: Int, `type`: Option[String], message: Option[String], status: Option[String], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[java.sql.Timestamp]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Int], <<[Int], <<?[String], <<?[String], <<?[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table MESSAGE. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Message(_tableTag: Tag) extends Table[MessageRow](_tableTag, "MESSAGE") {
    def * = (id, recipientId, `type`, message, status, createdDate) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(recipientId), `type`, message, status, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2.get, _3, _4, _5, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column RECIPIENT_ID SqlType(INTEGER) */
    val recipientId: Rep[Int] = column[Int]("RECIPIENT_ID")
    /** Database column TYPE SqlType(VARCHAR)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[Option[String]] = column[Option[String]]("TYPE")
    /** Database column MESSAGE SqlType(VARCHAR) */
    val message: Rep[Option[String]] = column[Option[String]]("MESSAGE")
    /** Database column STATUS SqlType(VARCHAR) */
    val status: Rep[Option[String]] = column[Option[String]]("STATUS")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column ID SqlType(INTEGER), PrimaryKey
   *  @param hash Database column HASH SqlType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column APPLIED_AT SqlType(TIMESTAMP)
   *  @param applyScript Database column APPLY_SCRIPT SqlType(CLOB)
   *  @param revertScript Database column REVERT_SCRIPT SqlType(CLOB)
   *  @param state Database column STATE SqlType(VARCHAR), Length(255,true)
   *  @param lastProblem Database column LAST_PROBLEM SqlType(CLOB) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[java.sql.Clob], revertScript: Option[java.sql.Clob], state: Option[String], lastProblem: Option[java.sql.Clob])
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Clob]], e4: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[java.sql.Clob], <<?[java.sql.Clob], <<?[String], <<?[java.sql.Clob]))
  }
  /** Table description of table PLAY_EVOLUTIONS. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "PLAY_EVOLUTIONS") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.PrimaryKey)
    /** Database column HASH SqlType(VARCHAR), Length(255,true) */
    val hash: Rep[String] = column[String]("HASH", O.Length(255,varying=true))
    /** Database column APPLIED_AT SqlType(TIMESTAMP) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("APPLIED_AT")
    /** Database column APPLY_SCRIPT SqlType(CLOB) */
    val applyScript: Rep[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("APPLY_SCRIPT")
    /** Database column REVERT_SCRIPT SqlType(CLOB) */
    val revertScript: Rep[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("REVERT_SCRIPT")
    /** Database column STATE SqlType(VARCHAR), Length(255,true) */
    val state: Rep[Option[String]] = column[Option[String]]("STATE", O.Length(255,varying=true))
    /** Database column LAST_PROBLEM SqlType(CLOB) */
    val lastProblem: Rep[Option[java.sql.Clob]] = column[Option[java.sql.Clob]]("LAST_PROBLEM")
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Round
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idTournament Database column ID_TOURNAMENT SqlType(INTEGER)
   *  @param number Database column NUMBER SqlType(INTEGER)
   *  @param designatedDate Database column DESIGNATED_DATE SqlType(TIMESTAMP)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class RoundRow(id: Int, idTournament: Int, number: Int, designatedDate: java.sql.Timestamp, createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching RoundRow objects using plain SQL queries */
  implicit def GetResultRoundRow(implicit e0: GR[Int], e1: GR[java.sql.Timestamp]): GR[RoundRow] = GR{
    prs => import prs._
    RoundRow.tupled((<<[Int], <<[Int], <<[Int], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table ROUND. Objects of this class serve as prototypes for rows in queries. */
  class Round(_tableTag: Tag) extends Table[RoundRow](_tableTag, "ROUND") {
    def * = (id, idTournament, number, designatedDate, createdDate) <> (RoundRow.tupled, RoundRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idTournament), Rep.Some(number), Rep.Some(designatedDate), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> RoundRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_TOURNAMENT SqlType(INTEGER) */
    val idTournament: Rep[Int] = column[Int]("ID_TOURNAMENT")
    /** Database column NUMBER SqlType(INTEGER) */
    val number: Rep[Int] = column[Int]("NUMBER")
    /** Database column DESIGNATED_DATE SqlType(TIMESTAMP) */
    val designatedDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("DESIGNATED_DATE")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing Tournament (database name CONSTRAINT_4A) */
    lazy val tournamentFk = foreignKey("CONSTRAINT_4A", idTournament, Tournament)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idTournament,number) (database name CONSTRAINT_INDEX_4A) */
    val index1 = index("CONSTRAINT_INDEX_4A", (idTournament, number), unique=true)
  }
  /** Collection-like TableQuery object for table Round */
  lazy val Round = new TableQuery(tag => new Round(tag))

  /** Entity class storing rows of table Team
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param shortName Database column SHORT_NAME SqlType(VARCHAR)
   *  @param longName Database column LONG_NAME SqlType(VARCHAR)
   *  @param description Database column DESCRIPTION SqlType(VARCHAR)
   *  @param `type` Database column TYPE SqlType(VARCHAR), Default(football)
   *  @param country Database column COUNTRY SqlType(VARCHAR), Default(INTERNATIONAL)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class TeamRow(id: Int, shortName: String, longName: Option[String], description: Option[String], `type`: String = "football", country: String = "INTERNATIONAL", createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching TeamRow objects using plain SQL queries */
  implicit def GetResultTeamRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[TeamRow] = GR{
    prs => import prs._
    TeamRow.tupled((<<[Int], <<[String], <<?[String], <<?[String], <<[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table TEAM. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Team(_tableTag: Tag) extends Table[TeamRow](_tableTag, "TEAM") {
    def * = (id, shortName, longName, description, `type`, country, createdDate) <> (TeamRow.tupled, TeamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(shortName), longName, description, Rep.Some(`type`), Rep.Some(country), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> TeamRow.tupled((_1.get, _2.get, _3, _4, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column SHORT_NAME SqlType(VARCHAR) */
    val shortName: Rep[String] = column[String]("SHORT_NAME")
    /** Database column LONG_NAME SqlType(VARCHAR) */
    val longName: Rep[Option[String]] = column[Option[String]]("LONG_NAME")
    /** Database column DESCRIPTION SqlType(VARCHAR) */
    val description: Rep[Option[String]] = column[Option[String]]("DESCRIPTION")
    /** Database column TYPE SqlType(VARCHAR), Default(football)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("TYPE", O.Default("football"))
    /** Database column COUNTRY SqlType(VARCHAR), Default(INTERNATIONAL) */
    val country: Rep[String] = column[String]("COUNTRY", O.Default("INTERNATIONAL"))
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")
  }
  /** Collection-like TableQuery object for table Team */
  lazy val Team = new TableQuery(tag => new Team(tag))

  /** Entity class storing rows of table Tournament
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param shortName Database column SHORT_NAME SqlType(VARCHAR)
   *  @param fullName Database column FULL_NAME SqlType(VARCHAR)
   *  @param `type` Database column TYPE SqlType(VARCHAR), Default(football)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class TournamentRow(id: Int, shortName: String, fullName: Option[String], `type`: String = "football", createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching TournamentRow objects using plain SQL queries */
  implicit def GetResultTournamentRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[TournamentRow] = GR{
    prs => import prs._
    TournamentRow.tupled((<<[Int], <<[String], <<?[String], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table TOURNAMENT. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class Tournament(_tableTag: Tag) extends Table[TournamentRow](_tableTag, "TOURNAMENT") {
    def * = (id, shortName, fullName, `type`, createdDate) <> (TournamentRow.tupled, TournamentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(shortName), fullName, Rep.Some(`type`), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> TournamentRow.tupled((_1.get, _2.get, _3, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column SHORT_NAME SqlType(VARCHAR) */
    val shortName: Rep[String] = column[String]("SHORT_NAME")
    /** Database column FULL_NAME SqlType(VARCHAR) */
    val fullName: Rep[Option[String]] = column[Option[String]]("FULL_NAME")
    /** Database column TYPE SqlType(VARCHAR), Default(football)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("TYPE", O.Default("football"))
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")
  }
  /** Collection-like TableQuery object for table Tournament */
  lazy val Tournament = new TableQuery(tag => new Tournament(tag))

  /** Entity class storing rows of table TournamentTeam
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idTournament Database column ID_TOURNAMENT SqlType(INTEGER)
   *  @param idTeam Database column ID_TEAM SqlType(INTEGER)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class TournamentTeamRow(id: Int, idTournament: Option[Int], idTeam: Option[Int], createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching TournamentTeamRow objects using plain SQL queries */
  implicit def GetResultTournamentTeamRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[java.sql.Timestamp]): GR[TournamentTeamRow] = GR{
    prs => import prs._
    TournamentTeamRow.tupled((<<[Int], <<?[Int], <<?[Int], <<[java.sql.Timestamp]))
  }
  /** Table description of table TOURNAMENT_TEAM. Objects of this class serve as prototypes for rows in queries. */
  class TournamentTeam(_tableTag: Tag) extends Table[TournamentTeamRow](_tableTag, "TOURNAMENT_TEAM") {
    def * = (id, idTournament, idTeam, createdDate) <> (TournamentTeamRow.tupled, TournamentTeamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), idTournament, idTeam, Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> TournamentTeamRow.tupled((_1.get, _2, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_TOURNAMENT SqlType(INTEGER) */
    val idTournament: Rep[Option[Int]] = column[Option[Int]]("ID_TOURNAMENT")
    /** Database column ID_TEAM SqlType(INTEGER) */
    val idTeam: Rep[Option[Int]] = column[Option[Int]]("ID_TEAM")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing Team (database name CONSTRAINT_875) */
    lazy val teamFk = foreignKey("CONSTRAINT_875", idTeam, Team)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing Tournament (database name CONSTRAINT_87) */
    lazy val tournamentFk = foreignKey("CONSTRAINT_87", idTournament, Tournament)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idTournament,idTeam) (database name CONSTRAINT_INDEX_875) */
    val index1 = index("CONSTRAINT_INDEX_875", (idTournament, idTeam), unique=true)
  }
  /** Collection-like TableQuery object for table TournamentTeam */
  lazy val TournamentTeam = new TableQuery(tag => new TournamentTeam(tag))

  /** Entity class storing rows of table User
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param eMail Database column E_MAIL SqlType(VARCHAR)
   *  @param password Database column PASSWORD SqlType(VARCHAR)
   *  @param firstName Database column FIRST_NAME SqlType(VARCHAR)
   *  @param lastName Database column LAST_NAME SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP)
   *  @param status Database column STATUS SqlType(VARCHAR) */
  case class UserRow(id: Int, eMail: String, password: String, firstName: String, lastName: String, createdDate: java.sql.Timestamp, status: String)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[String]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "USER") {
    def * = (id, eMail, password, firstName, lastName, createdDate, status) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(eMail), Rep.Some(password), Rep.Some(firstName), Rep.Some(lastName), Rep.Some(createdDate), Rep.Some(status)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column E_MAIL SqlType(VARCHAR) */
    val eMail: Rep[String] = column[String]("E_MAIL")
    /** Database column PASSWORD SqlType(VARCHAR) */
    val password: Rep[String] = column[String]("PASSWORD")
    /** Database column FIRST_NAME SqlType(VARCHAR) */
    val firstName: Rep[String] = column[String]("FIRST_NAME")
    /** Database column LAST_NAME SqlType(VARCHAR) */
    val lastName: Rep[String] = column[String]("LAST_NAME")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")
    /** Database column STATUS SqlType(VARCHAR) */
    val status: Rep[String] = column[String]("STATUS")
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))

  /** Entity class storing rows of table UserRole
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idUser Database column ID_USER SqlType(INTEGER)
   *  @param role Database column ROLE SqlType(VARCHAR)
   *  @param createdDate Database column CREATED_DATE SqlType(TIMESTAMP) */
  case class UserRoleRow(id: Int, idUser: Int, role: String, createdDate: java.sql.Timestamp)
  /** GetResult implicit for fetching UserRoleRow objects using plain SQL queries */
  implicit def GetResultUserRoleRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[UserRoleRow] = GR{
    prs => import prs._
    UserRoleRow.tupled((<<[Int], <<[Int], <<[String], <<[java.sql.Timestamp]))
  }
  /** Table description of table USER_ROLE. Objects of this class serve as prototypes for rows in queries. */
  class UserRole(_tableTag: Tag) extends Table[UserRoleRow](_tableTag, "USER_ROLE") {
    def * = (id, idUser, role, createdDate) <> (UserRoleRow.tupled, UserRoleRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idUser), Rep.Some(role), Rep.Some(createdDate)).shaped.<>({r=>import r._; _1.map(_=> UserRoleRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_USER SqlType(INTEGER) */
    val idUser: Rep[Int] = column[Int]("ID_USER")
    /** Database column ROLE SqlType(VARCHAR) */
    val role: Rep[String] = column[String]("ROLE")
    /** Database column CREATED_DATE SqlType(TIMESTAMP) */
    val createdDate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_DATE")

    /** Foreign key referencing User (database name CONSTRAINT_BC) */
    lazy val userFk = foreignKey("CONSTRAINT_BC", idUser, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table UserRole */
  lazy val UserRole = new TableQuery(tag => new UserRole(tag))

  /** Entity class storing rows of table UserTournament
   *  @param id Database column ID SqlType(INTEGER), AutoInc, PrimaryKey
   *  @param idUser Database column ID_USER SqlType(INTEGER)
   *  @param idTournament Database column ID_TOURNAMENT SqlType(INTEGER) */
  case class UserTournamentRow(id: Int, idUser: Int, idTournament: Int)
  /** GetResult implicit for fetching UserTournamentRow objects using plain SQL queries */
  implicit def GetResultUserTournamentRow(implicit e0: GR[Int]): GR[UserTournamentRow] = GR{
    prs => import prs._
    UserTournamentRow.tupled((<<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table USER_TOURNAMENT. Objects of this class serve as prototypes for rows in queries. */
  class UserTournament(_tableTag: Tag) extends Table[UserTournamentRow](_tableTag, "USER_TOURNAMENT") {
    def * = (id, idUser, idTournament) <> (UserTournamentRow.tupled, UserTournamentRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(idUser), Rep.Some(idTournament)).shaped.<>({r=>import r._; _1.map(_=> UserTournamentRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(INTEGER), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column ID_USER SqlType(INTEGER) */
    val idUser: Rep[Int] = column[Int]("ID_USER")
    /** Database column ID_TOURNAMENT SqlType(INTEGER) */
    val idTournament: Rep[Int] = column[Int]("ID_TOURNAMENT")

    /** Foreign key referencing Tournament (database name CONSTRAINT_F36) */
    lazy val tournamentFk = foreignKey("CONSTRAINT_F36", idTournament, Tournament)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
    /** Foreign key referencing User (database name CONSTRAINT_F3) */
    lazy val userFk = foreignKey("CONSTRAINT_F3", idUser, User)(r => r.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)

    /** Uniqueness Index over (idUser,idTournament) (database name CONSTRAINT_INDEX_F36) */
    val index1 = index("CONSTRAINT_INDEX_F36", (idUser, idTournament), unique=true)
  }
  /** Collection-like TableQuery object for table UserTournament */
  lazy val UserTournament = new TableQuery(tag => new UserTournament(tag))
}
