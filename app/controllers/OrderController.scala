package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{OrderRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class OrderController @Inject()(orderRepo:OrderRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "productId" -> number,
      "costumerId" -> number,

    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> number,
      "productId" -> number,
      "costumerId" -> number,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }

  def getOrder: Action[AnyContent] = Action.async { implicit request =>
    val ords = orderRepo.list()
    ords.map( comments => Ok(views.html.orders(ords)))
  }

  def getOrder(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val comment = orderRepo.getById(id)
    comment.map(comment => comment match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    orderRepo.delete(id)
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
case class CreateOrderForm(orderId:Int,productId:Int,customerId:Int)
case class UpdateOrderForm(id: Int,orderId:Int,customerId:Int,typeOf:Int)