val CatsVersion = "2.6.0"
val LogbackVersion = "1.2.3"
val TwitterVersion = "21.4.0"

lazy val core = project.in(file(".")).settings(
  Build.commonSettings,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % CatsVersion,
    "org.typelevel" %% "cats-free" % CatsVersion,
    "com.twitter"   %% "util-core" % TwitterVersion,

    "org.scalatest" %% "scalatest" % "3.2.7" % "test",

    "ch.qos.logback" % "logback-classic" % LogbackVersion
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)
)

initialCommands in console := """
  import cats._, cats.implicits._
  """
