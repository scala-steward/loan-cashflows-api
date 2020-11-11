package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.model.{APR, CashFlow, ComputationResult, IRR}
import it.mdtorelli.cashflows.util.ResourceLoader

trait CalculatorFixture extends ResourceLoader with CashFlowJsonSupport with ComputationResultJsonSupport {
  protected val aprCalculator: APRCalculator
  protected val irrCalculator: IRRCalculator

  private final val computationResult = parseJsonResource("computation-result.json").convertTo[ComputationResult]
  protected final lazy val cashFlow = parseJsonResource("cashflow.json").convertTo[CashFlow]

  protected def expectedAPR: APR = computationResult.apr
  protected def expectedIRR: IRR = computationResult.irr
}
