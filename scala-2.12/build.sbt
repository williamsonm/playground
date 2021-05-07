

// addCompilerPlugin("com.milessabin" % "si2712fix-plugin_2.11.8" % "1.2.0")
// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val core = project.in(file(".")).settings(
  Build.commonSettings,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.6.0",
    "org.typelevel" %% "cats-free" % "2.6.0"
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full)
)

initialCommands in console := """
  import cats._, cats.implicits._
  """
