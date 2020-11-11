package it.mdtorelli.cashflows

import cats.Show

import scala.concurrent.{ExecutionContext, ExecutionException, Future, TimeoutException}

package object adt {
  object ApplicationError {
    implicit val applicationErrorShow: Show[ApplicationError] = Show.show { error =>
      s"Error: ${error.message}${error.details.map(x => s" ($x)").getOrElse("")}"
    }
  }
  sealed trait ApplicationError extends Product with Serializable {
    def message: String
    def details: Option[String]
    def cause: Option[Throwable]
  }

  final case class GenericError(message: String, details: Option[String] = None, cause: Option[Throwable] = None)
      extends ApplicationError

  final case class AsyncOperationTimedOutError(details: Option[String] = None, cause: Option[Throwable] = None)
      extends ApplicationError {
    override val message: String = "Async operation timed-out"
  }

  final case class AsyncOperationInterruptedError(details: Option[String] = None, cause: Option[Throwable] = None)
      extends ApplicationError {
    override val message: String = "Async operation interrupted"
  }

  type ErrorOr[+A] = Either[ApplicationError, A]
  type AsyncErrorOr[+A] = Future[ErrorOr[A]]

  object ErrorOr {
    import cats.syntax.option._
    import it.mdtorelli.cashflows.adt.Implicits._

    private def recoverAllToErrorOr[A](details: Option[String]): PartialFunction[Throwable, AsyncErrorOr[A]] = {
      case ex: InterruptedException => AsyncOperationInterruptedError(details, ex.some).leftFuture
      case ex: TimeoutException     => AsyncOperationTimedOutError(details, ex.some).leftFuture
      case ex: ExecutionException   => recoverAllToErrorOr(details)(ex.getCause)
      case ex: Throwable            => GenericError(ex.getMessage, details, ex.some).leftFuture
    }

    def fromFutureWithDetails[A](details: String)(x: Future[A])(implicit ec: ExecutionContext): AsyncErrorOr[A] =
      x.map(_.right).recoverWith(recoverAllToErrorOr[A](details.some))

    def fromFuture[A](x: Future[A])(implicit ec: ExecutionContext): AsyncErrorOr[A] =
      x.map(_.right).recoverWith(recoverAllToErrorOr[A](none))

    def asyncWithDetails[A](details: String)(x: => A)(implicit ec: ExecutionContext): AsyncErrorOr[A] =
      ErrorOr.fromFutureWithDetails(details)(Future(x))

    def async[A](x: => A)(implicit ec: ExecutionContext): AsyncErrorOr[A] =
      ErrorOr.fromFuture(Future(x))
  }
}
