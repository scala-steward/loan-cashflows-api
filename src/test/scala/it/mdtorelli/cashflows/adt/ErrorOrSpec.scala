package it.mdtorelli.cashflows.adt

import cats.instances.try_._
import it.mdtorelli.cashflows.util.BaseSpec
import org.scalatest.OptionValues

import scala.concurrent.{ExecutionException, TimeoutException}

final class ErrorOrSpec extends BaseSpec with OptionValues {
  behavior of "ErrorOr"

  "successful" should "be a right" in {
    ErrorOr.successful((): Unit).resolveRight shouldBe a[Unit]
  }

  it should "not compile for ApplicationError" in {
    """ErrorOr.successful(GenericError("BOOM!"))""" shouldNot compile
  }

  "failed" should "be a left" in {
    ErrorOr.failed(GenericError("BOOM!")).resolveLeft shouldBe a[GenericError]
  }

  it should "not compile for anything different than ApplicationError" in {
    """ErrorOr.failed((): Unit)""" shouldNot compile
  }

  "pure" should "execute the operation successfully" in {
    ErrorOr.pure((): Unit).resolveRight shouldBe a[Unit]
  }

  it should "return an error if the operation is timed-out" in {
    val ex = new TimeoutException("BOOM!")

    ErrorOr.pure(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationTimedOutError]
      error.message shouldEqual "Async operation timed-out"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is interrupted" in {
    val ex = new InterruptedException("BOOM!")

    ErrorOr.pure(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationInterruptedError]
      error.message shouldEqual "Async operation interrupted"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is failed (1)" in {
    val ex = new ExecutionException("BOOM!", new RuntimeException("BOOM 2!"))

    ErrorOr.pure(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM 2!"
      error.details should not be defined
      error.cause.value shouldEqual ex.getCause
    }
  }

  it should "return an error if the operation is failed (2)" in {
    val ex = new RuntimeException("BOOM!")

    ErrorOr.pure(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM!"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  "withDetails" should "execute the operation successfully" in {
    ErrorOr.withDetails("details")((): Unit).resolveRight shouldBe a[Unit]
  }

  it should "return an error if the operation is timed-out" in {
    val ex = new TimeoutException("BOOM!")

    ErrorOr.withDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationTimedOutError]
      error.message shouldEqual "Async operation timed-out"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is interrupted" in {
    val ex = new InterruptedException("BOOM!")

    ErrorOr.withDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationInterruptedError]
      error.message shouldEqual "Async operation interrupted"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is failed (1)" in {
    val ex = new ExecutionException("BOOM!", new RuntimeException("BOOM 2!"))

    ErrorOr.withDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM 2!"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex.getCause
    }
  }

  it should "return an error if the operation is failed (2)" in {
    val ex = new RuntimeException("BOOM!")

    ErrorOr.withDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM!"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

}
