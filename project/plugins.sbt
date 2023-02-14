addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.15")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.7")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.10.4")
