import Dependencies._
import Settings._

lazy val root = (project in file(".")).commonSettings.gitSettings.coverageSettings.dockerSettings
  .settings(
    name := "loan-cashflows-api",
    mainClass in (Compile, run) := Some("it.mdtorelli.cashflows.Main"),
    libraryDependencies ++= akkaHttp,
    libraryDependencies += cats,
    libraryDependencies += scalaTest)

addCommandAlias("fmt", "; scalafmt; scalafmtSbt; test:scalafmt")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; test:scalafmtCheck")
