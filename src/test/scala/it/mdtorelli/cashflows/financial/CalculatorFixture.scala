package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.model.{APR, CashFlow, ComputationResult, IRR}
import it.mdtorelli.cashflows.util.ResourceLoader

trait CalculatorFixture[F[_]] extends ResourceLoader with CashFlowJsonSupport with ComputationResultJsonSupport {
  protected def aprCalculator: APRCalculator[F]
  protected def irrCalculator: IRRCalculator[F]

  private final val computationResult = parseJsonResource("computation-result.json").convertTo[ComputationResult]
  protected final lazy val cashFlow = parseJsonResource("cashflow.json").convertTo[CashFlow]

  protected def expectedAPR: APR = computationResult.apr
  protected def expectedIRR: IRR = computationResult.irr
}
