package repositories

import models.{ Shipping}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ShippingRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ShippingTable(tag: Tag) extends Table[Shipping](tag, "shipping") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def orderId = column[Int]("orderId")
    def typeOf = column[Int]("typeOf")
    def * = (id, orderId,typeOf) <> ((Shipping.apply _).tupled, Shipping.unapply)
  }

  val shipping = TableQuery[ShippingTable]

  def create(orderId:Int,typeOf:Int): Future[Shipping] = db.run {
    (shipping.map(c => (c.orderId,c.typeOf))
      returning shipping.map(_.id)
      into { case ((orderId,typeOf), id) => Shipping(id, orderId,typeOf) }
      ) += (orderId,typeOf)
  }

  def list(): Future[Seq[Shipping]] = db.run {
    shipping.result
  }
}
