package it.mdtorelli.cashflows.util

import it.mdtorelli.cashflows.adt.{ApplicationError, AsyncErrorOr, ErrorOr}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.{Assertion, EitherValues, OptionValues}

trait BaseSpec extends AnyFlatSpec with Matchers with EitherValues with ScalaFutures with OptionValues {
  protected implicit final class FutureErrorOrResolver[T](x: AsyncErrorOr[T]) {
    def resolveRight: T = x.futureValue.value
    def resolveLeft: ApplicationError = x.futureValue.left.value
    def assertLeft(assert: ApplicationError => Assertion): Assertion = assert(x.futureValue.left.value)
  }

  protected implicit final class ErrorOrResolver[T](x: ErrorOr[T]) {
    def resolveRight: T = x.value
    def resolveLeft: ApplicationError = x.left.value
    def assertLeft(assert: ApplicationError => Assertion): Assertion = assert(x.left.value)
  }
}
