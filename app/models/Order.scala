package models

import play.api.libs.json.Json

case class Order(id: Int,productId:Int,customerId:Int)

object Order {
  implicit val categoryFormat = Json.format[Order]
}
