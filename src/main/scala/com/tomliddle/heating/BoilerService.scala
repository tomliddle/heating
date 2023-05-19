package com.tomliddle.heating

import cats.effect.{Async, Sync}
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError, SetTemp, TempCommand}
import cats.*
import cats.data.*
import cats.implicits.*
import com.tomliddle.heating.Main.Logging

trait BoilerService[F[_]] {

  def setTemp(tempCommand: SetTemp): F[Either[ResultError, TempCommand]]
}


class BoilerServiceImpl[F[_]: Async: Sync] extends BoilerService[F] with Logging[F] {
  override def setTemp(temp: SetTemp): F[Either[ResultError, TempCommand]] = (for {
    _ <- EitherT.pure(logger.info(s"Water temp has been set to"))
    res <- EitherT.apply(process(temp))
  } yield res).value

  // TODO change this to its own service and within F context
  private def process(e: SetTemp): F[Either[ResultError, TempCommand]] = {
    val max = 60.0
    // 0 <= Off
    // 1 == 40
    // 2 == 50
    // 3 == 60
    val waterTemp = e.increaseNeeded match {
      case t if t <= 0.3  => None
      case t if t >= 3 => max.some
      case t => (30 + (t * 10)).some
    }

    Async[F].pure(TempCommand(waterTemp).asRight[ResultError])
  }
}