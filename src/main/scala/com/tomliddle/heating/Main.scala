package com.tomliddle.heating

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = HeatingServer.run[IO]
