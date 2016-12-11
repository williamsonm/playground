scalaVersion := "2.12.1"

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
  // "-Yinline-warnings",
  "-Yno-adapted-args",
  // "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.8.1"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
// addCompilerPlugin("com.milessabin" % "si2712fix-plugin_2.11.8" % "1.2.0")
// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)

initialCommands in console := """
  import cats._, cats.implicits._
  """
