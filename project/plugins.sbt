addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.2")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.0")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.26")
