import sbt._

object Dependencies {
  lazy val cats = "org.typelevel"                           %% "cats-core"        % Versions.cats
  lazy val scalafixOrganizeImports = "com.github.liancheng" %% "organize-imports" % Versions.scalafixOrganizeImports
  lazy val scalaTest = "org.scalatest"                      %% "scalatest"        % Versions.scalaTest % Test
  lazy val akkaHttp = Seq(
    "com.typesafe.akka" %% "akka-http"            % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-stream"          % Versions.akka,
    "com.typesafe.akka" %% "akka-http-testkit"    % Versions.akkaHttp % Test,
    "com.typesafe.akka" %% "akka-testkit"         % Versions.akka     % Test,
    "com.typesafe.akka" %% "akka-stream-testkit"  % Versions.akka     % Test)
}
