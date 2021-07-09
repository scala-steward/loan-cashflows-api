addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.3")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.8.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.8.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.29")
