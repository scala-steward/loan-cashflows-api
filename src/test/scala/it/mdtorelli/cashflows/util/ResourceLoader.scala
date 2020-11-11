package it.mdtorelli.cashflows.util

import spray.json.{enrichString, JsValue}

import scala.io.Source

object ResourceLoader extends ResourceLoader
trait ResourceLoader {
  protected def loadResource(resource: String): String = {
    val res = if (resource.startsWith("/")) resource else s"/$resource"
    val source = Source.fromInputStream(getClass.getResourceAsStream(res))
    try source.getLines().mkString("\n")
    finally source.close()
  }

  protected def parseJson(raw: String): JsValue = raw.parseJson

  protected def parseJsonResource(resource: String): JsValue = parseJson(loadResource(resource))
}
