import Build.commonScalacOptions

scalaVersion := "2.13.0"

scalacOptions in (Compile, console) ~= (_.filterNot (_ == "-Ywarn-unused-import"))
// scalacOptions in (Compile, console) ~= { opts =>
//   val exclude = List("-Ywarn-unused-import","-Yfatal-warnings")
//   opts filterNot exclude.contains
// }

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.0.0-RC2"
)

// addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")
// addCompilerPlugin("com.milessabin" % "si2712fix-plugin_2.11.8" % "1.2.0")
// addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

lazy val core = project.in(file(".")).settings(scalacOptions ++= commonScalacOptions)

initialCommands in console := """
  import cats._, cats.implicits._
  """
