package com.tomliddle.heating

import cats._
import cats.data._
import cats.implicits._
import cats.effect.IO
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError}

class TemperatureServiceStub extends TemperatureService[IO] {
  
  override def setTemp(temp: Double): IO[Either[ResultError, Result]] = IO.pure(Result(14.5).asRight)
}
