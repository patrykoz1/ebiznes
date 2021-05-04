package models

import play.api.libs.json.Json

case class Shipping(id: Int, orderId:Int,typeOf:Int)

object Shipping {
  implicit val categoryFormat = Json.format[Shipping]
}