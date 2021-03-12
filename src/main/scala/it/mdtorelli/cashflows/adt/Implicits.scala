package it.mdtorelli.cashflows.adt

import cats.Monad
import cats.data.EitherT
import cats.syntax.either._

object Implicits {
  implicit final class AnyErrorOps[A](private val value: A) extends AnyVal {
    def right: ErrorOr[A] = value.asRight[ApplicationError]

    def rightF[F[_]: Monad]: F[ErrorOr[A]] = Monad[F].pure(value.right)

    def rightT[F[_]: Monad]: EitherT[F, ApplicationError, A] = EitherT(rightF)
  }

  implicit final class ApplicationErrorOps(private val value: ApplicationError) extends AnyVal {
    def left[A]: ErrorOr[A] = value.asLeft

    def leftF[F[_]: Monad, A]: F[ErrorOr[A]] = Monad[F].pure(value.left)

    def leftT[F[_]: Monad, A]: EitherT[F, ApplicationError, A] = EitherT(leftF)
  }

  implicit final class ErrorOrOps[A](private val value: ErrorOr[A]) extends AnyVal {
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

  implicit final class ErrorOrFOps[F[_], A](private val value: F[ErrorOr[A]]) extends AnyVal {
    def eitherT: EitherT[F, ApplicationError, A] = EitherT(value)
  }
}
