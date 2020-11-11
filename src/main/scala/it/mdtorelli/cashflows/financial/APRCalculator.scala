package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.model.{APR, CashFlow}

trait APRCalculator {
  def compute(cashFlow: CashFlow): AsyncErrorOr[APR]
}
