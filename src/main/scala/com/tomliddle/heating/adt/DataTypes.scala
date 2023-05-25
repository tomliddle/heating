package com.tomliddle.heating.adt

import scala.collection.immutable
import scala.collection.immutable.Queue


object DataTypes {


  case class State(recentTemps: List[SetTemp] = List.empty) {
    def withTemp(t: SetTemp): State = this.copy(recentTemps = t +: recentTemps.take(5))
  }

  
  case class Result(targetTemp: Double)
  case class ResultError(message: String)

  sealed trait Event
  final case class SetTemp(currTemp: Double, setTemp: Double) extends Event {
    val increaseNeeded = setTemp - currTemp
  }
  case object Tick extends Event

  final case class TempCommand(waterTemperature: Option[Double]) extends Event

}