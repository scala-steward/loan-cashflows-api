addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.13")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.6")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")
