package repositories

import models.{Purchase, Rate}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RateRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class RateTable(tag: Tag) extends Table[Rate](tag, "rate") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def productId = column[Int]("productId")
    def customerId = column[Int]("customerId")
    def grade = column[Int]("grade")

    def * = (id, productId,customerId,grade) <> ((Rate.apply _).tupled, Rate.unapply)
  }

  val rate = TableQuery[RateTable]

  def create(productId: Int,customerId: Int,grade:Int): Future[Rate] = db.run {
    (rate.map(c => (c.productId,c.customerId,c.grade))
      returning rate.map(_.id)
      into { case ((productId,customerId,grade), id) => Rate(id, productId,customerId,grade) }
      ) += (productId,customerId,grade)
  }

  def list(): Future[Seq[Rate]] = db.run {
    rate.result
  }
  def getById(id: Int): Future[Rate] = db.run {
    rate.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Rate]] = db.run {
    rate.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Unit] = db.run(rate.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_inv: Rate): Future[Unit] = {
    val comToUpdate: Rate = new_inv.copy(id)
    db.run(rate.filter(_.id === id).update(comToUpdate)).map(_ => ())
  }

}
