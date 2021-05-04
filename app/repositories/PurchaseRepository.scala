package repositories

import models.{Purchase}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PurchaseRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PurchaseTable(tag: Tag) extends Table[Purchase](tag, "purchase") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def orderId = column[Int]("orderId")
    def customerId = column[Int]("customerId")
    def * = (id, orderId,customerId) <> ((Purchase.apply _).tupled, Purchase.unapply)
  }

  val purchase = TableQuery[PurchaseTable]

  def create(orderId:Int,customerId:Int): Future[Purchase] = db.run {
    (purchase.map(c => (c.orderId,c.customerId))
      returning purchase.map(_.id)
      into { case ((orderId,customerId), id) => Purchase(id, orderId,customerId) }
      ) += (orderId,customerId)
  }

  def list(): Future[Seq[Purchase]] = db.run {
    purchase.result
  }
}
