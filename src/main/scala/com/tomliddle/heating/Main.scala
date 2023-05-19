package com.tomliddle.heating

import cats.effect.{Async, IO, IOApp, Sync}
import cats.syntax.all.*
import com.comcast.ip4s.*
import org.http4s.implicits.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import cats.effect.*
import cats.implicits.*
import com.tomliddle.heating.Main.Logging
import com.tomliddle.heating.adt.DataTypes
import com.tomliddle.heating.adt.DataTypes.Event
import com.tomliddle.heating.http.HeatingRoutes
import com.tomliddle.heating.processor.StreamProcessor
import fs2.concurrent.Topic
import org.http4s.ember.server.EmberServerBuilder


object Main extends IOApp.Simple with Logging[IO]:
  val run = runApp


  def runApp: IO[Nothing] = {
    for {
      _ <- logger.info("Start App")
      temperatureService = new BoilerServiceImpl[IO]
      topic <- fs2.concurrent.Topic[IO, Event]
      streamProcessor = new StreamProcessor(temperatureService, topic)
      httpApp = new HeatingRoutes(streamProcessor).heatingRoutes.orNotFound
      _ <- streamProcessor.runStream.compile.drain.start
      server <-
        EmberServerBuilder.default[IO]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(httpApp)
          .build
          .useForever
    } yield server
  }


  trait Logging[F[_]: Sync] {
    given logger: Logger[F] = Slf4jLogger.getLogger[F]


  }