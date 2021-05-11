package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.UserRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UserController @Inject()(userRepo:UserRepository, cc: MessagesControllerComponents)
                              (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "firstName"->nonEmptyText,
      "secondName"->nonEmptyText,
      "email"->nonEmptyText,
      "password"->nonEmptyText

    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  val updateUserForm: Form[UpdateUserForm] = Form {
    mapping(
      "id" -> number,
      "firstName"->nonEmptyText,
      "secondName"->nonEmptyText,
      "email"->nonEmptyText,
      "password"->nonEmptyText
    )(UpdateUserForm.apply)(UpdateUserForm.unapply)
  }

  def getUsers: Action[AnyContent] = Action.async { implicit request =>
    val users =userRepo.list()
    users.map( comments => Ok(views.html.users(users)))
  }

  def getUser(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val user =userRepo.getById(id)
    user.map(user => user match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    userRepo.delete(id)
    //Redirect("/c")
  }

  def index = Action {
    Ok(views.html.index("GET."))
  }
  def delete = Action {
    Ok(views.html.index("DELETE."))
  }
  def patch = Action {
    Ok(views.html.index("PATCH."))
  }
  def add = Action {
    Ok(views.html.index("POST"))
  }
}
case class CreateUserForm(firstName:String,secondName:String, email:String,password:String)
case class UpdateUserForm(id: Int,firstName:String,secondName:String, email:String,password:String)
