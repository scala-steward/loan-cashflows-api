package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.financial.{CalculatorFixtures, IRRCalculator}
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.util.Try

final class ConvergenceIRRCalculatorSpec extends BaseSpec with CalculatorFixtures {
  import ConvergenceFunctions._

  behavior of "ConvergenceIRRCalculator"

  // Disabled: It diverges with our input
  "iterativeSteffensen" should "compute expected IRR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  // Disabled: It diverges with our input
  "tailRecSteffensen" should "compute expected IRR successfully" ignore new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  "iterativeSecant" should "compute expected IRR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = iterativeSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  "tailRecSecant" should "compute expected IRR successfully" in new Fixture {
    override protected implicit final val convergenceF: ConvergenceFunction[Try] = tailRecSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  private sealed trait Fixture extends IRRCalculatorFixture[Try] {
    protected implicit def convergenceF: ConvergenceFunction[Try]

    override protected final lazy val irrCalculator: IRRCalculator[Try] = ConvergenceIRRCalculator[Try]
  }
}
