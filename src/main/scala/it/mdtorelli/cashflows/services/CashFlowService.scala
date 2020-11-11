package it.mdtorelli.cashflows.services

import it.mdtorelli.cashflows.adt.AsyncErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.{APRCalculator, IRRCalculator}
import it.mdtorelli.cashflows.model.{CashFlow, ComputationResult}

import scala.concurrent.ExecutionContext

object CashFlowService {
  def apply(apr: APRCalculator, irr: IRRCalculator)(implicit ec: ExecutionContext): CashFlowService =
    new CashFlowServiceImpl(apr, irr)
}
sealed trait CashFlowService {
  def computeResult(cashflow: CashFlow): AsyncErrorOr[ComputationResult]
}

private final class CashFlowServiceImpl(apr: APRCalculator, irr: IRRCalculator)(implicit ec: ExecutionContext)
    extends CashFlowService {
  override def computeResult(cashflow: CashFlow): AsyncErrorOr[ComputationResult] = {
    val (aprF, irrF) = (apr.compute(cashflow), irr.compute(cashflow))
    (for {
      apr <- aprF.eitherT
      irr <- irrF.eitherT
    } yield ComputationResult(apr, irr)).value
  }
}
