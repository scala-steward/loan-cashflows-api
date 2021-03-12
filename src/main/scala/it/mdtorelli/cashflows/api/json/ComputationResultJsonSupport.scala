package it.mdtorelli.cashflows.api.json

import it.mdtorelli.cashflows.model._
import it.mdtorelli.cashflows.model.Implicits._

import spray.json._

trait ComputationResultJsonSupport extends JsonSupport {
  import DefaultJsonProtocol._

  protected implicit final lazy val aprFormat: JsonFormat[APR] = new JsonFormat[APR] {
    override def read(json: JsValue): APR =
      json match {
        case JsNumber(x) => x.toDecimal.toAPR
        case x           => deserializationError(s"Expected JSON number but got: '$x'")
      }
    override def write(obj: APR): JsValue = JsNumber(obj.scaled.value.value)
  }
  protected implicit final lazy val irrFormat: JsonFormat[IRR] = new JsonFormat[IRR] {
    override def read(json: JsValue): IRR =
      json match {
        case JsNumber(x) => x.toDecimal.toIRR
        case x           => deserializationError(s"Expected JSON number but got: '$x'")
      }
    override def write(obj: IRR): JsValue = JsNumber(obj.scaled.value.value)
  }

  protected implicit final lazy val computationResultFormat: RootJsonFormat[ComputationResult] =
    jsonFormat2(ComputationResult.apply)
}
