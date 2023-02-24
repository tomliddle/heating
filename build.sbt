import sbt._

lazy val root = (project in file("."))
  .settings(
    organization := "com.tomliddle",
    name         := "heating",
    version      := "0.0.1-SNAPSHOT",
    scalaVersion := "3.2.2",
    testFrameworks += new TestFramework("munit.Framework"),
    libraryDependencies ++= ServiceDependencies()
  )
