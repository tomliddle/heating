package com.tomliddle.heating.http

import cats.effect.{Async, IO}
import cats.implicits.toSemigroupKOps
import com.tomliddle.heating.BoilerService
import com.tomliddle.heating.adt.DataTypes.TempCommand
import org.http4s.HttpRoutes
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.syntax.option.catsSyntaxOptionId

class HeatingRoutes[F[_]: Async](temperatureService: BoilerService[F]) {


  val heatingRoute: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic(s => (temperatureService.setTemp(TempCommand(s._1.some)))))

  val anotherRoute: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(HeatingApi.setTemperatureEndpoint.serverLogic(s => (temperatureService.setTemp(TempCommand(s._1.some)))))
  
  val heatingRoutes: HttpRoutes[F] =
    heatingRoute <+> anotherRoute
}
