package it.mdtorelli.cashflows.api.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import it.mdtorelli.cashflows.adt.ApplicationError
import it.mdtorelli.cashflows.model.Decimal
import spray.json._

object JsonSupport extends JsonSupport

trait JsonSupport extends SprayJsonSupport {
  implicit final lazy val printer: JsonPrinter = PrettyPrinter

  implicit final lazy val decimalFormat: JsonFormat[Decimal] = new JsonFormat[Decimal] {
    override def write(obj: Decimal): JsValue = JsNumber(obj.value)

    @scala.annotation.tailrec
    override def read(json: JsValue): Decimal =
      json match {
        case JsNumber(raw)                                => Decimal.apply(raw)
        case JsObject(fields) if fields.contains("value") => read(fields("value"))
        case x                                            => deserializationError(s"Expected JSON number but got: '$x'")
      }
  }

  implicit final def applicationErrorWriter[A <: ApplicationError]: RootJsonWriter[A] = error => {
    val defaultFields: Seq[JsField] = Seq("message" -> JsString(error.message))
    val optionalFields: Seq[JsField] = error match {
      case error if error.details.nonEmpty => Seq("details" -> JsString(error.details.get))
      case _                               => Seq.empty
    }

    JsObject(defaultFields ++ optionalFields: _*)
  }
}
