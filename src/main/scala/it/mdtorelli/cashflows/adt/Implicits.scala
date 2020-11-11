package it.mdtorelli.cashflows.adt

import cats.data.EitherT
import cats.implicits._

import scala.concurrent.Future

object Implicits {
  implicit final class AnyErrorOps[A](val value: A) extends AnyVal {
    def right: ErrorOr[A] = value.asRight[ApplicationError]

    def rightFuture: AsyncErrorOr[A] = value.right.asFuture
  }

  implicit final class ApplicationErrorOps(val value: ApplicationError) extends AnyVal {
    def left[A]: ErrorOr[A] = value.asLeft

    def leftFuture[A]: AsyncErrorOr[A] = value.left.asFuture
  }

  implicit final class ErrorOrOps[A](val value: ErrorOr[A]) extends AnyVal {
    def asFuture: AsyncErrorOr[A] = Future.successful(value)

    def eitherT: EitherT[Future, ApplicationError, A] = asFuture.eitherT

    def asSome: ErrorOr[Option[A]] = value.map(Some.apply)

    def onRight[B](f: A => B): ErrorOr[A] =
      value.map { a =>
        f(a)
        a
      }

    def onLeft[B](f: ApplicationError => B): ErrorOr[A] =
      value.leftMap { e =>
        f(e)
        e
      }

    def onBoth[B](f: ErrorOr[A] => B): ErrorOr[A] = {
      f(value)
      value
    }
  }

  implicit final class ErrorOrFOps[F[_], A](val value: F[ErrorOr[A]]) extends AnyVal {
    def eitherT: EitherT[F, ApplicationError, A] = EitherT(value)
  }
}
