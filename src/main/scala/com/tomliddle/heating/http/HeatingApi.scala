package com.tomliddle.heating.http

import com.tomliddle.heating.adt.DataTypes.{Result, ResultError}
import sttp.tapir.{Endpoint, endpoint, stringToPath}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody
import com.tomliddle.heating.adt.Codecs.*

object HeatingApi:

  val baseEndpoint = endpoint

  val setTemperatureEndpoint: Endpoint[Unit, (Double, Double), ResultError, Result, Any] = baseEndpoint
    .get
    .in("temp" / "set" / path[Double] / path[Double])
    .out(jsonBody[Result])
    .errorOut(jsonBody[ResultError])
