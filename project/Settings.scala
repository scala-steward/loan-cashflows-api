import com.typesafe.sbt.GitPlugin.autoImport._
import com.typesafe.sbt.GitVersioning
import com.typesafe.sbt.packager.Keys.{daemonUser, daemonUserUid, maintainer}
import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import com.typesafe.sbt.packager.docker.{Cmd, CmdLike, DockerPlugin}
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import org.scalafmt.sbt._
import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys.{coverageExcludedPackages, coverageFailOnMinimum, coverageMinimum}

object Settings {
  private val _organization = "it.mdtorelli"
  private val _organizationName = "MDT"
  private val _scalaVersion = "2.13.3"
  private val _scalacOptions = Seq(
    "-Wconf:cat=deprecation:w",
    "-Wconf:cat=feature:w",
    "-Wconf:cat=optimizer:w",
    "-Wconf:cat=unchecked:w",
    "-Wconf:cat=unused:w",
    "-Xlint:adapted-args",
    "-Xlint:unused",
    "-Wunused:imports",
    "-Wunused:patvars",
    "-Wunused:privates",
    "-Wunused:locals",
    "-Wunused:explicits",
    "-Wunused:implicits",
    "-Wunused:linted",
    "-Wdead-code")

  implicit final class ProjectFrom(project: Project) {
    def minimalSettings: Project =
      project
        .settings(
          organization := _organization,
          organizationName := _organizationName,
          scalaVersion := _scalaVersion,
          scalacOptions ++= _scalacOptions)

    def scalafmtSettings: Project =
      project
        .enablePlugins(ScalafmtPlugin)
        .settings(scalafmtOnCompile := false)

    def gitSettings: Project =
      project.enablePlugins(GitVersioning).settings(git.useGitDescribe := true)

    def coverageSettings: Project =
      project.settings(
        coverageMinimum := 90,
        coverageFailOnMinimum := true,
        coverageExcludedPackages := Seq(
          "<empty>",
          "it.mdtorelli.cashflows.Main",
          "it.mdtorelli.cashflows.adt.ApplicationError.*",
          "it.mdtorelli.cashflows.api.APIServer",
          "it.mdtorelli.cashflows.adt.Implicits.*",
          "it.mdtorelli.cashflows.adt.ErrorOr.TypeConstraints.*",
          "it.mdtorelli.cashflows.adt.ToFuture.Implicits.*",
          "it.mdtorelli.cashflows.model.Implicits.*").mkString(";"))

    def dockerSettings: Project = {
      val runAsUser = "daemon"

      def runAsRoot(cmdLike: CmdLike*): Seq[CmdLike] =
        (Cmd("USER", "root") +: cmdLike) :+ Cmd("USER", runAsUser)

      project
        .enablePlugins(JavaServerAppPackaging)
        .enablePlugins(DockerPlugin)
        .settings(
          dockerBaseImage := "adoptopenjdk/openjdk8:alpine-slim",
          dockerUpdateLatest := true,
          dockerExposedPorts := Seq(8080),
          daemonUserUid in Docker := None,
          daemonUser in Docker := runAsUser,
          maintainer in Docker := organization.value,
          version in Docker := version.value,
          javaOptions in Universal := Seq(
            "-J-XX:MinRAMPercentage=20.0",
            "-J-XX:MaxRAMPercentage=80.0",
            "-J-XshowSettings:vm"),
          dockerCommands ++= runAsRoot(Cmd("RUN", "/sbin/apk", "add", "--no-cache", "bash")))
    }

    def commonSettings: Project =
      project.minimalSettings.scalafmtSettings
        .settings(
          fork in (Test, run) := true,
          parallelExecution in Test := true,
          // Avoid generating scaladoc documentation files on sbt dist, decreasing the size of the build
          sources in (Compile, doc) := Seq.empty,
          publishArtifact in (Compile, packageDoc) := false)
  }
}
