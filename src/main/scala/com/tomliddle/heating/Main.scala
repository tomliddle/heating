package com.tomliddle.heating

import cats.effect.{Async, IO, IOApp, Sync}
import cats.syntax.all.*
import com.comcast.ip4s.*
import org.http4s.implicits.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.*
import cats.implicits.*
import com.tomliddle.heating.adt.DataTypes
import com.tomliddle.heating.http.HeatingRoutes
import org.http4s.ember.server.EmberServerBuilder

object Main extends IOApp.Simple:
  val run = runApp[IO]

  given logger[F[_]: Sync]: Logger[F] = Slf4jLogger.getLogger[F]
  def runApp[F[_] : Async]: F[Nothing] = {
    for {
      _ <- Logger[F].info("Start App")
      temperatureService = new TemperatureServiceImpl[F]
      httpApp = (
        new HeatingRoutes[F](temperatureService).heatingRoutes <+>
          new HeatingRoutes[F](temperatureService).heatingRoutes
        ).orNotFound

      e <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp)
          .build
          .useForever
    } yield e
  }