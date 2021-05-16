libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.6.1",
  "co.fs2" %% "fs2-core" % "3.0.3",
  "co.fs2" %% "fs2-io" % "3.0.3",
  "org.scalameta" %% "munit" % "0.7.26" % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % Test
)

scalaVersion := "3.0.0"

lazy val core = project
  .in(file("."))
  .settings(Build.commonSettings)

console / initialCommands := """
    import cats.syntax.all._
  """
