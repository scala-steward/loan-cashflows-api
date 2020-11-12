package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.financial.{APRCalculator, CalculatorFixture, IRRCalculator}
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

final class ConvergenceCalculatorsSpec extends BaseSpec {
  import ConvergenceFunctions._

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(2.seconds)

  behavior of "ConvergenceAPRCalculator"

  // Disabled: It diverges with our input
  ignore should "compute expected APR successfully using iterative Steffensen" in new Fixture {
    override protected implicit final val convergenceF = iterativeSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  // Disabled: It diverges with our input
  ignore should "compute expected APR successfully using tail-recursive Steffensen" in new Fixture {
    override protected implicit final val convergenceF = tailRecSteffensen()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "compute expected APR successfully using iterative Secant" in new Fixture {
    override protected implicit final val convergenceF = iterativeSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  it should "compute expected APR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF = tailRecSecant()
    aprCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedAPR
  }

  behavior of "ConvergenceIRRCalculator"

  // Disabled: It diverges with our input
  ignore should "compute expected IRR successfully using iterative Steffensen" in new Fixture {
    override protected implicit final val convergenceF = iterativeSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  // Disabled: It diverges with our input
  ignore should "compute expected IRR successfully using tail-recursive Steffensen" in new Fixture {
    override protected implicit final val convergenceF = tailRecSteffensen()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  it should "compute expected IRR successfully using iterative Secant" in new Fixture {
    override protected implicit final val convergenceF = iterativeSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  it should "compute expected IRR successfully using tail-recursive Secant" in new Fixture {
    override protected implicit final val convergenceF = tailRecSecant()
    irrCalculator.compute(cashFlow).resolveRight.scaled shouldEqual expectedIRR
  }

  private sealed trait Fixture extends CalculatorFixture {
    protected implicit def convergenceF: ConvergenceFunction

    override protected final lazy val aprCalculator: APRCalculator = ConvergenceAPRCalculator.apply
    override protected final lazy val irrCalculator: IRRCalculator = ConvergenceIRRCalculator.apply
  }
}
