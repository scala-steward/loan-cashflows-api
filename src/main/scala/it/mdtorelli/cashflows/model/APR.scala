package it.mdtorelli.cashflows.model

object APR {
  private[model] val scale = 1
}
final case class APR private (value: Decimal)
