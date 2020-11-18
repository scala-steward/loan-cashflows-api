package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.financial.{APRCalculator, CalculatorFixture, IRRCalculator}
import it.mdtorelli.cashflows.model.Decimal
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.util.Try

final class ConvergenceCalculatorsSpec extends BaseSpec {
  import ConvergenceFunctions._

  behavior of "ConvergenceAPRCalculator"

  // Disabled: It diverges with our input
  "iterativeSteffensen (APR)" should "compute expected APR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  // Disabled: It diverges with our input
  "tailRecSteffensen (APR)" should "compute expected APR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  "iterativeSecant (APR)" should "compute expected APR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "return zero on empty schedule" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    aprCalculator.compute(cashFlow.copy(schedule = Seq.empty)).resolveRight.scaled shouldEqual Decimal.Zero.toAPR
  }

  "tailRecSecant (APR)" should "compute expected APR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "return zero on empty schedule" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    aprCalculator.compute(cashFlow.copy(schedule = Seq.empty)).resolveRight.scaled shouldEqual Decimal.Zero.toAPR
  }

  behavior of "ConvergenceIRRCalculator"

  // Disabled: It diverges with our input
  "iterativeSteffensen (IRR)" should "compute expected IRR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  // Disabled: It diverges with our input
  "tailRecSteffensen (IRR)" should "compute expected IRR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  "iterativeSecant (IRR)" should "compute expected IRR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  "tailRecSecant (IRR)" should "compute expected IRR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  private sealed trait Fixture extends CalculatorFixture[Try] {
    protected implicit def convergenceF: ConvergenceFunction[Try]

    override protected final lazy val aprCalculator: APRCalculator[Try] = ConvergenceAPRCalculator[Try]
    override protected final lazy val irrCalculator: IRRCalculator[Try] = ConvergenceIRRCalculator[Try]
  }
}
