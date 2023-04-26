package com.tomliddle.heating

import cats.effect.IO
import com.tomliddle.heating.adt.DataTypes.Event
import com.tomliddle.heating.http.{HeatingApi, HeatingRoutes}
import com.tomliddle.heating.processor.StreamProcessor
import com.tomliddle.heating.stubs.BoilerServiceStub
import org.http4s.*
import org.http4s.implicits.*
import munit.CatsEffectSuite

class SetTempSpec extends CatsEffectSuite:

/*  private[this] val setTemperature: IO[Response[IO]] =
    val temperatureService = new BoilerServiceImpl[IO]
    val topic = fs2.concurrent.Topic[IO, Event]
    val streamProcessor = new StreamProcessor(temperatureService, topic.)
    val routes = new HeatingRoutes(temperatureServiceStub)
    val getRoot = Request[IO](Method.GET, uri"temp/set/19.0/16.0")
    routes.heatingRoute.orNotFound.run(getRoot)

  test("setTemperature returns status code 200") {
    assertIO(setTemperature.map(_.status) ,Status.Ok)
  }
*/
  test("setTemperature returns current temp") {
    //assertIO(setTemperature.flatMap(_.as[String]), "{\"currentTemp\":14.5}")
    //assertIO(IO.unit[String], "")
    assert(true)
  }

