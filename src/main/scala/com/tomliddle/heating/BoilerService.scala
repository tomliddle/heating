package com.tomliddle.heating

import cats.effect.Async
import com.tomliddle.heating.adt.DataTypes.{Result, ResultError, TempCommand}
import cats.*
import cats.data.*
import cats.implicits.*

trait BoilerService[F[_]] {

  def setTemp(tempCommand: TempCommand): F[Either[ResultError, Result]]
}


class BoilerServiceImpl[F[_]: Async] extends BoilerService[F] {
  override def setTemp(tempCommand: TempCommand): F[Either[ResultError, Result]] = {
    Async[F].pure(Result(19.6).asRight)
  }
}

/*
import cats.Applicative
import cats.implicits._

import org.http4s.EntityEncoder


trait HelloWorld[F[_]]:
  def hello(n: String): F[String]

object HelloWorld:

  def impl[F[_]: Applicative]: HelloWorld[F] = new HelloWorld[F]:
    def hello(n: HelloWorld.Name): F[HelloWorld.Greeting] =
        Greeting("Hello, " + n.name).pure[F]
*/