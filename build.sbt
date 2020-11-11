import Dependencies._
import Settings._

ThisBuild / version := "1.0.0"

lazy val root = (project in file(".")).commonSettings.coverageSettings.dockerSettings
  .settings(
    name := "loan-cashflows-api",
    mainClass in (Compile, run) := Some("it.mdtorelli.cashflows.Main"),
    libraryDependencies ++= akkaHttp,
    libraryDependencies += cats,
    libraryDependencies += scalaTest % Test)

addCommandAlias("fmt", "; scalafmt; scalafmtSbt; test:scalafmt")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; test:scalafmtCheck")
