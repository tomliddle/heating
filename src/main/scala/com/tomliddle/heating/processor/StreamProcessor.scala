package com.tomliddle.heating.processor

import cats.data.EitherT
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

class StreamProcessor(boilerService: com.tomliddle.heating.BoilerService[IO], topic: Topic[IO, Event]) extends Logging[IO] {

  private val heatingSetFrequency: FiniteDuration = 10.seconds

  def publish(t: SetTemp): IO[Either[Closed, Unit]] =
    topic.publish1(t)
    
  def get: IO[State] = IO.pure(State(List(SetTemp(4.4, 4.4))))

  def runStream: fs2.Stream[IO, State] = {
    val stream: fs2.Stream[IO, Event]            = topic.subscribeUnbounded
    val timerStream: fs2.Stream[Pure, Tick.type] = fs2.Stream(Tick)

    val result: fs2.Stream[IO, Event] = stream ++ timerStream
    val events: fs2.Stream[IO, Event] = result.mergeHaltL(fs2.Stream.awakeEvery[IO](heatingSetFrequency).map(_ => Tick))

    events
      .evalTap(f => logger.info(s"processing $f"))
      .evalScan(State()) {
        case (state, Tick) =>
          for {
            tmp <- processCommand(state)
            _ <- logger.info(s"state is $state tmp is $tmp")
          } yield state
        case (state, t: SetTemp) => IO.pure(state.withTemp(t))
        case (state, _) => IO.pure(state)
      }
  }

  private def processCommand(state: State): IO[Either[ResultError, TempCommand]] = (for {
    t   <- EitherT.fromOption(state.recentTemps.headOption, ResultError("No recent temp"))
    res <- EitherT.apply(boilerService.setTemp(t))
  } yield res).value

}
