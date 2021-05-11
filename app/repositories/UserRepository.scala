package repositories

import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("firstName")
    def secondName = column[String]("secondName")
    def email = column[String]("email")
    def password = column[String]("password")
    def * = (id, firstName,secondName,email,password) <> ((User.apply _).tupled, User.unapply)
  }

  val user = TableQuery[UserTable]

  def create(firstName:String,secondName:String, email:String,password:String): Future[User] = db.run {
    (user.map(c => (c.firstName,c.secondName,c.email,c.password))
      returning user.map(_.id)
      into { case ((firstName,secondName, email,password), id) => User(id, firstName,secondName, email,password) }
      ) += (firstName,secondName, email,password)
  }

  def list(): Future[Seq[User]] = db.run {
    user.result
  }
  def getById(id: Int): Future[User] = db.run {
    user.filter(_.id === id).result.head
  }

  def getByIdOption(id: Int): Future[Option[User]] = db.run {
    user.filter(_.id === id).result.headOption
  }


  def delete(id: Int): Future[Unit] = db.run(user.filter(_.id === id).delete).map(_ => ())

  def update(id: Int, new_inv: User): Future[Unit] = {
    val comToUpdate: User = new_inv.copy(id)
    db.run(user.filter(_.id === id).update(comToUpdate)).map(_ => ())
  }
}
