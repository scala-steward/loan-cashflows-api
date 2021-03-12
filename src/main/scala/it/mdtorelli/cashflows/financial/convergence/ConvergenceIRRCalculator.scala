package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.IRRCalculator
import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.model.Implicits._

import cats.Monad
import cats.syntax.show._

object ConvergenceIRRCalculator {
  def apply[F[_]: Monad](implicit convergenceF: ConvergenceFunction[F]): IRRCalculator[F] =
    new ConvergenceIRRCalculator

  private def f(c: BigDecimal, x: BigDecimal, n: Int): BigDecimal = c / (1 + x).pow(n)
}
private final class ConvergenceIRRCalculator[F[_]: Monad](implicit convergenceF: ConvergenceFunction[F])
    extends IRRCalculator[F] {
  import ConvergenceIRRCalculator.f

  // 0 = SUM(n: 0, N)(Cn / (1 + x)^n
  override def compute(cashFlow: CashFlow): F[ErrorOr[IRR]] = {
    println(show"Calculating IRR for: $cashFlow")

    def irr(x: BigDecimal): BigDecimal =
      -cashFlow.principal.value + cashFlow.upfrontFee.value + cashFlow.upfrontCreditlineFee.value + cashFlow.schedule.zipWithIndex.map {
        case (s, idx) => f(s.principal.value + s.interestFee.value, x, idx + 1)
      }.sum

    convergenceF.converge(irr).eitherT.map(_.toIRR).value
  }
}
