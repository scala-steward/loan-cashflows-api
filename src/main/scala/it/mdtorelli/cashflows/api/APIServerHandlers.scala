package it.mdtorelli.cashflows.api

import it.mdtorelli.cashflows.adt.GenericError
import it.mdtorelli.cashflows.api.json.JsonSupport._

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives.complete

trait APIServerHandlers {
  protected implicit final val rejectionHandler: RejectionHandler =
    RejectionHandler
      .newBuilder()
      .result()
      .seal
      .mapRejectionResponse {
        case res @ HttpResponse(_, _, entity: HttpEntity.Strict, _) =>
          val message = entity.data.utf8String.replaceAll("\"", """\"""")
          res.withEntity(entity = HttpEntity(ContentTypes.`application/json`, s"""{"message": "$message"}"""))
        case x                                                      => x
      }

  protected implicit final val defaultExceptionHandler: ExceptionHandler =
    ExceptionHandler { ex: Throwable =>
      complete(StatusCodes.InternalServerError, GenericError("Unhandled error", Option(ex.getMessage)))
    }
}
