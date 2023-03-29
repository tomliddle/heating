package com.tomliddle.heating

import cats.*
import cats.data.*
import cats.implicits.*
import cats.effect.{IO, Sync}
import com.tomliddle.heating.adt.DataTypes
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError}

class BoilerServiceStub[F[_]](implicit F: Sync[F]) extends BoilerService[F] {
    override def setTemp(tempCommand: DataTypes.TempCommand): F[Either[ResultError, Result]] = F.pure(Result(14.5).asRight)
}
