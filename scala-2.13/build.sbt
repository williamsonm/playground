lazy val core = project.in(file("."))
  .settings(
    Build.commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.1.0",
      "org.typelevel" %% "cats-effect" % "2.0.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.3" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

initialCommands in console := """import cats._, cats.implicits._"""
