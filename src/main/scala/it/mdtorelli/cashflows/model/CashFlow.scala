package it.mdtorelli.cashflows.model

import java.time.LocalDate

object CashFlow {
  private implicit final val localDateOrdering: Ordering[LocalDate] = Ordering.by(_.toEpochDay)
  private implicit final val idOrdering: Ordering[Id] = Ordering.by(_.value)

  final def apply(
    principal: Decimal,
    upfrontFee: Decimal,
    upfrontCreditlineFee: Decimal,
    schedule: Seq[Schedule]
  ): CashFlow = new CashFlow(principal, upfrontFee, upfrontCreditlineFee, schedule.sortBy(_.id).sortBy(_.date))
}
final case class CashFlow private (
  principal: Decimal,
  upfrontFee: Decimal,
  upfrontCreditlineFee: Decimal,
  schedule: Seq[Schedule]
)
