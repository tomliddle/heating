package com.tomliddle.heating

import cats.effect.IO
import com.tomliddle.heating.http.{HeatingApi, HeatingRoutes}
import org.http4s.*
import org.http4s.implicits.*
import munit.CatsEffectSuite

class HelloWorldSpec extends CatsEffectSuite:

  test("setTemperature returns status code 200") {
    assertIO(setTemperature.map(_.status) ,Status.Ok)
  }

  test("setTemperature returns hello world message") {
    assertIO(setTemperature.flatMap(_.as[String]), "{\"message\":\"Hello, world\"}")
  }

  private[this] val setTemperature: IO[Response[IO]] =
    val temperatureServiceStub = new TemperatureServiceStub()
    val routes = new HeatingRoutes[IO](temperatureServiceStub)
    val getRoot = Request[IO](Method.GET, uri"temp/set/19.0")
    routes.heatingRoutes.orNotFound.run(getRoot)
