addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.10.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.1.0")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.12.1")
