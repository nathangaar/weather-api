package weatherapi.clients

import cats.effect.IO
import org.http4s.Uri
import weatherapi.domain.{Latitude, Longitude}

abstract class WeatherServiceClient {
  def forecastUri(latitude: Latitude, longitude: Longitude): IO[Either[Throwable, ForecastUri]]
  def forecasts(uri: Uri): IO[Either[Throwable, Forecasts]]
}
