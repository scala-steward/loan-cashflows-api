package it.mdtorelli.cashflows.financial.dummy

import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.IRRCalculator
import it.mdtorelli.cashflows.model.{CashFlow, Decimal, IRR}
import it.mdtorelli.cashflows.model.Implicits._

import cats.Monad

object DummyIRRCalculator {
  def apply[F[_]: Monad]: DummyIRRCalculator[F] = new DummyIRRCalculator
}
final class DummyIRRCalculator[F[_]: Monad] private extends IRRCalculator[F] {
  override def compute(cashFlow: CashFlow): F[ErrorOr[IRR]] = Decimal(0.023400878344589).toIRR.rightF
}
