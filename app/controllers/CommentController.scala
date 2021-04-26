package controllers

import play.api.mvc.{AbstractController, ControllerComponents}

import javax.inject.Inject

class CommentController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


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
