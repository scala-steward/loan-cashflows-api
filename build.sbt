import Dependencies._
import Settings._

lazy val root = (project in file(".")).commonSettings.gitSettings.coverageSettings.dockerSettings
  .settings(
    name := "loan-cashflows-api",
    Compile / run / mainClass := Some("it.mdtorelli.cashflows.Main"),
    libraryDependencies ++= akkaHttp,
    libraryDependencies += cats,
    libraryDependencies += scalaTest)

addCommandAlias("fix", "; scalafix; Test / scalafix")
addCommandAlias("fmt", "; scalafmt; scalafmtSbt; Test / scalafmt")
addCommandAlias("fixCheck", "; scalafix --check; Test / scalafix --check")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; Test / scalafmtCheck")
