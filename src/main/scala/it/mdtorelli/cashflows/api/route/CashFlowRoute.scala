package it.mdtorelli.cashflows.api.route

import akka.http.scaladsl.server.{Directives, Route}
import it.mdtorelli.cashflows.api.json.{CashFlowJsonSupport, ComputationResultJsonSupport}
import it.mdtorelli.cashflows.api.{CompletionDirectives, ToFuture}
import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.services.CashFlowService

object CashFlowRoute {
  def apply[F[_]: ToFuture](cashFlowService: CashFlowService[F]): Route =
    new CashFlowRoute(cashFlowService).route
}

final class CashFlowRoute[F[_]: ToFuture] private (cashFlowService: CashFlowService[F])
    extends Directives
    with CashFlowJsonSupport
    with ComputationResultJsonSupport
    with CompletionDirectives {

  private val computeCashFlowRoute: Route =
    entity(as[CashFlow]) { cashFlow =>
      logRequest("POST /cashflows") {
        completeF(cashFlowService.computeResult(cashFlow))
      }
    }

  lazy val route: Route =
    path("cashflows") {
      post {
        computeCashFlowRoute
      }
    }
}
