package it.mdtorelli.cashflows

import cats.Show

import scala.concurrent.{ExecutionException, TimeoutException}

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

  object ErrorOr {
    import cats.MonadError
    import cats.syntax.applicativeError._
    import cats.syntax.functor._
    import cats.syntax.option._
    import it.mdtorelli.cashflows.adt.Implicits._

    type MonadThrowableError[F[_]] = MonadError[F, Throwable]

    private def recoverAllToErrorOr[A](details: Option[String]): PartialFunction[Throwable, ErrorOr[A]] = {
      case ex: InterruptedException => AsyncOperationInterruptedError(details, ex.some).left
      case ex: TimeoutException     => AsyncOperationTimedOutError(details, ex.some).left
      case ex: ExecutionException   => recoverAllToErrorOr(details).apply(ex.getCause)
      case ex: Throwable            => GenericError(ex.getMessage, details, ex.some).left
    }

    def fromFWithDetails[F[_]: MonadThrowableError, A](details: String)(x: => F[A]): F[ErrorOr[A]] =
      x.map(_.right).recover(recoverAllToErrorOr(details.some))

    def fromF[F[_]: MonadThrowableError, A](x: => F[A]): F[ErrorOr[A]] =
      x.map(_.right).recover(recoverAllToErrorOr(none))

    def withDetails[F[_]: MonadThrowableError, A](details: String)(x: => A): F[ErrorOr[A]] = {
      val m = implicitly[MonadThrowableError[F]]
      ErrorOr.fromFWithDetails(details) {
        try m.pure(x)
        catch {
          case t: Throwable => m.raiseError(t)
        }
      }
    }

    def pure[F[_]: MonadThrowableError, A](x: => A): F[ErrorOr[A]] = {
      val m = implicitly[MonadThrowableError[F]]
      ErrorOr.fromF {
        try m.pure(x)
        catch {
          case t: Throwable => m.raiseError(t)
        }
      }
    }
  }
}
