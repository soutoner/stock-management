package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

class ProductControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "ProductController GET" should {

    "show on existent product, returns it with stock" in {
      val request = FakeRequest(GET, "/product/1")
      val show = route(app, request).get

      status(show) mustBe OK
      contentType(show) mustBe Some("application/json")
      contentAsString(show) must include ("stock")
    }

    "show on non-existent product, returns 404" in {
      val request = FakeRequest(GET, "/product/0")
      val show = route(app, request).get

      status(show) mustBe NOT_FOUND
      contentType(show) mustBe Some("text/plain")
      contentAsString(show) must include ("Product id: 0 not found")
    }
  }

  "ProductController POST" should {

    "create with name, saves new product" in {
      val body = Json.parse("""
          |{
          |  "name": "TV"
          |}
        """.stripMargin
      )
      val request = FakeRequest(POST, "/product").withJsonBody(body)
      val create = route(app, request).get

      status(create) mustBe OK
      contentType(create) mustBe Some("text/plain")
      contentAsString(create) must include ("TV created successfully")
    }

    "refill, increase by one product stock" in {
      val showRequest = FakeRequest(GET, "/product/1")
      val show = route(app, showRequest).get
      contentAsString(show) must include (""""stock":3""")

      val refillRequest = FakeRequest(POST, "/product/1/refill")
      val refill = route(app, refillRequest).get
      status(refill) mustBe OK
      contentType(refill) mustBe Some("text/plain")
      contentAsString(refill) must include ("Product id: 1 successfully refilled")

      val showRequestAfter = FakeRequest(GET, "/product/1")
      val showAfter = route(app, showRequestAfter).get
      contentAsString(showAfter) must include (""""stock":4""")
    }

    "buy, decrease by one product stock" in {
      val showRequest = FakeRequest(GET, "/product/1")
      val show = route(app, showRequest).get
      contentAsString(show) must include (""""stock":3""")

      val refillRequest = FakeRequest(POST, "/product/1/buy")
      val refill = route(app, refillRequest).get
      status(refill) mustBe OK
      contentType(refill) mustBe Some("text/plain")

      val showRequestAfter = FakeRequest(GET, "/product/1")
      val showAfter = route(app, showRequestAfter).get
      contentAsString(showAfter) must include (""""stock":2""")
    }

    "reserve, decrease by one product stock" in {
      val showRequest = FakeRequest(GET, "/product/1")
      val show = route(app, showRequest).get
      contentAsString(show) must include (""""stock":3""")

      val refillRequest = FakeRequest(POST, "/product/1/reserve")
      val refill = route(app, refillRequest).get
      status(refill) mustBe OK
      contentType(refill) mustBe Some("text/plain")

      val showRequestAfter = FakeRequest(GET, "/product/1")
      val showAfter = route(app, showRequestAfter).get
      contentAsString(showAfter) must include (""""stock":2""")
    }
  }
}
