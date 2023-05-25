package com.tomliddle.heating

import cats.effect.IO
import com.tomliddle.heating.adt.DataTypes.Event
import com.tomliddle.heating.http.{HeatingApi, HeatingRoutes}
import com.tomliddle.heating.processor.StreamProcessor
import com.tomliddle.heating.stubs.BoilerServiceStub
import weaver.*
import org.http4s.*
import org.http4s.implicits.*

class StreamProcessorSpec extends SimpleIOSuite {

  def streamProcessor: IO[StreamProcessor[IO]] = for {
    topic <- fs2.concurrent.Topic[IO, Event]
    boilerService = new BoilerServiceStub[IO]
    streamProcessor = new StreamProcessor[IO](boilerService, topic)
  } yield streamProcessor

  test("streamProcessor 1") {
    for {
      sp <- streamProcessor
      result <- sp.get
    } yield expect(result = Status.Ok)
  }

  test("streamProcessor 2") {
    for {
      result <- streamProcessor.map(_.get.as[String])
    } yield expect(result = "{\"targetTemp\":16.0}")
  }
}
