package repositories

import models.{Category, Product}

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def description: Rep[String] = column[String]("description")
    def * = (id, name,description) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def create(name: String,description: String): Future[Category] = db.run {
    (category.map(c => (c.name,c.description))
      returning category.map(_.id)
      into { case ((name, description), id) => Category(id, name, description) }
      ) += (name,description)
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def getById(id: Int): Future[Category] = db.run {
    category.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Category]] = db.run {
    category.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Unit] = db.run(category.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_cat: Category): Future[Unit] = {
    val catToUpdate: Category = new_cat.copy(id)
    db.run(category.filter(_.id === id).update(catToUpdate)).map(_ => ())
  }
}
