package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.financial.{APRCalculator, CalculatorFixture, IRRCalculator}
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.util.Try

final class ConvergenceCalculatorsSpec extends BaseSpec {
  import ConvergenceFunctions._

  behavior of "ConvergenceAPRCalculator"

  // Disabled: It diverges with our input
  ignore should "compute expected APR successfully using iterative Steffensen" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  // Disabled: It diverges with our input
  ignore should "compute expected APR successfully using tail-recursive Steffensen" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "compute expected APR successfully using iterative Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "compute expected APR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  behavior of "ConvergenceIRRCalculator"

  // Disabled: It diverges with our input
  ignore should "compute expected IRR successfully using iterative Steffensen" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  // Disabled: It diverges with our input
  ignore should "compute expected IRR successfully using tail-recursive Steffensen" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  it should "compute expected IRR successfully using iterative Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  it should "compute expected IRR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  private sealed trait Fixture extends CalculatorFixture[Try] {
    protected implicit def convergenceF: ConvergenceFunction[Try]

    override protected final lazy val aprCalculator: APRCalculator[Try] = ConvergenceAPRCalculator[Try]
    override protected final lazy val irrCalculator: IRRCalculator[Try] = ConvergenceIRRCalculator[Try]
  }
}
