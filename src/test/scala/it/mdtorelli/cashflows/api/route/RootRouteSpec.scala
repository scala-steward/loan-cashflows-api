package it.mdtorelli.cashflows.api.route

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import it.mdtorelli.cashflows.adt.GenericError
import it.mdtorelli.cashflows.api.APIServerHandlers
import it.mdtorelli.cashflows.api.json.JsonSupport
import it.mdtorelli.cashflows.util.BaseSpec
import spray.json._

final class RootRouteSpec extends BaseSpec with ScalatestRouteTest {
  behavior of "RootRoute"

  it should "say welcome" in new Fixture {
    Get("/") ~> rootRoute ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`text/plain(UTF-8)`
      responseAs[String] shouldEqual "Welcome"
    }
  }

  it should "transform rejections to JSON" in new Fixture with JsonSupport with APIServerHandlers {
    Get("/foo") ~> rootRoute ~> check {
      status shouldEqual StatusCodes.NotFound
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError("The requested resource could not be found.").toJson
    }
  }

  private sealed trait Fixture extends APIServerHandlers {
    protected final val rootRoute = Route.seal(RootRoute.apply)
  }
}
