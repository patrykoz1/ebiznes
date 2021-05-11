package repositories

import models.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "product") {
    def id:Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name:Rep[String] = column[String]("name")
    def description: Rep[String] = column[String]("description")
    //def productId:Rep[Int] = column[Int]("productId")
    def categoryId:Rep[Int] = column[Int]("categoryId")
    def * = (id, name,description,categoryId) <> ((Product.apply _).tupled, Product.unapply)
  }

  val product = TableQuery[ProductTable]

  def create(name: String,description: String,productId: Int,categoryId: Int): Future[Product] = db.run {
    (product.map(c => (c.name,c.description,c.categoryId))
      returning product.map(_.id)
      into { case ((name, description,categoryId), id) => Product(id, name, description,categoryId) }
      ) += (name,description,categoryId)
  }

  def list(): Future[Seq[Product]] = db.run {
    product.result
  }
  def getByCategory(category_id: Int): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId === category_id).result
  }

  def getById(id: Int): Future[Product] = db.run {
    product.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def getByCategories(category_ids: List[Int]): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId inSet category_ids).result
  }

  def delete(id: Int): Future[Unit] = db.run(product.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_product: Product): Future[Unit] = {
    val productToUpdate: Product = new_product.copy(id)
    db.run(product.filter(_.id === id).update(productToUpdate)).map(_ => ())
  }
}
