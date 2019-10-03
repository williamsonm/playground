val CatsVersion = "2.0.0"

lazy val core = project.in(file("."))
  .settings(
    Build.commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % CatsVersion,
      "org.typelevel" %% "cats-effect" % CatsVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )

initialCommands in console := """import cats._, cats.implicits._"""
