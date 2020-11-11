package it.mdtorelli.cashflows.financial.dummy

import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.IRRCalculator
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model.{CashFlow, Decimal, IRR}

object DummyIRRCalculator extends IRRCalculator {
  override final def compute(cashFlow: CashFlow): AsyncErrorOr[IRR] =
    Decimal(0.023400878344589).toIRR.right.eitherT.value
}
