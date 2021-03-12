package it.mdtorelli.cashflows.util

import it.mdtorelli.cashflows.adt.{ApplicationError, ErrorOr}

import org.scalatest.{Assertion, EitherValues, TryValues}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Try

trait BaseSpec extends AnyFlatSpec with Matchers {
  protected implicit final class ErrorOrResolver[T](x: ErrorOr[T]) extends EitherValues {
    def resolveRight: T = x.value
    def resolveLeft: ApplicationError = x.left.value
    def assertLeft(assert: ApplicationError => Assertion): Assertion = assert(x.left.value)
  }

  protected implicit final class ErrorOrTryResolver[T](x: Try[ErrorOr[T]]) extends TryValues with EitherValues {
    def resolveRight: T = x.success.value.value
    def resolveLeft: ApplicationError = x.success.value.left.value
    def assertLeft(assert: ApplicationError => Assertion): Assertion = assert(x.success.value.left.value)
  }
}
