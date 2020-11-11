package it.mdtorelli.cashflows.api.route

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import cats.syntax.option._
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.adt._
import it.mdtorelli.cashflows.api.APIServerHandlers
import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.financial.dummy.{DummyAPRCalculator, DummyIRRCalculator}
import it.mdtorelli.cashflows.financial.{APRCalculator, IRRCalculator}
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.services.CashFlowService
import it.mdtorelli.cashflows.util.{BaseSpec, ResourceLoader}
import spray.json._

import scala.concurrent.Future

final class CashFlowRouteSpec extends BaseSpec with ScalatestRouteTest {
  behavior of "CashFlowRoute"

  "POST /cashflows" should "return the computed APR and IRR" in new Fixture {
    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.OK
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[ComputationResult] shouldEqual expectedComputationResult
    }
  }

  it should "return an error in case of APR calculator timeout" in new Fixture {
    private final val error = AsyncOperationTimedOutError()
    override protected final lazy val aprCalculator = new APRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.RequestTimeout
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of IRR calculator timeout" in new Fixture {
    private final val error = AsyncOperationTimedOutError()
    override protected final lazy val irrCalculator = new IRRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.RequestTimeout
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of APR calculator interrupt" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    override protected final lazy val aprCalculator = new APRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of IRR calculator interrupt" in new Fixture {
    private final val error = AsyncOperationInterruptedError()
    override protected final lazy val irrCalculator = new IRRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of APR calculator generic error" in new Fixture {
    private final val error = GenericError("BOOM!")
    override protected final lazy val aprCalculator = new APRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of IRR calculator generic error" in new Fixture {
    private final val error = GenericError("BOOM!")
    override protected final lazy val irrCalculator = new IRRCalculatorAdapter(error.leftFuture)

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual error.toJson
    }
  }

  it should "return an error in case of APR calculator throws an exception" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    override protected final lazy val aprCalculator = new APRCalculatorAdapter(ErrorOr.async(throw ex))

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError(ex.getMessage).toJson
    }
  }

  it should "return an error in case of IRR calculator throws an exception" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    override protected final lazy val irrCalculator = new IRRCalculatorAdapter(ErrorOr.async(throw ex))

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError(ex.getMessage).toJson
    }
  }

  it should "return an error in case of APR calculator future fails" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    override protected final lazy val aprCalculator = new APRCalculatorAdapter(Future.failed(ex))

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError("Unhandled error", ex.getMessage.some).toJson
    }
  }

  it should "return an error in case of IRR calculator future fails" in new Fixture {
    private final val ex = new RuntimeException("BOOM!")
    override protected final lazy val irrCalculator = new IRRCalculatorAdapter(Future.failed(ex))

    Post("/cashflows", cashFlow) ~> cashFlowRoute ~> check {
      status shouldEqual StatusCodes.InternalServerError
      contentType shouldEqual ContentTypes.`application/json`
      responseAs[JsValue] shouldEqual GenericError("Unhandled error", ex.getMessage.some).toJson
    }
  }

  private sealed trait Fixture
      extends ResourceLoader
      with CashFlowJsonSupport
      with ComputationResultJsonSupport
      with APIServerHandlers {
    protected def aprCalculator: APRCalculator = DummyAPRCalculator
    protected def irrCalculator: IRRCalculator = DummyIRRCalculator

    protected final val cashFlowRoute = Route.seal(CashFlowRoute(CashFlowService(aprCalculator, irrCalculator)))

    protected final val expectedComputationResult = ComputationResult(Decimal(38.3).toAPR, Decimal(0.0234008783).toIRR)
    protected final lazy val cashFlow = parseJsonResource("cashflow.json").convertTo[CashFlow]
  }

  private final class APRCalculatorAdapter(x: => AsyncErrorOr[APR]) extends APRCalculator {
    override def compute(cashFlow: CashFlow): AsyncErrorOr[APR] = x
  }
  private final class IRRCalculatorAdapter(x: => AsyncErrorOr[IRR]) extends IRRCalculator {
    override def compute(cashFlow: CashFlow): AsyncErrorOr[IRR] = x
  }
}
