package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.APRCalculator
import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.model.Implicits._

import cats.Monad
import cats.syntax.show._

import java.time.LocalDate

object ConvergenceAPRCalculator {
  def apply[F[_]: Monad](implicit convergenceF: ConvergenceFunction[F]): APRCalculator[F] =
    new ConvergenceAPRCalculator

  private def f(a: BigDecimal, x: BigDecimal, tn: BigDecimal): BigDecimal =
    a * Math.pow((1 + x / 100).toDouble, -tn.toDouble)

  private def yearFractionFrom(d1: LocalDate)(d2: LocalDate): BigDecimal =
    (java.time.temporal.ChronoUnit.DAYS.between(d1, d2) + 0.41666d) / (365d + 1d / 4d)
}
private final class ConvergenceAPRCalculator[F[_]: Monad](implicit convergenceF: ConvergenceFunction[F])
    extends APRCalculator[F] {
  import ConvergenceAPRCalculator.{f, yearFractionFrom}

  // 0 = - S + A + SUM(n: 1, N)(An * (1 + x / 100)^-tn)
  override def compute(cashFlow: CashFlow): F[ErrorOr[APR]] = {
    println(show"Calculating APR for: $cashFlow")

    lazy val startingDate = cashFlow.schedule.head.date.minusDays(30)
    def t(date: LocalDate): BigDecimal = yearFractionFrom(startingDate)(date)

    def apr(x: BigDecimal): BigDecimal =
      -cashFlow.principal.value + cashFlow.upfrontFee.value + cashFlow.upfrontCreditlineFee.value +
        cashFlow.schedule.map { s =>
          f(s.principal.value + s.interestFee.value, x, t(s.date))
        }.sum

    (
      if (cashFlow.schedule.isEmpty) Decimal.Zero.rightT[F]
      else convergenceF.converge(apr).eitherT
    ).map(_.toAPR).value
  }
}
