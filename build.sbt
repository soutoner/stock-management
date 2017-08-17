import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "stock-management",
    organization := "com.rindus",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.12.3",
    libraryDependencies ++= backendDeps
  ).enablePlugins(PlayScala)

mainClass in Compile := Some("com.rindus.Main")