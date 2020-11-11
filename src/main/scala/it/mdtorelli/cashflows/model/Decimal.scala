package it.mdtorelli.cashflows.model

object Decimal {
  val Zero: Decimal = Decimal(0.0)
}
final case class Decimal private (value: BigDecimal) extends AnyVal
