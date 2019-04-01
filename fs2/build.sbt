import org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings

name := "fs2-playground"
organization := "com.pennwell"
organizationName := "PennWell Corporation"
description := "Don't cross the streams..."

fork := true

val catsVersion = "1.5.0"
val catsEffectVersion = "1.2.0"
val circeVersion = "0.11.1"
val http4sVersion = "0.20.0-M6"
val jacksonVersion = "2.9.8"

lazy val coreDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "co.fs2" %% "fs2-core" % "1.0.3",
  "co.fs2" %% "fs2-io" % "1.0.3",
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-fs2" % "0.11.0",
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.verizon.knobs" %% "core" % "6.0.33.1",
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-core" % http4sVersion,
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
)

lazy val testDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % "it,test",
  "org.scalatest" %% "scalatest" % "3.0.5" % "it,test"
)

lazy val core = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Build.buildSettings)
  .settings(
    Defaults.itSettings,
    inConfig(IntegrationTest)(scalafmtConfigSettings),
    libraryDependencies ++= coreDependencies ++ testDependencies
  )

  wartremoverErrors in (Compile, compile) ++= Seq(
    Wart.AsInstanceOf,
    Wart.DefaultArguments,
    Wart.EitherProjectionPartial,
    Wart.IsInstanceOf,
    Wart.TraversableOps,
    Wart.NonUnitStatements,
    Wart.Null,
    Wart.OptionPartial,
    Wart.Product,
    Wart.Return,
    Wart.Serializable,
    Wart.StringPlusAny,
    Wart.Throw,
    Wart.TryPartial,
    Wart.Var
  )

addCommandAlias("validate", ";compile;test;scalastyle;test:scalastyle")
addCommandAlias("report",   ";clean;coverage;test;coverageReport")
