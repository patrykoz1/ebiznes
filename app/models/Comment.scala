package models

import play.api.libs.json.Json

case class Comment(id: Int, body:String,productId:Int,customerId:Int)

object Comment {
  implicit val categoryFormat = Json.format[Comment]
}

