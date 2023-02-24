package com.tomliddle.heating.http

import cats.effect.{Async, IO}
import com.tomliddle.heating.TemperatureService
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter

class HeatingRoutes[F[_]: Async](temperatureService: TemperatureService[F]) {


  val heatingRoute: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic(temperatureService.setTemp))

  val anotherRoute: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic(temperatureService.setTemp))
  
  val heatingRoutes: HttpRoutes[F] =
    heatingRoute
}
