val CatsEffectVersion = "2.1.2"
val CatsVersion = "2.1.0"
val CirceVersion = "0.13.0"
val Fs2Version = "2.3.0"
val KafkaVersion = "2.4.1"
val Http4sVersion = "0.21.3"
val LogbackVersion = "1.2.3"
val Specs2Version = "4.8.3"

lazy val root = (project in file("."))
  .settings(
    organization := "com.example",
    name := "scala-kafka",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      "co.fs2"           %% "fs2-core"            % Fs2Version,
      "org.apache.kafka"  % "kafka-streams"       % KafkaVersion,
      "org.apache.kafka" %% "kafka-streams-scala" % KafkaVersion,
      "org.http4s"       %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"       %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"       %% "http4s-circe"        % Http4sVersion,
      "org.http4s"       %% "http4s-dsl"          % Http4sVersion,
      "org.typelevel"    %% "cats-core"           % CatsVersion,
      "org.typelevel"    %% "cats-effect"         % CatsEffectVersion,
      "io.circe"         %% "circe-generic"       % CirceVersion,
      "org.specs2"       %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"    % "logback-classic"     % LogbackVersion
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")
  )

scalacOptions ++= Build.commonScalacOptions
