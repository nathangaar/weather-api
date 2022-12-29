package weatherapi.routes

import cats.effect._
import org.http4s.Status.{BadRequest, Ok}
import org.http4s._
import org.http4s.implicits.http4sLiteralsSyntax
import org.mockito.Mockito.{reset, when}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import weatherapi.clients.{ForecastUri, Forecasts, Summary, WeatherServiceClientImpl}
import cats.effect.unsafe.implicits.global

import java.nio.charset.StandardCharsets

class WeatherRoutesSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  val clientMock = mock[WeatherServiceClientImpl]
  val httpRoutes: HttpRoutes[IO] = WeatherRoutes.routes(clientMock)

  reset(clientMock)

  "GET -> /weather" should "respond with Bad Request when using invalid coordinates" in {
    val request = org.http4s.Request[IO](Method.GET, uri"/weather?latitude=-10000.00&longitude=100000.0000")
    for {
      response <- httpRoutes(request).value.unsafeRunSync()
    } yield {
      val jsonBody = new String(response.body.compile.toVector.unsafeRunSync().toArray, StandardCharsets.UTF_8)
      jsonBody shouldBe """{"errors":["Latitude must be between -90.0 and 90.0","Longitude must be between -180.0 and 180"]}"""
      response.status shouldBe BadRequest
    }
  }

  "GET -> /weather" should "respond with a shortforecast " in {
    val weatherUri = uri"http//weather.gov/cast"
    when(
      clientMock.forecasts(weatherUri)
    ).thenReturn(
      IO(Right[Throwable, Forecasts](Forecasts(Seq(Summary(1, "pensively realistic", 20)))))
    )
    when(
      clientMock.forecastUri(10.0, 1.0)
    ).thenReturn(
      IO(Right[Throwable, ForecastUri](ForecastUri(weatherUri)))
    )
    val request = org.http4s.Request[IO](Method.GET, uri"/weather?latitude=10.00&longitude=1.0")

    for {
      response <- httpRoutes(request).value.unsafeRunSync()
    } yield {
      val jsonBody = new String(response.body.compile.toVector.unsafeRunSync().toArray, StandardCharsets.UTF_8)
      jsonBody shouldBe """{"short_forecast":"pensively realistic","temperature":"cold"}"""
      response.status shouldBe Ok
    }
  }
}
