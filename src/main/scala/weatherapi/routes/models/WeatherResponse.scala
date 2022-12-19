package weatherapi.routes.models

import io.circe.{Encoder, Json}
import weatherapi.domain.{ShortForecast, Temperature}

final case class WeatherResponse(
    shortForecast: ShortForecast,
    temp: String,
)

object WeatherResponse {

  def apply(shortForecast: ShortForecast, temperature: Temperature): WeatherResponse = {
    val temp = temperature match {
      case t if t < 40             => "cold"
      case t if t >= 40 && t <= 80 => "moderate"
      case t if t > 80             => "warm"
    }
    WeatherResponse(shortForecast, temp)
  }

  implicit val encodeWeatherResponse: Encoder[WeatherResponse] = new Encoder[WeatherResponse] {
    final def apply(w: WeatherResponse): Json = Json.obj(
      ("short_forecast", Json.fromString(w.shortForecast)),
      ("temperature", Json.fromString(w.temp)),
    )
  }
}
