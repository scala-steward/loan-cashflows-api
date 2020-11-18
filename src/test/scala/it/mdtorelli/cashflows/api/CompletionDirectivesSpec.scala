package it.mdtorelli.cashflows.api

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import cats.instances.try_._
import it.mdtorelli.cashflows.adt.ToFuture.Implicits.tryInstance
import it.mdtorelli.cashflows.adt.{AsyncOperationInterruptedError, AsyncOperationTimedOutError, ErrorOr, GenericError}
import it.mdtorelli.cashflows.api.json.ComputationResultJsonSupport
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model.{ComputationResult, Decimal}
import it.mdtorelli.cashflows.util.BaseSpec
import spray.json._

final class CompletionDirectivesSpec extends BaseSpec with ScalatestRouteTest {
  behavior of "CompletionDirectives"

  "completeF" should "respond with 200 status code and payload on success" in new Fixture {
    override protected final lazy val directive = completeF(ErrorOr.successful(result))

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[ComputationResult] shouldEqual result
    }
  }

  it should "respond with custom status code and payload on success" in new Fixture {
    override protected final lazy val directive =
      completeF(ErrorOr.successful(result), responseStatusCode = StatusCodes.Created)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.Created
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[ComputationResult] shouldEqual result
    }
  }

  it should "respond with 500 error status code and payload on failure" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    override protected final lazy val directive = completeF(ErrorOr.pure(throw ex))

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError(ex.getMessage, cause = Some(ex)).toJson
    }
  }

  it should "respond with 500 error status code and payload on unhandled error" in new Fixture {
    import scala.util.Try

    private final val ex = new RuntimeException("BOOM!")
    private final val resultF = ErrorOr.fromF(cats.MonadError[Try, Throwable].raiseError(ex))
    override protected final lazy val directive = completeF(resultF)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError(ex.getMessage, cause = Some(ex)).toJson
    }
  }

  it should "respond with 500 status code and payload on thrown exception" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    @scala.annotation.nowarn("cat=w-flag-dead-code")
    override protected final lazy val directive = completeF(throw ex)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError(
        "Unhandled error",
        details = Some(ex.getMessage),
        cause = Some(ex)).toJson
    }
  }

  it should "recover the error if defined in recoverWith" in new Fixture {
    private final val resultF = ErrorOr.failed(AsyncOperationInterruptedError())
    override protected final lazy val directive =
      completeF(resultF, recoverWith = { case _: AsyncOperationInterruptedError => result })

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[ComputationResult] shouldEqual result
    }
  }

  it should "not recover the error if not defined in recoverWith" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive =
      completeF(resultF, recoverWith = { case _: GenericError => result })

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "respond with a custom error status code if defined in statusCodeErrorHandler" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive =
      completeF(resultF, statusCodeErrorHandler = { case _: AsyncOperationInterruptedError => StatusCodes.NotFound })

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.NotFound
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "not respond with a custom error status code if not defined in statusCodeErrorHandler" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive =
      completeF(resultF, statusCodeErrorHandler = { case _: GenericError => StatusCodes.NotFound })

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "respond with the 408 error status code for AsyncOperationTimedOutError" in new Fixture {
    private final val error = AsyncOperationTimedOutError()
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive = completeF(resultF)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.RequestTimeout
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "respond with the 500 error status code for AsyncOperationInterruptedError" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive = completeF(resultF)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "respond with the 500 error status code for GenericError" in new Fixture {
    private final val error = GenericError("BOOM!")
    private final val resultF = ErrorOr.failed(error)
    override protected final lazy val directive = completeF(resultF)

    Get("/") ~> route ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  private sealed trait Fixture
      extends Directives
      with APIServerHandlers
      with ComputationResultJsonSupport
      with CompletionDirectives {
    protected def directive: Route

    protected final val result = ComputationResult(Decimal.Zero.toAPR, Decimal.Zero.toIRR)

    protected final lazy val route = Route.seal {
      pathSingleSlash {
        get(directive)
      }
    }
  }
}
