package weatherapi.clients

import io.circe.{Decoder, HCursor}
import weatherapi.domain.{Index, ShortForecast, Temperature}

final case class Forecasts(summaries: Seq[Summary])

final case class Summary(
    index: Index,
    shortForecast: ShortForecast,
    temperature: Temperature,
)
object Forecasts {

  implicit val encodeSummary: Decoder[Summary] = new Decoder[Summary] {
    final def apply(c: HCursor): Decoder.Result[Summary] =
      for {
        number <- c.downField("number").as[Int]
        shortForecast <- c.downField("shortForecast").as[String]
        temperature <- c.downField("temperature").as[Int]
      } yield Summary(number, shortForecast, temperature)
  }

  implicit val encodeForeCast: Decoder[Forecasts] = new Decoder[Forecasts] {
    final def apply(c: HCursor): Decoder.Result[Forecasts] =
      for {
        summaries <- c.downField("properties").downField("periods").as[Seq[Summary]]
      } yield Forecasts(summaries)
  }
}
