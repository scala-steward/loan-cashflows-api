package it.mdtorelli.cashflows.api

import akka.http.scaladsl.util.FastFuture
import akka.http.scaladsl.util.FastFuture.EnhancedFuture
import cats.Id

import scala.concurrent.Future
import scala.util.Try

object ToFuture {
  def apply[F[_]](implicit C: ToFuture[F]): ToFuture[F] = C
}

sealed trait ToFuture[F[_]] {
  def toFuture[A](f: F[A]): Future[A]
}

object ToFutureInstances {
  implicit val futureInstance: ToFuture[Future] = new ToFuture[Future] {
    override final def toFuture[A](x: Future[A]): Future[A] = x.fast.future
  }

  implicit val tryInstance: ToFuture[Try] = new ToFuture[Try] {
    override final def toFuture[A](x: Try[A]): Future[A] = FastFuture(x)
  }

  implicit val idInstance: ToFuture[Id] = new ToFuture[Id] {
    override final def toFuture[A](x: Id[A]): Future[A] = Future.successful(x)
  }

}
