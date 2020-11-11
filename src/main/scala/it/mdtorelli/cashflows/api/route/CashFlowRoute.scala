package it.mdtorelli.cashflows.api.route

import akka.http.scaladsl.server.{Directives, Route}
import it.mdtorelli.cashflows.api.CompletionDirectives
import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.services.CashFlowService

object CashFlowRoute {
  def apply(cashFlowService: CashFlowService): Route = new CashFlowRoute(cashFlowService).route
}

final class CashFlowRoute private (cashFlowService: CashFlowService)
    extends Directives
    with CashFlowJsonSupport
    with ComputationResultJsonSupport
    with CompletionDirectives {
  private val computeCashFlowRoute: Route =
    entity(as[CashFlow]) { cashFlow =>
      logRequest("POST /cashflows") {
        completeWith(cashFlowService.computeResult(cashFlow))
      }
    }

  lazy val route: Route =
    path("cashflows") {
      post {
        computeCashFlowRoute
      }
    }
}
