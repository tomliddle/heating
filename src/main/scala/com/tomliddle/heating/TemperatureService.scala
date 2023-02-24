package com.tomliddle.heating


  

class TemperatureService[F[_]] {

  def setTemp(temp: Double): F[Either[Throwable, Double]] = ???
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