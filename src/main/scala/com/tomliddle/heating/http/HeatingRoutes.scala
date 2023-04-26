package com.tomliddle.heating.http

import cats.effect.{Async, IO}
import cats.implicits.toSemigroupKOps
import com.tomliddle.heating.BoilerService
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError, TempCommand}
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.syntax.option.catsSyntaxOptionId
import com.tomliddle.heating.processor.StreamProcessor
import fs2.concurrent.Topic.Closed
import cats.syntax.all._

class HeatingRoutes(streamProcessor: StreamProcessor) {

  private val setTemperature: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic { s =>
      streamProcessor.publish(TempCommand(s._1.some)).map(e => toResult(e, s._1))
    })

  private val getTemperature: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(HeatingApi.getTemperatureEndpoint.serverLogic { s =>
      IO.pure(Result(2.2).asRight[ResultError])
    })

  private def toResult(e: Either[Closed, Unit], value: Double): Either[ResultError, Result] = e match {
    case Left(_) => ResultError("Cannot set temp").asLeft
    case Right(_) => Result(value).asRight
  }

  val heatingRoutes: HttpRoutes[IO] =
    setTemperature <+> getTemperature
}
