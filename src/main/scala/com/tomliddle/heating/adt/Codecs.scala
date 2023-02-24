package com.tomliddle.heating.adt

import com.tomliddle.heating.adt.DataTypes.Result
import io.circe.Decoder
import sttp.tapir.*
import sttp.tapir.json.circe.*
import sttp.tapir.generic.auto.*
import io.circe.generic.semiauto.*

object Codecs {

  given resultCodec: Decoder[Result] = deriveDecoder
}
