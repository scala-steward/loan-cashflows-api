addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
scalafmtConfig := file(".scalafmt.conf")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.16")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.9")

addSbtPlugin("com.github.sbt" % "sbt-git" % "2.0.1")

addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.11.0")
