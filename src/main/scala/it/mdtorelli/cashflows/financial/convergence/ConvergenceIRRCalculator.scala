package it.mdtorelli.cashflows.financial.convergence

import cats.syntax.show._
import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.IRRCalculator
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model._

import scala.concurrent.ExecutionContext

object ConvergenceIRRCalculator {
  def apply(implicit convergenceF: ConvergenceFunction, ec: ExecutionContext): IRRCalculator =
    new ConvergenceIRRCalculator

  private def f(c: BigDecimal, x: BigDecimal, n: Int): BigDecimal = c / (1 + x).pow(n)
}
private final class ConvergenceIRRCalculator(implicit convergenceF: ConvergenceFunction, ec: ExecutionContext)
    extends IRRCalculator {
  import ConvergenceIRRCalculator.f

  // 0 = SUM(n: 0, N)(Cn / (1 + x)^n
  override def compute(cashFlow: CashFlow): AsyncErrorOr[IRR] = {
    println(show"Calculating IRR for: $cashFlow")

    def irr(x: BigDecimal): BigDecimal =
      -cashFlow.principal.value + cashFlow.upfrontFee.value + cashFlow.upfrontCreditlineFee.value + cashFlow.schedule.zipWithIndex.map {
        case (s, idx) => f(s.principal.value + s.interestFee.value, x, idx + 1)
      }.sum

    convergenceF.converge(irr).eitherT.map(_.toIRR).value
  }
}
