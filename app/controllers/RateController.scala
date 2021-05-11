package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, number}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}
import repositories.{RateRepository}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class RateController @Inject()(rateRepo:RateRepository, cc: MessagesControllerComponents)
                      (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {


  val rateForm: Form[CreateRateForm] = Form {
    mapping(
      "productId" -> number,
      "customerId" ->number,
      "grade"->number,
    )(CreateRateForm.apply)(CreateRateForm.unapply)
  }

  val updateRateForm: Form[UpdateRateForm] = Form {
    mapping(
      "id" -> number,
      "productId" -> number,
      "customerId" ->number,
      "grade"->number,
    )(UpdateRateForm.apply)(UpdateRateForm.unapply)
  }

  def getRates: Action[AnyContent] = Action.async { implicit request =>
    val rates = rateRepo.list()
    rates.map( comments => Ok(views.html.rates(rates)))
  }

  def getRate(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val rate = rateRepo.getById(id)
    rate.map(rate => rate match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    rateRepo.delete(id)
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
case class CreateRateForm(productId:Int,customerId:Int,grade:Int)
case class UpdateRateForm(id: Int,productId:Int,customerId:Int,grade:Int)
