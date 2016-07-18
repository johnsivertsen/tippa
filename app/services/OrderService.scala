package services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import db.Tables._
import maps.Converters._
import maps.OrderMap
import org.joda.time.DateTime
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

class OrderService @Inject()(dbConfigProvider: DatabaseConfigProvider) {
	val dbConfig = dbConfigProvider.get[JdbcProfile]
	val db = dbConfig.db
	import dbConfig.driver.api._

	def getOrders: Future[Seq[OrderMap]] = db.run {
		Order.result.map(res => res.map(row => OrderMap(Some(row.id), Some(row.date), row.name, row.description)))
	}

	def upsertOrder(order: OrderMap): Future[Int] = db.run {
		Order.insertOrUpdate(OrderRow(order.id.getOrElse(0), order.date.getOrElse[DateTime](DateTime.now()), order.name, order.description))
	}
}