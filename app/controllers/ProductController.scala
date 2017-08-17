package controllers

import com.google.inject.Inject
import daos.ProductDao
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import models.Product._

class ProductController @Inject()(productDao: ProductDao, cc: ControllerComponents) extends AbstractController(cc) {

  def create(): Action[JsValue] = Action(parse.json) { request: Request[JsValue] =>
    val name = (request.body \ "name").as[String]

    productDao.create(name) match {
      case 0 => Ok("Oops, something went wrong")
      case id => Ok(s"""Product id: $id name: $name created successfully""")
    }
  }

  def show(id: Int) = Action {
    productDao.get(id) match {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound(s"""Product id: $id not found""")
    }
  }

  def refill(id: Int) = Action {
    if (productDao.refill(id)) {
      Ok(s"""Product id: $id successfully refilled""")
    } else {
      Ok(s"""Error while refilling product id: $id""")
    }
  }

  def buy(id: Int) = Action {
    val stockId = productDao.stockId(id)

    if (stockId == 0) {
      NotFound("Not enough stock")
    } else {
      productDao.buy(stockId) match {
        case 0 => Ok(s"""Error while buying product id: $id""")
        case saleId => Ok(s"""Product id: $id successfully bought. Your sale identifier is: $saleId""")
      }
    }
  }

  def reserve(id: Int) = Action {
    val stockId = productDao.stockId(id)

    if (stockId == 0) {
      NotFound("Not enough stock")
    } else {
      if (productDao.reserve(stockId)) {
        Ok(s"""Product id: $id successfully reserved. Your reservation identifier is: $stockId""")
      } else {
        Ok(s"""Error while reserving product id: $id""")
      }
    }
  }
}
