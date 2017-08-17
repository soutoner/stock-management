package daos

import java.sql.{Connection, Statement}

import com.google.inject.Inject
import play.api.db.Database
import models.Product

class ProductDao @Inject()(db: Database) {

  def create(name: String): Int = {
    val sql = "INSERT INTO product (name) VALUES (?)"

    db.withTransaction { conn =>
      val st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

      st.setString(1, name)

      st.executeUpdate()

      val rs = st.getGeneratedKeys
      if (rs.next()) {
        rs.getInt(1)
      } else {
        0
      }
    }
  }

  def get(productId: Int): Option[Product] = {
    val sql = "SELECT id, name, created_at " +
      "FROM product " +
      "WHERE id = ?"

    db.withTransaction { implicit conn =>
      val st = conn.prepareStatement(sql)

      st.setInt(1, productId)

      val rs = st.executeQuery()

      if (rs.next()) {
        Some(Product.parse(rs, stock(productId)))
      } else {
        None
      }
    }
  }

  def refill(productId: Int): Boolean = {
    val sql = "INSERT INTO stock (product_id) VALUES (?)"

    db.withTransaction { conn =>
      val st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

      st.setInt(1, productId)

      st.executeUpdate != 0
    }
  }

  def buy(stockId: Int): Int = {
    val sql = "INSERT INTO sale (stock_id) VALUES (?)"

    db.withTransaction { conn =>
      val st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

      st.setInt(1, stockId)

      st.executeUpdate()

      val rs = st.getGeneratedKeys
      if (rs.next()) {
        rs.getInt(1)
      } else {
        0
      }
    }
  }

  def stockId(productId: Int): Int = {
    val sql = "SELECT id " +
      "FROM stock " +
      "WHERE product_id = ? " +
      "AND id NOT IN (SELECT stock_id FROM sale) " +
      "ORDER BY id ASC " +
      "LIMIT 1"

    db.withTransaction { conn =>
      val st = conn.prepareStatement(sql)

      st.setInt(1, productId)

      val rs = st.executeQuery()

      if (rs.next()) {
        rs.getInt(1)
      } else {
        0
      }
    }
  }

  def stock(productId: Int)(implicit conn: Connection): Int = {
    val sql = "SELECT COUNT(id) " +
      "FROM stock " +
      "WHERE " +
      "product_id = ? " +
      "AND id NOT IN (SELECT stock_id FROM sale)"

    val st = conn.prepareStatement(sql)

    st.setInt(1, productId)

    val rs = st.executeQuery()

    if (rs.next()) {
      rs.getInt(1)
    } else {
      0
    }
  }
}
