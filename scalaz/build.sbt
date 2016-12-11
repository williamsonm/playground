libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.11",
  "org.scalaz.stream" %% "scalaz-stream" % "0.8",
  "io.verizon.journal" %% "core" % "2.3.16"

  // "com.chuusai" %% "shapeless" % "2.3.1",
  // "io.argonaut" %% "argonaut" % "6.1",
  // "com.github.alexarchambault" %% "argonaut-shapeless_6.1" % "0.3.1",

  // "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalaVersion := "2.11.8"

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
  "-Xlint",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  // "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")
addCompilerPlugin("com.milessabin" % "si2712fix-plugin_2.11.8" % "1.2.0")
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)

initialCommands in console := """
  import scalaz._, Scalaz._
  import scalaz.concurrent._
  """
