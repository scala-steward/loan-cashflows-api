import sbt._

object Dependencies {
  private object Versions {
    val cats = "2.4.2"
    val akkaHttp = "10.2.3"
    val akka = "2.6.12"
    val scalaTest = "3.2.4"
  }

  lazy val cats = "org.typelevel"      %% "cats-core" % Versions.cats
  lazy val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test
  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http"            % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-stream"          % Versions.akka,
    "com.typesafe.akka" %% "akka-http-testkit"    % Versions.akkaHttp % Test,
    "com.typesafe.akka" %% "akka-testkit"         % Versions.akka     % Test,
    "com.typesafe.akka" %% "akka-stream-testkit"  % Versions.akka     % Test)
}
