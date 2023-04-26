package com.tomliddle.heating.processor

import cats.effect.{Concurrent, IO, Resource, Sync, Temporal}
import com.tomliddle.heating.adt.DataTypes.*

import scala.util.Random
import cats.syntax.option.catsSyntaxOptionId
import com.tomliddle.heating.BoilerService
import com.tomliddle.heating.Main.Logging
import fs2.{Pipe, Pure}

import java.time
import java.time.Instant
import scala.collection.immutable.Queue
import scala.concurrent.duration.*
import fs2.concurrent.Topic
import fs2.concurrent.Topic.Closed

class StreamProcessor(boilerService: com.tomliddle.heating.BoilerService[IO], topic: Topic[IO, Event]) extends Logging {

  private val heatingSetFrequency: FiniteDuration = 5.minutes

  def publish(t: TempCommand): IO[Either[Closed, Unit]] = {
    topic.publish1(t)
  }

  def runStream: fs2.Stream[IO, State] = {
    val stream: fs2.Stream[IO, Event] = topic.subscribeUnbounded
    val timerStream: fs2.Stream[Pure, Tick.type] = fs2.Stream(Tick)

    val result: fs2.Stream[IO, Event] = stream ++ timerStream
    val events: fs2.Stream[IO, Event] = result.mergeHaltL(fs2.Stream.awakeEvery[IO](heatingSetFrequency).map(_ => Tick))

    events
      .evalTap(f => logger.info(s"processing ${f}"))
      .evalScan(State()) {
      case (state, Tick) =>
        processCommand(state).flatMap(_ => IO.pure(state))
      case (state, t: SetTemp) => IO.pure(state.withTemp(t))
    }
  }

  private def processCommand(state: State): IO[Either[ResultError, Result]] =
    boilerService.setTemp(process(state.recentTemps.head))

  // TODO change this to its own service and within F context
  private def process(e: SetTemp): TempCommand = {
    val max = 60.0
    // -1 == Off
    // 0 == 30
    // 1 == 40
    // 2 == 50
    // 3 == 60
    val waterTemp = e.increaseNeeded match {
      case t if t < -1 => None
      case t if t >= 3 => max.some
      case t           => (30 + (t * 10)).some
    }

    TempCommand(waterTemp)
  }

  /*def withTopic[F[_]](implicit F: Concurrent[F]): fs2.Stream[F, String] = {
    val topic = fs2.concurrent.Topic[F, String]

    val topicStream: fs2.Stream[F, Topic[F, String]] = fs2.Stream.eval(topic)

    topicStream.flatMap { topic =>
      val publisher: fs2.Stream[F, Pipe[F, String, Nothing]] =
        fs2.Stream.emit("1").repeat.covary[F].map(t => topic.publish)
      val subscriber: fs2.Stream[F, String] = topic.subscribe(10).take(4)
      subscriber.concurrently(publisher)
    }
  }*/

}
