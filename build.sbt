import Dependencies._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / version := "0.1.0"
ThisBuild / organization := ""
ThisBuild / organizationName := ""

addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full)
addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1")

lazy val root = (project in file("."))
  .settings(
    name := "weather-api",
    libraryDependencies ++= Libraries
  )
