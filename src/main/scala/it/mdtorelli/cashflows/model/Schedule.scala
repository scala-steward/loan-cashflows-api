package it.mdtorelli.cashflows.model

import java.time.LocalDate

final case class Schedule private (id: Id, date: LocalDate, principal: Decimal, interestFee: Decimal)
