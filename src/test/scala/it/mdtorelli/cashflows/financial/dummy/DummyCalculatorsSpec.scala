package it.mdtorelli.cashflows.financial.dummy

import cats.Id
import it.mdtorelli.cashflows.financial.{APRCalculator, CalculatorFixture, IRRCalculator}
import it.mdtorelli.cashflows.model.Decimal
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

final class DummyCalculatorsSpec extends BaseSpec {
  behavior of "DummyAPRCalculator"

  it should "compute expected APR successfully" in new Fixture {
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR.scaled
  }

  behavior of "DummyIRRCalculator"

  it should "compute expected IRR successfully" in new Fixture {
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR.scaled
  }

  private sealed trait Fixture extends CalculatorFixture[Id] {
    override protected final val expectedAPR = Decimal(38.345).toAPR
    override protected final val expectedIRR = Decimal(0.0234008783).toIRR

    override protected final lazy val aprCalculator: APRCalculator[Id] = DummyAPRCalculator[Id]
    override protected final lazy val irrCalculator: IRRCalculator[Id] = DummyIRRCalculator[Id]
  }
}
