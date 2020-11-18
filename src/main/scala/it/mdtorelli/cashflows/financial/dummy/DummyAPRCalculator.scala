package it.mdtorelli.cashflows.financial.dummy

import cats.Monad
import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.APRCalculator
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model.{APR, CashFlow, Decimal}

object DummyAPRCalculator {
  def apply[F[_]: Monad]: DummyAPRCalculator[F] = new DummyAPRCalculator
}
final class DummyAPRCalculator[F[_]: Monad] private extends APRCalculator[F] {
  override def compute(cashFlow: CashFlow): F[ErrorOr[APR]] = Decimal(38.345).toAPR.rightF
}
