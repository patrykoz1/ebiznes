package repositories
import models.Comment

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CommentRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CommentTable(tag: Tag) extends Table[Comment](tag, "comment") {
    def id:Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def body:Rep[String] = column[String]("body")
    def productId: Rep[Int] = column[Int]("productId")
    def customerId: Rep[Int] = column[Int]("customerId")

    def * = (id, body,productId,customerId) <> ((Comment.apply _).tupled, Comment.unapply)
  }

  val comment = TableQuery[CommentTable]

  def create(body: String,productId: Int,costumerId: Int): Future[Comment] = db.run {
    (comment.map(c => (c.body,c.productId,c.customerId))
      returning comment.map(_.id)
      into { case ((body, productId, customerId), id) => Comment(id, body,productId,customerId) }
      ) += (body,productId,costumerId)
  }

  def list(): Future[Seq[Comment]] = db.run {
    comment.result
  }
}