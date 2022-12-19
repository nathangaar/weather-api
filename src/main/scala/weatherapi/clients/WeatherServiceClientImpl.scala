package weatherapi.clients

import cats.effect._
import org.http4s.client.Client
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.implicits.http4sLiteralsSyntax
import org.http4s.{Header, Uri}
import org.http4s.dsl.io.{GET, Path}
import org.typelevel.ci.CIStringSyntax
import org.http4s.client.dsl.io._
import io.circe.parser._
import weatherapi.domain.{Latitude, Longitude}

class WeatherServiceClientImpl(
    httpClient: Resource[IO, Client[IO]] = EmberClientBuilder.default[IO].build
) extends WeatherServiceClient {
  import Forecasts._
  import ForcecastUri._

  val UserAgent = Header.Raw(ci"User-Agent", "ndt")

  // Retrieve forcecast uri
  override def forecastUri(latitude: Latitude, longitude: Longitude): IO[Either[Throwable, ForecastUri]] = {
    val request = GET(
      uri"https://api.weather.gov"
        .withPath(Path.unsafeFromString(s"points/$latitude,$longitude"))
    ).withHeaders(UserAgent)

    httpClient
      .use { client =>
        client
          .expect[String](request)
          .map(decode[ForecastUri])
      }
  }

  override def forecasts(uri: Uri): IO[Either[Throwable, Forecasts]] = {
    val request = GET(
      uri
    ).withHeaders(UserAgent)

    httpClient
      .use { client =>
        client
          .expect[String](request)
          .map(decode[Forecasts])
      }
  }
}
