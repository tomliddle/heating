val Http4sVersion = "1.0.0-M29"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"
val Fs2Version = "3.5.0"
val Tapir = "1.2.6"
val Cats = "2.9.0"
val CatsEffect = "3.4.5"


lazy val root = (project in file("."))
  .settings(
    organization := "com.tomliddle",
    name := "heating",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "3.1.0",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Tapir,
      "org.typelevel" %% "cats-core" % Cats,
"org.typelevel" %% "cats-effect" % CatsEffect,
      "co.fs2" %% "fs2-core" % Fs2Version,
      "co.fs2" %% "fs2-io" % Fs2Version,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      //"com.github.fd4s" %% "fs2-kafka" % Fs2Version
    ),
    testFrameworks += new TestFramework("munit.Framework")
  )
