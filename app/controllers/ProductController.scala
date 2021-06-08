package controllers

import javax.inject._
import models.{Category, Product}
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import repositories.{CategoryRepository, ProductRepository}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ProductController @Inject()(productsRepo:ProductRepository,categoryRepo:CategoryRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "categoryId" -> number,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "categoryId" -> number,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }

  def getProducts: Action[AnyContent] = Action.async { implicit request =>
    val products = productsRepo.list()
    products.map( products => Ok(views.html.products(products)))
  }

  def getProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val product = productsRepo.getByIdOption(id)
    product.map(product => product match {
      case Some(p) => Ok(views.html.products(Seq(p)))
      case None => Ok(views.html.products(Seq()))
  })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    productsRepo.delete(id)
    Redirect("/products")
  }

  def getProductsJson: Action[AnyContent] = Action.async { implicit request =>
    val products = productsRepo.list()
    products.map( products => Ok(Json.toJson(products)))
  }

  def getProductJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val product = productsRepo.getByIdOption(id)
    product.map(product => product match {
      case Some(p) => Ok(Json.toJson(product))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def deleteJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    productsRepo.delete(id).map{
      res => Ok(Json.toJson(id))}
  }

  def updateJson(id: Int, name: String, description: String, categoryId: Int): Unit ={
    productsRepo.update(id, new Product(id,name,description,categoryId)).map {
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

case class CreateProductForm(name: String, description: String, categoryId: Int)
case class UpdateProductForm(id: Int, name: String, description: String, categoryId: Int)