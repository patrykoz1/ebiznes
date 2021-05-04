package models

import play.api.libs.json.Json

case class Purchase(id: Int,orderId:Int,customerId:Int)

object Purchase {
  implicit val categoryFormat = Json.format[Purchase]
}