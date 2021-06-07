package controllers

import models.Comment
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, MessagesAbstractController, MessagesControllerComponents}
import repositories.CommentRepository

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class CommentController @Inject()(commentRepo:CommentRepository,cc: MessagesControllerComponents)
                                 (implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val commentForm: Form[CreateCommentForm] = Form {
    mapping(
      "body" -> nonEmptyText,
      "costumerId" -> number,
      "clientId" -> number,

    )(CreateCommentForm.apply)(CreateCommentForm.unapply)
  }

  val updateCommentForm: Form[UpdateCommentForm] = Form {
    mapping(
      "id" -> number,
      "body" -> nonEmptyText,
      "costumerId" -> number,
      "clientId" -> number,
    )(UpdateCommentForm.apply)(UpdateCommentForm.unapply)
  }

  def getCommentsJSON: Action[AnyContent] = Action.async { implicit request =>
    val comments = commentRepo.list()
    comments.map( comments => Ok(Json.toJson(comments)))
  }

  def getCommentJSON(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val comment = commentRepo.getByIdOption(id)
    comment.map(comment => comment match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.HomeController.index)
    })
  }

  def deleteJSON(id: Int): Action[AnyContent] = Action.async {
    commentRepo.delete(id).map{
      res => Ok(Json.toJson(id))}

  }

  def addJSON(body: String,productId:Int,customerId:Int): Action[AnyContent] = Action.async { implicit request =>
    commentRepo.create(body,productId,customerId).map {
      res => Ok(Json.toJson(res))
    }
  }
  def updateJSON(id: Int, body: String,productId:Int,customerId:Int): Action[AnyContent] = Action.async { implicit request =>
    commentRepo.update(id, new Comment(id,body,productId,customerId)).map {
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
case class CreateCommentForm(body: String, costumerId: Int,clientId:Int)
case class UpdateCommentForm(id: Int, body: String, costumerId: Int,clientId:Int)