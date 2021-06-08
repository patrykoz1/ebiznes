package controllers

import models.Category
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{CategoryRepository, ProductRepository}
import play.api.libs.json.Json

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class CategoryController @Inject()(productsRepo:ProductRepository,categoryRepo:CategoryRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }

  def getCategoriesJSON: Action[AnyContent] = Action.async { implicit request =>
    val categories = categoryRepo.list()
    categories.map(res => Ok(Json.toJson(res)))
  }

  def getCategoryJSON(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val product = categoryRepo.getByIdOption(id)
    product.map(product => product match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def deleteJSON(id: Int): Action[AnyContent] = Action.async { implicit request =>
    categoryRepo.delete(id).map{
    res => Ok(Json.toJson(id))}
  }

  def addJSON(name: String,description: String): Action[AnyContent] = Action.async { implicit request =>
    categoryRepo.create(name,description).map {
      res => Ok(Json.toJson(res))
    }
  }
  def updateJSON(id: Int, name: String,description: String): Action[AnyContent] = Action.async { implicit request =>
    categoryRepo.update(id, new Category(id,name,description)).map {
      res => Ok(Json.toJson(id))
    }
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

case class CreateCategoryForm(name: String, description: String)
case class UpdateCategoryForm(id: Int, name: String, description: String)