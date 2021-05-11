package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{CategoryRepository, ProductRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ProductController @Inject()(productsRepo:ProductRepository,categoryRepo:CategoryRepository,cc: MessagesControllerComponents)
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

  def getCategories: Action[AnyContent] = Action.async { implicit request =>
    val products = categoryRepo.list()
    products.map( products => Ok(views.html.categories(products)))
  }

  def getCategory(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val product = categoryRepo.getByIdOption(id)
    product.map(product => product match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    productsRepo.delete(id)
    Redirect("/categories")
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