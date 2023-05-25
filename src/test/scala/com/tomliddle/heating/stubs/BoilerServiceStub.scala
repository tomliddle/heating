package com.tomliddle.heating.stubs

import cats.*
import cats.data.*
import cats.effect.{IO, Sync}
import cats.implicits.*
import com.tomliddle.heating.BoilerService
import com.tomliddle.heating.adt.DataTypes
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError, TempCommand}

case class BoilerServiceStub[F[_]]()(implicit F: Sync[F]) extends BoilerService[F] {

  override def setTemp(tempCommand: DataTypes.SetTemp): F[Either[ResultError, DataTypes.TempCommand]] = {
    F.pure(TempCommand(1.1.some).asRight)
  }
}
