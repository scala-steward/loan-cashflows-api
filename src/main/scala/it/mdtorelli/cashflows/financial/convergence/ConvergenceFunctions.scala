package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.adt.{AsyncErrorOr, ErrorOr}
import it.mdtorelli.cashflows.model.Decimal
import it.mdtorelli.cashflows.model.Implicits._

import scala.concurrent.ExecutionContext

sealed abstract class ConvergenceFunction {
  def converge(f: BigDecimal => BigDecimal)(implicit ec: ExecutionContext): AsyncErrorOr[Decimal]
}

object ConvergenceFunctions {
  private final val defaultX: BigDecimal = 0
  private final val defaultTolerance: BigDecimal = 0.000000000001

  /*
   * Iterative implementation of the Steffensen's method
   * Ref: https://en.wikipedia.org/wiki/Steffensen%27s_method
   */
  final def iterativeSteffensen(
    x: BigDecimal = defaultX,
    tolerance: BigDecimal = defaultTolerance
  ): ConvergenceFunction =
    new ConvergenceFunction {
      override def converge(f: BigDecimal => BigDecimal)(implicit ec: ExecutionContext): AsyncErrorOr[Decimal] =
        ErrorOr.asyncWithDetails("operation: iterativeSteffensen") {
          var (x0, f0) = (x, f(x))

          while (f0.abs > tolerance) {
            val f1 = f(x0 + f0)
            val g = (f1 / f0) - 1
            x0 = x0 - (f0 / g)
            f0 = f(x0)
          }

          x0.toDecimal
        }
    }

  /*
   * Tail-recursive implementation of the Steffensen's method
   * Ref: https://en.wikipedia.org/wiki/Steffensen%27s_method
   */
  final def tailRecSteffensen(x: BigDecimal = defaultX, tolerance: BigDecimal = defaultTolerance): ConvergenceFunction =
    new ConvergenceFunction {
      override def converge(f: BigDecimal => BigDecimal)(implicit ec: ExecutionContext): AsyncErrorOr[Decimal] =
        ErrorOr.asyncWithDetails("operation: tailRecSteffensen") {
          @scala.annotation.tailrec
          def step(x0: BigDecimal, f0: BigDecimal): Decimal =
            if (f0.abs <= tolerance) x0.toDecimal
            else {
              val f1 = f(x0 + f0)
              val g = (f1 / f0) - 1
              val x1 = x0 - (f0 / g)
              step(x1, f(x1))
            }

          step(x, f(x))
        }
    }

  /*
   * Iterative implementation of the Secant method
   * Ref: https://en.wikipedia.org/wiki/Secant_method
   */
  final def iterativeSecant(
    x: BigDecimal = defaultX,
    y: BigDecimal = defaultX + 1,
    tolerance: BigDecimal = defaultTolerance
  ): ConvergenceFunction =
    new ConvergenceFunction {
      override def converge(f: BigDecimal => BigDecimal)(implicit ec: ExecutionContext): AsyncErrorOr[Decimal] =
        ErrorOr.asyncWithDetails("operation: iterativeSecant") {
          var (x0, x1, f0, f1) = (x, y, f(x), f(y))

          while ((f1 - f0).abs > tolerance) {
            val x2 = x1 - f1 * (x1 - x0) / (f1 - f0)
            x0 = x1
            x1 = x2
            f0 = f1
            f1 = f(x2)
          }

          x1.toDecimal
        }
    }

  /*
   * Tail-recursive implementation of the Secant method
   * Ref: https://en.wikipedia.org/wiki/Secant_method
   */
  final def tailRecSecant(
    x: BigDecimal = defaultX,
    y: BigDecimal = defaultX + 1,
    tolerance: BigDecimal = defaultTolerance
  ): ConvergenceFunction =
    new ConvergenceFunction {
      override def converge(f: BigDecimal => BigDecimal)(implicit ec: ExecutionContext): AsyncErrorOr[Decimal] =
        ErrorOr.asyncWithDetails("operation: tailRecSecant") {
          @scala.annotation.tailrec
          def step(x0: BigDecimal, x1: BigDecimal, f0: BigDecimal, f1: BigDecimal): Decimal =
            if ((f1 - f0).abs <= tolerance) x1.toDecimal
            else {
              val x2 = x1 - f1 * (x1 - x0) / (f1 - f0)
              step(x1, x2, f1, f(x2))
            }

          step(x, y, f(x), f(y))
        }
    }
}
