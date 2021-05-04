package models

import play.api.libs.json.Json

case class Invoice(id: Int,orderId:Int,customerId:Int,typeOf:Int)

object Invoice {
  implicit val categoryFormat = Json.format[Invoice]
}
