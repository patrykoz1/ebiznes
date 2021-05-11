package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.PaymentRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class PaymentController @Inject()(paymentRepo:PaymentRepository, cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val invoiceForm: Form[CreatePaymentForm] = Form {
    mapping(

      "type0f" -> number,

    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updateInvoiceForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> number,
      "type0f" -> number,
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def getPayments: Action[AnyContent] = Action.async { implicit request =>
    val payments = paymentRepo.list()
    payments.map( comments => Ok(views.html.payments(payments)))
  }

  def getPayment(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val payment = paymentRepo.getById(id)
    payment.map(payment => payment match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    paymentRepo.delete(id)
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
case class CreatePaymentForm(typeOf:Int)
case class UpdatePaymentForm(id: Int,typeOf:Int)