import sbt._, Keys._

object Build {

  lazy val commonScalacOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
  )

  lazy val commonSettings = List(
    scalaVersion := "3.0.0",
    scalacOptions ++= commonScalacOptions
  )
}

/*
  // "io.circe" %% "circe-core" % "0.5.0-M2",
  // "io.circe" %% "circe-generic" % "0.5.0-M2",
  // "io.circe" %% "circe-literal" % "0.5.0-M2",
  // "io.circe" %% "circe-parser" % "0.5.0-M2",

  // "org.atnos" %% "eff-cats" % "1.6.2",

  // "com.chuusai" %% "shapeless" % "2.3.1",
  // "io.argonaut" %% "argonaut" % "6.1",
  // "com.github.alexarchambault" %% "argonaut-shapeless_6.1" % "0.3.1",
  // "com.github.mpilquist" %% "simulacrum" % "0.7.0",

  // "net.virtual-void" %% "speed" % "15" % "provided",

  // "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  // "com.sun.jna" % "jna" % "3.0.9",
*/