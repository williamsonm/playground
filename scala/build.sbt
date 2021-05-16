libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",

  // "io.circe" %% "circe-core" % "0.5.0-M2",
  // "io.circe" %% "circe-generic" % "0.5.0-M2",
  // "io.circe" %% "circe-literal" % "0.5.0-M2",
  // "io.circe" %% "circe-parser" % "0.5.0-M2",

  // "org.atnos" %% "eff-cats" % "1.6.2",

  // "com.chuusai" %% "shapeless" % "2.3.1",
  // "io.argonaut" %% "argonaut" % "6.1",
  // "com.github.alexarchambault" %% "argonaut-shapeless_6.1" % "0.3.1",

  "co.fs2" %% "fs2-core" % "3.0.3",
  "co.fs2" %% "fs2-io" % "3.0.3",

  // "com.github.mpilquist" %% "simulacrum" % "0.7.0",

  // "net.virtual-void" %% "speed" % "15" % "provided",

  // "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  // "com.sun.jna" % "jna" % "3.0.9",

  "org.scalatest" %% "scalatest" % "3.2.9" % "test",
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % "test"
)

scalaVersion := "3.0.0"

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
  // "-Xlint",
  // "-Yinline-warnings",
  // "-Yno-adapted-args",
  // "-Ywarn-dead-code",
  // "-Ywarn-numeric-widen",
  // "-Ywarn-value-discard",
  // "-Xfuture"
)

// addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.7.1")
// addCompilerPlugin("com.milessabin" % "si2712fix-plugin_2.11.8" % "1.2.0")
// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)

initialCommands in console := """
  import tcs._
  import Maybe._, Functor.ops._, Applicative.ops._, Monad.ops._
  import Usage._
  """
  // import cats._
  // import cats.std.all._
  // import cats.syntax.all._
