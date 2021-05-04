package repositories

import models.Order
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "order") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def productId:Rep[Int] = column[Int]("productId")
    def customerId: Rep[Int] = column[Int]("customerId")
    def * = (id, productId,customerId) <> ((Order.apply _).tupled, Order.unapply)
  }

  val order = TableQuery[OrderTable]

  def create(productId:Int,customerId:Int): Future[Order] = db.run {
    (order.map(c => (c.productId,c.customerId))
      returning order.map(_.id)
      into { case ((productId,customerId), id) => Order(id, productId,customerId) }
      ) += (productId,customerId)
  }

  def list(): Future[Seq[Order]] = db.run {
    order.result
  }
}
