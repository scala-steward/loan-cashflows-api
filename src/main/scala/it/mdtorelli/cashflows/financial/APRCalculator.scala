package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.model.{APR, CashFlow}

trait APRCalculator[F[_]] {
  def compute(cashFlow: CashFlow): F[ErrorOr[APR]]
}
