package com.tomliddle.heating.processor

import cats.effect.{Concurrent, IO, Sync, Temporal}
import com.tomliddle.heating.adt.DataTypes.{TempEvent, *}

import scala.util.Random
import cats.syntax.option.catsSyntaxOptionId
import com.tomliddle.heating.BoilerService
import fs2.Pure

import java.time
import java.time.Instant
import scala.collection.immutable.Queue
import scala.concurrent.duration.*

case class State(recentTemps: Queue[TempEvent] = Queue.empty, lastCommandTime: time.Instant = Instant.now) {
  def withTemp(t: TempEvent): State = this.copy(recentTemps = recentTemps.takeRight(9).enqueue(t))
  def resetTime: State              = this.copy(lastCommandTime = Instant.now)
}

class StreamProcessor[F[_]](boilerService: com.tomliddle.heating.BoilerService[F])(using F: Temporal[F]) {

  val heatingSetFrequency: time.Duration = java.time.Duration.ofMinutes(5)

  def tempEvent: TempEvent = TempEvent(Random.between(10.0, 20.0), Random.between(10.0, 20.0))

  def runStream = {
    val stream: fs2.Stream[F, TempEvent]    = fs2.Stream.constant(tempEvent).covary[F].take(100)
    val timerStream: fs2.Stream[Pure, Tick.type] = fs2.Stream(Tick)

    val result: fs2.Stream[F, Event] = stream ++ timerStream
    val events: fs2.Stream[F, Event] = result.mergeHaltL(fs2.Stream.awakeEvery[F](5.minutes).map(_ => Tick))

    events.evalScan(State()) {
      case (state, Tick) =>
        F.flatMap(doCommand(state))(_ => F.pure(state))
      case (state, t: TempEvent) => F.pure(state.withTemp(t))
    }
  }

  def doCommand(state: State): F[Either[ResultError, Result]] =
    boilerService.setTemp(process(state.recentTemps.head))

  // TODO change this to its own service and within F context
  def process(e: TempEvent): TempCommand = {
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

}
