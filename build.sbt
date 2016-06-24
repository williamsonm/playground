libraryDependencies ++= Seq(
  "org.typelevel" %% "cats" % "0.5.0",

  "io.circe" %% "circe-core" % "0.4.1",
  "io.circe" %% "circe-generic" % "0.4.1",
  "io.circe" %% "circe-literal" % "0.4.1",
  "io.circe" %% "circe-parser" % "0.4.1",

  "org.atnos" %% "eff-cats" % "1.6.2",

  "com.chuusai" %% "shapeless" % "2.3.1",
  "io.argonaut" %% "argonaut" % "6.1",
  "com.github.alexarchambault" %% "argonaut-shapeless_6.1" % "0.3.1",

  "co.fs2" %% "fs2-core" % "0.9.0-M2",
  "co.fs2" %% "fs2-io" % "0.9.0-M2",

  "net.virtual-void" %% "speed" % "15" % "provided"
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

lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)

initialCommands in console := """
    import cats._
    import cats.std.all._
    import cats.syntax.all._
  """
