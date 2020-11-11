package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.model.{CashFlow, IRR}

trait IRRCalculator {
  def compute(cashFlow: CashFlow): AsyncErrorOr[IRR]
}
