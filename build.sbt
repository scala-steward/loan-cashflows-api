import Dependencies._
import Settings._

lazy val root = (project in file(".")).commonSettings.gitSettings.coverageSettings.dockerSettings
  .settings(
    name := "loan-cashflows-api",
    mainClass in (Compile, run) := Some("it.mdtorelli.cashflows.Main"),
    libraryDependencies ++= akkaHttp,
    libraryDependencies += cats,
    libraryDependencies += scalaTest)

addCommandAlias("fix", "; compile:scalafix; test:scalafix")
addCommandAlias("fmt", "; scalafmt; scalafmtSbt; test:scalafmt")
addCommandAlias("fixCheck", "; compile:scalafix --check; test:scalafix --check")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; test:scalafmtCheck")
