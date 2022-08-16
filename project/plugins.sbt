addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.10")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.2")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.0")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.1")
