package it.mdtorelli.cashflows.model

object IRR {
  private[model] val scale = 10
}
final case class IRR private (value: Decimal)
