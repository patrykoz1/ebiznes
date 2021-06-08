package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.ShippingRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class ShippingController @Inject()(shippingRepo:ShippingRepository, cc: MessagesControllerComponents)
                                  (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val shippingForm: Form[CreateShippingForm] = Form {
    mapping(
      "orderId" -> number,
      "type0f" -> number,

    )(CreateShippingForm.apply)(CreateShippingForm.unapply)
  }

  val updateShippingForm: Form[UpdateShippingForm] = Form {
    mapping(
      "id" -> number,
      "orderId" -> number,
      "type0f" -> number,
    )(UpdateShippingForm.apply)(UpdateShippingForm.unapply)
  }

  def getShippings: Action[AnyContent] = Action.async {
    implicit request =>
    val shippings = shippingRepo.list()
    shippings.map( shippings => Ok(views.html.shippings(shippings)))
  }

  def getShipping(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val shipping = shippingRepo.getByIdOption(id)
    shipping.map(shipping => shipping match {
      case Some(p) => Ok(views.html.shippings(Seq(p)))
      case None => Ok(views.html.shippings(Seq()))
    })
  }

  def delete(id: Int): Action[AnyContent] = Action.async {
    shippingRepo.delete(id).map{
      res => Ok(views.html.shippings(Seq()))
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
case class CreateShippingForm(orderId:Int,typeOf:Int)
case class UpdateShippingForm(id: Int,orderId:Int,typeOf:Int)
