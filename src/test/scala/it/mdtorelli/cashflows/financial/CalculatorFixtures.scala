package it.mdtorelli.cashflows.financial

import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.model.{APR, CashFlow, ComputationResult, IRR}
import it.mdtorelli.cashflows.util.{BaseSpec, ResourceLoader}

trait CalculatorFixtures extends ResourceLoader with CashFlowJsonSupport with ComputationResultJsonSupport {
  self: BaseSpec =>

  private final val computationResult = parseJsonResource("computation-result.json").convertTo[ComputationResult]
  protected final lazy val cashFlow = parseJsonResource("cashflow.json").convertTo[CashFlow]

  trait APRCalculatorFixture[F[_]] {
    protected def aprCalculator: APRCalculator[F]

    private final val computationResult = parseJsonResource("computation-result.json").convertTo[ComputationResult]
    protected final lazy val cashFlow = parseJsonResource("cashflow.json").convertTo[CashFlow]

    protected def expectedAPR: APR = computationResult.apr
  }

  trait IRRCalculatorFixture[F[_]] {
    protected def irrCalculator: IRRCalculator[F]

    protected def expectedIRR: IRR = computationResult.irr
  }
}
