package com.tomliddle.heating.adt


object DataTypes {

  
  case class Result(currentTemp: Double)
  case class ResultError(message: String)

  sealed trait Event
  final case class TempEvent(setTemp: Double, currentTemp: Double) extends Event {
    val increaseNeeded = setTemp - currentTemp
  }
  case object Tick extends Event
  
  
  trait Command
  final case class TempCommand(waterTemperature: Option[Double]) extends Event


  
  
 // def apply[F[_]](implicit ev: DataTypes[F]): DataTypes[F] = ev
  

  /*def impl[F[_]: Concurrent](C: Client[F]): DataTypes[F] = new DataTypes[F]:
    val dsl = new Http4sClientDsl[F]{}
    import dsl.*
    def get: F[DataTypes.HeatDemand] =
      C.expect[HeatDemand](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => JokeError(t)} // Prevent Client Json Decoding Failure Leaking
  */
}