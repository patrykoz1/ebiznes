package repositories

import models.{Comment, Invoice}

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class InvoiceRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class InvoiceTable(tag: Tag) extends Table[Invoice](tag, "invoice") {
    def id:Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def orderId:Rep[Int] = column[Int]("orderId")
    def customerId: Rep[Int] = column[Int]("customerId")
    def typeOf: Rep[Int] = column[Int]("typeOf")
    def * = (id, orderId,customerId,typeOf) <> ((Invoice.apply _).tupled, Invoice.unapply)
  }

  val invoice = TableQuery[InvoiceTable]

  def create(orderId:Int, customerId:Int, typeOf:Int): Future[Invoice] = db.run {
    (invoice.map(c => (c.orderId,c.customerId,c.typeOf))
      returning invoice.map(_.id)
      into { case ((orderId,customerId,typeOf), id) => Invoice(id, orderId,customerId,typeOf) }
      ) += ( orderId, customerId, typeOf)
  }

  def list(): Future[Seq[Invoice]] = db.run {
    invoice.result
  }
  def getById(id: Int): Future[Invoice] = db.run {
    invoice.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Invoice]] = db.run {
    invoice.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Unit] = db.run(invoice.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_inv: Invoice): Future[Unit] = {
    val comToUpdate: Invoice = new_inv.copy(id)
    db.run(invoice.filter(_.id === id).update(comToUpdate)).map(_ => ())
  }
}
