package com.tomliddle.heating.processor

import cats.Monad
import cats.data.EitherT
import cats.effect.{Async, Concurrent, Resource, Sync, Temporal}
import com.tomliddle.heating.adt.DataTypes.*

import scala.util.Random
import cats.syntax.option.catsSyntaxOptionId
import com.tomliddle.heating.{BoilerService, Persistance}
import com.tomliddle.heating.Main.Logging
import fs2.{Pipe, Pure}
import cats.implicits.toFlatMapOps
import cats.implicits.toFunctorOps

import java.time
import java.time.Instant
import scala.collection.immutable.Queue
import scala.concurrent.duration.*
import fs2.concurrent.Topic
import fs2.concurrent.Topic.Closed

class StreamProcessor[F[_]: Async](boilerService: BoilerService[F], topic: Topic[F, Event]) extends Logging[F] {

  private val heatingSetFrequency: FiniteDuration = 5.seconds

  def publish(t: SetTemp): F[Either[Closed, Unit]] =
    topic.publish1(t)


  def runStream: fs2.Stream[F, State] = {
    val stream: fs2.Stream[F, Event]            = topic.subscribeUnbounded
    val timerStream: fs2.Stream[Pure, Tick.type] = fs2.Stream(Tick)

    val result: fs2.Stream[F, Event] = stream ++ timerStream
    val events: fs2.Stream[F, Event] = result.mergeHaltL(fs2.Stream.awakeEvery[F](heatingSetFrequency).map(_ => Tick))

    events
      .evalTap(f => logger.info(s"processing $f"))
      .evalScan(State()) {
        case (state, Tick) =>
          for {
            tmp <- processCommand(state)
            _ <- logger.info(s"state is $state tmp is $tmp")
          } yield state
        case (state, t: SetTemp) => Async[F].pure(state.withTemp(t))
        case (state, _) => Async[F].pure(state)
      }
  }

  private def processCommand(state: State): F[Either[ResultError, TempCommand]] = (for {
    t   <- EitherT.fromOption[F](state.recentTemps.headOption, ResultError("No recent temp"))
    res <- EitherT.apply(boilerService.setTemp(t))
  } yield res).value

}
