import sbt.{ModuleID, _}
import Keys._


object ServiceDependencies {

  //val Http4sVersion = "1.0.0-M29"
  val MunitVersion = "0.7.29"
  val LogbackVersion = "1.4.5"
  val MunitCatsEffectVersion = "1.0.7"
  val Fs2Version = "3.6.1"
  val Tapir = "1.2.9"
  val Cats = "2.9.0"
  val CatsEffect = "3.4.8"

  def apply(): Seq[ModuleID] = commonDependencies ++ tapir

  private val commonDependencies: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % Cats,
    "org.typelevel" %% "cats-effect" % CatsEffect,
    "co.fs2" %% "fs2-core" % Fs2Version,
    "co.fs2" %% "fs2-io" % Fs2Version,
    "org.scalameta" %% "munit" % MunitVersion % Test,
    "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    //"org.typelevel" %% "log4cats-core"    % "2.5.0",
    "org.typelevel" %% "log4cats-slf4j"   % "2.5.0",
    "org.http4s" %% "http4s-ember-server" % "0.23.18",
    "org.http4s" %% "http4s-ember-client" % "0.23.18",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.2.9",
    //"com.github.fd4s" %% "fs2-kafka" % Fs2Version
  )

  private val tapir =
    Seq[ModuleID](
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Tapir
    )

}
