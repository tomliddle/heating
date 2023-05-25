package com.tomliddle.heating.adt

import com.tomliddle.heating.adt.DataTypes.{Result, ResultError}
import io.circe.Codec
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import io.circe.generic.semiauto.*

object Codecs {

  implicit val resultCodec: Codec[Result] = deriveCodec

  implicit val resultError: Codec[ResultError] = deriveCodec
}
