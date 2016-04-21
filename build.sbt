libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.4.1",

  "io.circe" %% "circe-core" % "0.4.0",
  "io.circe" %% "circe-generic" % "0.4.0",
  "io.circe" %% "circe-parser" % "0.4.0"
)


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
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
)

initialCommands in console := """
    import cats._
    import cats.std.all._
    import cats.syntax.all._
  """
