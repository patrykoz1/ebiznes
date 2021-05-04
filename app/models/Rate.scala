package models

import play.api.libs.json.Json

case class Rate(id: Int,productId:Int,customerId:Int,grade:Int)

object Rate {
  implicit val categoryFormat = Json.format[Rate]
}