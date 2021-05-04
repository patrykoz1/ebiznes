package models

import play.api.libs.json.Json

case class Payment(id: Int,typeOf:Int)

object Payment {
  implicit val categoryFormat = Json.format[Payment]
}