package it.mdtorelli.cashflows.api.route

import akka.http.scaladsl.server.{Directives, Route}

object RootRoute {
  def apply: Route = new RootRoute().route
}

final class RootRoute private () extends Directives {
  lazy val route: Route =
    pathSingleSlash {
      get {
        logRequest("GET /") {
          complete("Welcome")
        }
      }
    }
}
