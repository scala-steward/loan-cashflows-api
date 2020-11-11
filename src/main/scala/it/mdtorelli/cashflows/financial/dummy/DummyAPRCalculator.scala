package it.mdtorelli.cashflows.financial.dummy

import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.APRCalculator
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.model.{APR, CashFlow, Decimal}

object DummyAPRCalculator extends APRCalculator {
  override final def compute(cashFlow: CashFlow): AsyncErrorOr[APR] =
    Decimal(38.345).toAPR.right.eitherT.value
}
