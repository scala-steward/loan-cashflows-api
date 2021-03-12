package it.mdtorelli.cashflows.api

import it.mdtorelli.cashflows.adt._
import it.mdtorelli.cashflows.api.CompletionDirectives.defaultStatusCodeErrorHandler
import it.mdtorelli.cashflows.api.json.JsonSupport

import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}

import scala.util.{Failure, Success}

private object CompletionDirectives {
  private val defaultStatusCodeErrorHandler: ApplicationError => StatusCode = {
    case _: AsyncOperationTimedOutError => StatusCodes.RequestTimeout
    case _                              => StatusCodes.InternalServerError
  }
}
trait CompletionDirectives {
  self: Directives with JsonSupport =>

  protected final def completeF[F[_]: ToFuture, T: ToEntityMarshaller](
    f: F[ErrorOr[T]],
    recoverWith: PartialFunction[ApplicationError, T] = PartialFunction.empty,
    statusCodeErrorHandler: PartialFunction[ApplicationError, StatusCode] = PartialFunction.empty,
    responseStatusCode: StatusCode = StatusCodes.OK
  ): Route = onComplete(ToFuture[F].toFuture(f)) {
    case Success(Right(value))                                             =>
      complete(responseStatusCode -> value)
    case Success(Left(error)) if recoverWith.isDefinedAt(error)            =>
      complete(responseStatusCode -> recoverWith(error))
    case Success(Left(error)) if statusCodeErrorHandler.isDefinedAt(error) =>
      complete(statusCodeErrorHandler(error) -> error)
    case Success(Left(error))                                              =>
      complete(defaultStatusCodeErrorHandler(error) -> error)
    case Failure(ex)                                                       =>
      throw ex
  }
}
