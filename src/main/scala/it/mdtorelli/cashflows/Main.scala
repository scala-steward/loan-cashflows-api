package it.mdtorelli.cashflows

import akka.actor.ActorSystem
import it.mdtorelli.cashflows.api.APIServer
import it.mdtorelli.cashflows.api.route.{CashFlowRoute, RootRoute}
import it.mdtorelli.cashflows.financial.convergence._
import it.mdtorelli.cashflows.services.CashFlowService

import scala.concurrent.{ExecutionContext, Future}

object Main extends App {
  import it.mdtorelli.cashflows.adt.ToFuture.Implicits.futureInstance

  implicit val system: ActorSystem = ActorSystem("LoanCashflowsApi")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val convergenceF: ConvergenceFunction[Future] = ConvergenceFunctions.tailRecSecant()

  val cashFlowService = CashFlowService[Future](ConvergenceAPRCalculator[Future], ConvergenceIRRCalculator[Future])

  val server = APIServer("0.0.0.0", 8080) {
    import akka.http.scaladsl.server.Directives._

    RootRoute.apply ~ CashFlowRoute(cashFlowService)
  }

  scala.sys.addShutdownHook {
    server.stop()
  }

  server.start()
}
