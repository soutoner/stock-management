package models

import java.sql.ResultSet
import java.time.LocalDateTime

import play.api.libs.json._

case class Product(id: Int, name: String, stock: Int, createdAt: LocalDateTime)

object Product {

  val ReservationDays = 2

  implicit val productWrites = Json.writes[Product]

  def parse(rs: ResultSet, stock: Int = 0): Product = {
    Product(
      rs.getInt("id"),
      rs.getString("name"),
      stock,
      rs.getTimestamp("created_at").toLocalDateTime
    )
  }
}
