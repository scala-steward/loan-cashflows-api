package it.mdtorelli.cashflows.financial.convergence

import it.mdtorelli.cashflows.model.Decimal
import it.mdtorelli.cashflows.model.Implicits._
import it.mdtorelli.cashflows.util.BaseSpec

import scala.util.Try

final class ConvergenceFunctionsSpec extends BaseSpec {
  import ConvergeFunctionBehaviors._
  import ConvergenceFunctions._

  behavior of "Iterative Steffensen"

  it should behave like aConvergeFunctionUsing(iterativeSteffensen())

  behavior of "Tail-recursive Steffensen"

  it should behave like aConvergeFunctionUsing(tailRecSteffensen())

  behavior of "Iterative Secant"

  it should behave like aConvergeFunctionUsing(iterativeSecant())

  behavior of "Tail-recursive Secant"

  it should behave like aConvergeFunctionUsing(tailRecSecant())

  private object ConvergeFunctionBehaviors {
    private final val scale = 10

    final def aConvergeFunctionUsing(convergenceFunction: ConvergenceFunction[Try]): Unit = {
      def resultFor(f: BigDecimal => BigDecimal): Decimal =
        convergenceFunction.converge(f).resolveRight.withScale(scale)
      def scaledDecimal(x: BigDecimal): Decimal = x.toDecimal(scale)

      it should "make x^3-100 converge to zero" in {
        resultFor { x =>
          x.pow(3) - 100
        } shouldEqual scaledDecimal(4.641588833612778892410076)
      }

      it should "make cos(x)-x converge to zero" in {
        resultFor { x =>
          Math.cos(x.toDouble) - x
        } shouldEqual scaledDecimal(0.73908513321516064166)
      }

      it should "make x^2−e^x−3x+2 converge to zero" in {
        resultFor { x =>
          x.pow(2) - Math.pow(Math.E, x.toDouble) - 3 * x + 2
        } shouldEqual scaledDecimal(0.25753028543986076046)
      }

      it should "make ln(x^2+x+2)−x+1 converge to zero" in {
        resultFor { x =>
          Math.log((x.pow(2) + x + 2).toDouble) - x + 1
        } shouldEqual scaledDecimal(4.1525907367571582750)
      }

      it should "make 8x−cos(x)−2x^2 converge to zero" in {
        resultFor { x =>
          8 * x - Math.cos(x.toDouble) - 2 * x.pow(2)
        } shouldEqual scaledDecimal(0.12807710275379877853)
      }
    }
  }
}
