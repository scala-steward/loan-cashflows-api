package it.mdtorelli.cashflows.adt

import cats.Id

import scala.concurrent.Future
import scala.util.Try

object ToFuture {
  def apply[F[_]](implicit F: ToFuture[F]): ToFuture[F] = F

  object Implicits {
    implicit val futureInstance: ToFuture[Future] = new ToFuture[Future] {
      override final def toFuture[A](x: Future[A]): Future[A] = x
    }

    implicit val tryInstance: ToFuture[Try] = new ToFuture[Try] {
      override final def toFuture[A](x: Try[A]): Future[A] = x.fold(Future.failed, Future.successful)
    }

    implicit val idInstance: ToFuture[Id] = new ToFuture[Id] {
      override final def toFuture[A](x: Id[A]): Future[A] = Future.successful(x)
    }
  }
}

sealed trait ToFuture[F[_]] {
  def toFuture[A](f: F[A]): Future[A]
}
