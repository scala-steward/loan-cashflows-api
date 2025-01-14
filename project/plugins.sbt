addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.11.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.2.2")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.1.0")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.13.0")
