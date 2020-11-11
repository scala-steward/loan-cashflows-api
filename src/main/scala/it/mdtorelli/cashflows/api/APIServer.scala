package it.mdtorelli.cashflows.api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object APIServer {
  def apply(host: String, port: Int)(routes: Route)(implicit system: ActorSystem, ec: ExecutionContext): APIServer =
    new APIServer(host, port, routes)
}
final class APIServer private (host: String, port: Int, routes: Route)(implicit
  system: ActorSystem,
  ec: ExecutionContext
) extends APIServerHandlers {
  def start(): Unit =
    Http().newServerAt(host, port).bindFlow(Route.seal(routes)).onComplete {
      case Success(binding) =>
        system.log.info("Server ready at {}:{}", binding.localAddress.getHostString, binding.localAddress.getPort)
      case Failure(ex)      =>
        system.log.error("Unable to bind the server. Terminating.", ex)
        stop()
    }

  def stop(): Unit = system.log.info("Server stopped.")
}
