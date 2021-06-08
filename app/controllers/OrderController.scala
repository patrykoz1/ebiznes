package controllers

import models.Order
import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.OrderRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class OrderController @Inject()(orderRepo:OrderRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "productId" -> number,
      "customerId" -> number,

    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> number,
      "productId" -> number,
      "customerId" -> number,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  def getOrders()= Action.async { implicit request =>
      orderRepo.list().map{
      res => Ok(views.html.orders(res))}
  }

  def getOrder(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val comment = orderRepo.getByIdOption(id)
    comment.map(comment => comment match {
     // case Some(p) => Ok(views.html.index(p)
      case None => Redirect(routes.HomeController.index)
    })
  }

  /*def delete(id: Int): Action[AnyContent] = Action { implicit request =>
    orderRepo.delete(id);
    //Redirect("/c")
  }*/
  def getOrderJson: Action[AnyContent] = Action.async { implicit request =>
    val orders = orderRepo.list()
    orders.map( orders => Ok(Json.toJson(orders)))
  }

  def getOrderJson(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val order = orderRepo.getByIdOption(id)
    order.map(order => order match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def deleteJson(id: Int): Action[AnyContent] = Action.async {
    orderRepo.delete(id).map{
      res => Ok(Json.toJson(id))
    }

  }

  def updateJSON(id: Int,productId:Int,customerId:Int): Action[AnyContent] = Action.async { implicit request =>
    orderRepo.update(id, new Order(id,productId,customerId)).map {
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
case class CreateOrderForm(productId:Int,customerId:Int)
case class UpdateOrderForm(id: Int,productId:Int,customerId:Int)