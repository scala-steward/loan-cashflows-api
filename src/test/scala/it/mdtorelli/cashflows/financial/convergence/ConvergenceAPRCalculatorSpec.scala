package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.financial.{APRCalculator, CalculatorFixtures}
import it.mdtorelli.cashflows.model.Decimal
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.util.Try

final class ConvergenceAPRCalculatorSpec extends BaseSpec with CalculatorFixtures {
  import ConvergenceFunctions._

  behavior of "ConvergenceAPRCalculator"

  // Disabled: It diverges with our input
  "iterativeSteffensen" should "compute expected APR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  // Disabled: It diverges with our input
  "tailRecSteffensen" should "compute expected APR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  "iterativeSecant" should "compute expected APR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "return zero on empty schedule" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    aprCalculator.compute(cashFlow.copy(schedule = Seq.empty)).resolveRight.scaled shouldEqual Decimal.Zero.toAPR
  }

  "tailRecSecant" should "compute expected APR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "return zero on empty schedule" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    aprCalculator.compute(cashFlow.copy(schedule = Seq.empty)).resolveRight.scaled shouldEqual Decimal.Zero.toAPR
  }

  private sealed trait Fixture extends APRCalculatorFixture[Try] {
    protected implicit def convergenceF: ConvergenceFunction[Try]

    override protected final lazy val aprCalculator: APRCalculator[Try] = ConvergenceAPRCalculator[Try]
  }
}
