package it.mdtorelli.cashflows.api.json

import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.util.{BaseSpec, ResourceLoader}

final class JsonSupportSpec extends BaseSpec {
  behavior of "JsonSupport"

  it should "parse a Decimal" in new Fixture with JsonSupport {
    "Decimal(55555.55).value" should compile
    parseJson(decimalRaw).convertTo[Decimal] shouldEqual Decimal(55555.55)
  }

  behavior of "CashFlowJsonSupport"

  it should "parse a LocalDate" in new Fixture with CashFlowJsonSupport {
    parseJson(localDateRaw).convertTo[java.time.LocalDate] shouldEqual java.time.LocalDate.of(2019, 11, 30)
  }

  it should "parse an Id" in new Fixture with CashFlowJsonSupport {
    "Id(55555).value" should compile
    parseJson(numericRaw).convertTo[Id] shouldEqual Id(55555)
  }

  it should "parse an Schedule" in new Fixture with CashFlowJsonSupport {
    parseJson(scheduleRaw).convertTo[Schedule]
  }

  it should "parse a full CashFlow json request body" in new Fixture with CashFlowJsonSupport {
    cashFlowRaw should not be empty
    parseJson(cashFlowRaw).convertTo[CashFlow]
  }

  behavior of "ComputationResultJsonSupport"

  it should "parse a full ComputationResult json request body" in new Fixture with ComputationResultJsonSupport {
    computationResultRaw should not be empty
    parseJson(computationResultRaw).convertTo[ComputationResult]
  }

  private sealed trait Fixture extends ResourceLoader {
    protected final val numericRaw = "55555"
    protected final val decimalRaw = "55555.55"
    protected final val localDateRaw = """"2019-11-30""""
    protected final val scheduleRaw =
      s"""| {
          |   "id": $numericRaw,
          |   "date": $localDateRaw,
          |   "principal": $decimalRaw,
          |   "interestFee": $decimalRaw
          | }""".stripMargin
    protected final lazy val cashFlowRaw = loadResource("cashflow.json")
    protected final lazy val computationResultRaw = loadResource("computation-result.json")
  }
}
