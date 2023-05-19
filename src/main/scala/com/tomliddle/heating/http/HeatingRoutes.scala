package com.tomliddle.heating.http

import cats.effect.{Async, IO}
import cats.implicits.toSemigroupKOps
import com.tomliddle.heating.BoilerService
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError, SetTemp, TempCommand}
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.syntax.option.catsSyntaxOptionId
import fs2.concurrent.Topic.Closed
import cats.syntax.all.*
import com.tomliddle.heating.processor.StreamProcessor

class HeatingRoutes(streamProcessor: StreamProcessor) {

  private val setTemperature: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic { case (currTemp, setTemp) =>
      streamProcessor.publish(SetTemp(currTemp, setTemp)).map(e => toResult(e, setTemp))
    })

  private val getTemperature: HttpRoutes[IO] =
    Http4sServerInterpreter[IO]().toRoutes(HeatingApi.getTemperatureEndpoint.serverLogic { _ =>
      for {
        s <- streamProcessor.get
        x = s.recentTemps.headOption.map(_.setTemp).getOrElse(-1.0)
      } yield Result(x).asRight[ResultError]
    })

  private def toResult(e: Either[Closed, Unit], value: Double): Either[ResultError, Result] = e match {
    case Left(_) => ResultError("Cannot set temp").asLeft
    case Right(_) => Result(value).asRight
  }

  val heatingRoutes: HttpRoutes[IO] =
    setTemperature <+> getTemperature
}
