package repositories

import models.{Payment}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def typeOf=column[Int]("typeOf")
    def * = (id, typeOf) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  val payment = TableQuery[PaymentTable]

  def create(typeOf:Int): Future[Payment] = db.run {
    (payment.map(c => (c.typeOf))
      returning payment.map(_.id)
      into { case ((typeOf), id) => Payment(id, typeOf) }
      ) += (typeOf)
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }
}
