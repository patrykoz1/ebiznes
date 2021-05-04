package models

import play.api.libs.json.Json

case class User(id: Int,firstName:String,secondName:String, email:String,password:String)

object User {
  implicit val categoryFormat = Json.format[User]
}
