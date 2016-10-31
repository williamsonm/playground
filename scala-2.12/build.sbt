scalaVersion := "2.12.0-M5"

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

// lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)
