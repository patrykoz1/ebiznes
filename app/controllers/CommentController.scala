package controllers

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}
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

  def getComments: Action[AnyContent] = Action.async { implicit request =>
    val comments = commentRepo.list()
    comments.map( comments => Ok(views.html.comments(comments)))
  }

  def getComment(id: Int): Action[AnyContent] = Action.async { implicit request =>
    val comment = commentRepo.getById(id)
    comment.map(comment => comment match {
      case Some(p) => Ok(views.html.index(p))
      case None => Redirect(routes.HomeController.get())
    })
  }

  def delete(id: Int): Action[AnyContent] = Action {
    commentRepo.delete(id)
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
case class CreateCommentForm(name: String, description: String)
case class UpdateCommentForm(id: Int, name: String, description: String)