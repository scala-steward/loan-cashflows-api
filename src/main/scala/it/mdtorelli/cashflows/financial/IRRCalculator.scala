package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.model.{CashFlow, IRR}

trait IRRCalculator[F[_]] {
  def compute(cashFlow: CashFlow): F[ErrorOr[IRR]]
}
