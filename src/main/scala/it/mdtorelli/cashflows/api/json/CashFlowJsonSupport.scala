package it.mdtorelli.cashflows.api.json

import java.time.LocalDate

import it.mdtorelli.cashflows.model._
import spray.json._

trait CashFlowJsonSupport extends JsonSupport {
  import DefaultJsonProtocol._

  protected implicit final lazy val localDateFormat: JsonFormat[LocalDate] = new JsonFormat[LocalDate] {
    private val formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
    override def write(obj: LocalDate): JsValue = JsString(formatter.format(obj))
    override def read(json: JsValue): LocalDate =
      json match {
        case JsString(raw) =>
          scala.util
            .Try(LocalDate.parse(raw, formatter))
            .getOrElse(deserializationError(s"Expected ISO date format, but got: '$raw'"))
        case x             => deserializationError(s"Expected JSON string but got: '$x'")
      }
  }

  protected implicit final lazy val idFormat: JsonFormat[Id] = new JsonFormat[Id] {
    override def write(obj: Id): JsValue = JsNumber(obj.value)
    override def read(json: JsValue): Id =
      json match {
        case JsNumber(raw) if raw.isValidLong => Id.apply(raw.toLong)
        case JsNumber(x)                      => deserializationError(s"Expected a long value number but got: '$x'")
        case x                                => deserializationError(s"Expected JSON number but got: '$x'")
      }
  }
  protected implicit final lazy val scheduleFormat: RootJsonFormat[Schedule] = jsonFormat4(Schedule.apply)
  protected implicit final lazy val cashFlowFormat: RootJsonFormat[CashFlow] = jsonFormat4(CashFlow.apply)
}
