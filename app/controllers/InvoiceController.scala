package controllers

import models.Invoice
import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.{CommentRepository, InvoiceRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class InvoiceController @Inject()(invoiceRepo:InvoiceRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val invoiceForm: Form[CreateInvoiceForm] = Form {
    mapping(
      "orderId" -> number,
      "costumerId" -> number,
      "type0f" -> number,

    )(CreateInvoiceForm.apply)(CreateInvoiceForm.unapply)
  }

  val updateInvoiceForm: Form[UpdateInvoiceForm] = Form {
    mapping(
      "id" -> number,
      "orderId" -> number,
      "costumerId" -> number,
      "type0f" -> number,
    )(UpdateInvoiceForm.apply)(UpdateInvoiceForm.unapply)
  }

  def getInvoicesJSON: Action[AnyContent] = Action.async { implicit request =>
    val invoices = invoiceRepo.list()
    invoices.map( invoices => Ok(Json.toJson(invoices)))
  }

  def getInvoiceJSON(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val invoice = invoiceRepo.getByIdOption(id)
    invoice.map(invoice => invoice match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def deleteJSON(id: Int): Action[AnyContent] = Action.async {
    invoiceRepo.delete(id).map{
      res => Ok(Json.toJson(id));
    }
  }
  def addJSON(orderId:Int,customerId:Int,typeOf:Int): Action[AnyContent] = Action.async { implicit request =>
    invoiceRepo.create(orderId,customerId, typeOf).map {
      res => Ok(Json.toJson(res))
    }
  }
  def updateJSON(id: Int, orderId:Int,customerId:Int,typeOf:Int): Action[AnyContent] = Action.async { implicit request =>
    invoiceRepo.update(id, new Invoice(id,orderId,customerId, typeOf)).map {
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
case class CreateInvoiceForm(orderId:Int,customerId:Int,typeOf:Int)
case class UpdateInvoiceForm(id: Int,orderId:Int,customerId:Int,typeOf:Int)