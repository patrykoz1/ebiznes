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
  def getById(id: Int): Future[Shipping] = db.run {
    shipping.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Shipping]] = db.run {
    shipping.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Unit] = db.run(shipping.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_inv: Shipping): Future[Unit] = {
    val comToUpdate: Shipping = new_inv.copy(id)
    db.run(shipping.filter(_.id === id).update(comToUpdate)).map(_ => ())
  }

}
