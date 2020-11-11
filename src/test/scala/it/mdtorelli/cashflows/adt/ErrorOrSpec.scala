package it.mdtorelli.cashflows.adt

import it.mdtorelli.cashflows.util.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ExecutionException, TimeoutException}

final class ErrorOrSpec extends BaseSpec {
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(500.millis)

  behavior of "ErrorOr"

  "async" should "execute the operation successfully" in {
    ErrorOr.async((): Unit).resolveRight shouldBe a[Unit]
  }

  it should "return an error if the operation is timed-out" in {
    val ex = new TimeoutException("BOOM!")

    ErrorOr.async(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationTimedOutError]
      error.message shouldEqual "Async operation timed-out"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is interrupted" in {
    val ex = new InterruptedException("BOOM!")

    ErrorOr.async(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationInterruptedError]
      error.message shouldEqual "Async operation interrupted"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is failed (1)" in {
    val ex = new ExecutionException("BOOM!", new RuntimeException("BOOM 2!"))

    ErrorOr.async(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM 2!"
      error.details should not be defined
      error.cause.value shouldEqual ex.getCause
    }
  }

  it should "return an error if the operation is failed (2)" in {
    val ex = new RuntimeException("BOOM!")

    ErrorOr.async(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM!"
      error.details should not be defined
      error.cause.value shouldEqual ex
    }
  }

  "asyncWithDetails" should "execute the operation successfully" in {
    ErrorOr.asyncWithDetails("details")((): Unit).resolveRight shouldBe a[Unit]
  }

  it should "return an error if the operation is timed-out" in {
    val ex = new TimeoutException("BOOM!")

    ErrorOr.asyncWithDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationTimedOutError]
      error.message shouldEqual "Async operation timed-out"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is interrupted" in {
    val ex = new InterruptedException("BOOM!")

    ErrorOr.asyncWithDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[AsyncOperationInterruptedError]
      error.message shouldEqual "Async operation interrupted"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

  it should "return an error if the operation is failed (1)" in {
    val ex = new ExecutionException("BOOM!", new RuntimeException("BOOM 2!"))

    ErrorOr.asyncWithDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM 2!"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex.getCause
    }
  }

  it should "return an error if the operation is failed (2)" in {
    val ex = new RuntimeException("BOOM!")

    ErrorOr.asyncWithDetails("details")(throw ex).assertLeft { error =>
      error shouldBe a[GenericError]
      error.message shouldEqual "BOOM!"
      error.details.value shouldEqual "details"
      error.cause.value shouldEqual ex
    }
  }

}
