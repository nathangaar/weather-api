package weatherapi.clients

import io.circe.{Decoder, HCursor}
import org.http4s.Uri
import org.http4s.Uri.unsafeFromString

final case class ForecastUri(uri: Uri)

object ForecastUri {

  implicit val encodeForeCastsUri: Decoder[ForecastUri] = new Decoder[ForecastUri] {
    final def apply(c: HCursor): Decoder.Result[ForecastUri] =
      for {
        uri <- c.downField("properties").downField("forecast").as[String]
      } yield ForecastUri(unsafeFromString(uri))
  }
}
