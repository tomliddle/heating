package com.tomliddle.heating.adt

import cats.effect.Concurrent
import cats.implicits.*
import com.tomliddle.heating.adt.DataTypes
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.Method.*
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.implicits.uri
import org.http4s.syntax.all.uri
import org.http4s.syntax.literals.uri

object DataTypes:

  case class Result(currentTemp: Double)
  final case class HeatDemand(temperature: Double)
  
 // def apply[F[_]](implicit ev: DataTypes[F]): DataTypes[F] = ev
  

  /*def impl[F[_]: Concurrent](C: Client[F]): DataTypes[F] = new DataTypes[F]:
    val dsl = new Http4sClientDsl[F]{}
    import dsl.*
    def get: F[DataTypes.HeatDemand] =
      C.expect[HeatDemand](GET(uri"https://icanhazdadjoke.com/"))
        .adaptError{ case t => JokeError(t)} // Prevent Client Json Decoding Failure Leaking
*/