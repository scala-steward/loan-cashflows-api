package it.mdtorelli.cashflows.services

import cats.Monad
import it.mdtorelli.cashflows.adt.ErrorOr
import it.mdtorelli.cashflows.adt.Implicits._
import it.mdtorelli.cashflows.financial.{APRCalculator, IRRCalculator}
import it.mdtorelli.cashflows.model.{CashFlow, ComputationResult}

object CashFlowService {
  def apply[F[_]: Monad](apr: APRCalculator[F], irr: IRRCalculator[F]): CashFlowService[F] =
    new CashFlowServiceImpl(apr, irr)
}
sealed trait CashFlowService[F[_]] {
  def computeResult(cashflow: CashFlow): F[ErrorOr[ComputationResult]]
}

private final class CashFlowServiceImpl[F[_]: Monad](apr: APRCalculator[F], irr: IRRCalculator[F])
    extends CashFlowService[F] {
  override def computeResult(cashflow: CashFlow): F[ErrorOr[ComputationResult]] = {
    val (aprF, irrF) = (apr.compute(cashflow), irr.compute(cashflow))
    (for {
      apr <- aprF.eitherT
      irr <- irrF.eitherT
    } yield ComputationResult(apr, irr)).value
  }
}
