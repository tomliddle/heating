package com.tomliddle.heating

import cats.effect.IO
import com.tomliddle.heating.adt.DataTypes.Event
import com.tomliddle.heating.http.{HeatingApi, HeatingRoutes}
import com.tomliddle.heating.processor.StreamProcessor
import com.tomliddle.heating.stubs.BoilerServiceStub
import org.http4s.*
import org.http4s.implicits.*
import munit.CatsEffectSuite

class RoutesSpec extends CatsEffectSuite {

  def callEndpoint(request: Request[IO]): IO[Response[IO]] = for {
    topic <- fs2.concurrent.Topic[IO, Event]
    boilerService = new BoilerServiceStub[IO]
    streamProcessor = new StreamProcessor[IO](boilerService, topic)
    routes = new HeatingRoutes[IO](streamProcessor)
    result <- routes.heatingRoutes.orNotFound.run(request)
  } yield result

  test("setTemperature returns status code 200") {
    val request = Request[IO](Method.PUT, uri"temp/19.0/16.0")
    assertIO(callEndpoint(request).map(_.status), Status.Ok)
  }

  test("setTemperature returns current temp") {
    val request = Request[IO](Method.PUT, uri"temp/19.0/16.0")
    assertIO(callEndpoint(request).flatMap(_.as[String]), "{\"targetTemp\":16.0}")
  }

  test("getTemp returns current temp") {
    val request = Request[IO](Method.GET, uri"temp")
    assertIO(callEndpoint(request).flatMap(_.as[String]), "{\"targetTemp\":4.4}")
  }
}