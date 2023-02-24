package com.tomliddle.heating.http

import com.tomliddle.heating.TemperatureService.Result
import sttp.tapir.{Endpoint, endpoint, stringToPath}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

object HeatingApi:

  val baseEndpoint = endpoint

  val setTemperatureEndpoint: Endpoint[Unit, Double, Unit, Result, Any] = baseEndpoint
    .get
    .in("temp" / "set" / path[Double])
  .out(jsonBody[Result])
    .errorOut()
