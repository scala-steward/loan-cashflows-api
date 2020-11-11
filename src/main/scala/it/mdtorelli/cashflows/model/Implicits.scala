package it.mdtorelli.cashflows.model

import cats.Show
import cats.syntax.show._

object Implicits {
  private implicit def seqShow[A: Show]: Show[Seq[A]] =
    Show.show(_.map(implicitly[Show[A]].show).mkString("[", ", ", "]"))

  implicit val localDateShow: Show[java.time.LocalDate] = Show.fromToString
  implicit val idShow: Show[Id] = Show.show(_.value.toString)
  implicit val decimalShow: Show[Decimal] = Show.show(_.value.toString)
  implicit val aprShow: Show[APR] = Show.show(_.scaled.value.show)
  implicit val irrShow: Show[IRR] = Show.show(_.scaled.value.show)
  implicit val scheduleShow: Show[Schedule] = Show.show { x =>
    show"""|(
           |id=${x.id},
           |date=${x.date},
           |principal=${x.principal},
           |interestFee=${x.interestFee}
           |)""".stripMargin.replaceAll("\n", "")
  }
  implicit val cashFlowShow: Show[CashFlow] = Show.show { x =>
    show"""|(
           |principal=${x.principal}, 
           |upfrontFee=${x.upfrontFee}, 
           |upfrontCreditLineFee=${x.upfrontCreditlineFee}, 
           |schedule=${x.schedule}
           |)""".stripMargin.replaceAll("\n", "")
  }

  implicit final class BigDecimalOps(x: BigDecimal) {
    def toDecimal: Decimal = Decimal(x)
    def toDecimal(scale: Int): Decimal = Decimal(x).withScale(scale)
  }

  implicit final class DecimalOps(x: Decimal) {
    def withScale(scale: Int): Decimal = x.copy(x.value.setScale(scale, scala.math.BigDecimal.RoundingMode.HALF_UP))
    def toAPR: APR = APR(x)
    def toIRR: IRR = IRR(x)
  }

  implicit final class APROps(x: APR) {
    def scaled: APR = x.copy(x.value.withScale(APR.scale))
  }

  implicit final class IRROps(x: IRR) {
    def scaled: IRR = x.copy(x.value.withScale(IRR.scale))
  }
}
