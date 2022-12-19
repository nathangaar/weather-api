package weatherapi.routes

import cats.effect._
import org.http4s.Status.BadRequest
import org.http4s._
import org.http4s.implicits.http4sLiteralsSyntax
import org.mockito.Mockito.{reset, when}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar
import weatherapi.clients.{ForecastUri, Forecasts, Summary, WeatherServiceClientImpl}
import cats.effect.unsafe.implicits.global

class WeatherRoutesSpec extends AnyFlatSpec with Matchers with MockitoSugar {

  val clientMock = mock[WeatherServiceClientImpl]
  val httpRoutes: HttpRoutes[IO] = WeatherRoutes.routes(clientMock)

  reset(clientMock)

  "GET -> /weather" should "should respond with Bad Request when using invalid coordinates" in {
    val weatherUri = uri"http//weather.gov/cast"
    when(
      clientMock.forecasts(weatherUri)
    ).thenReturn(
      IO(Right[Throwable, Forecasts](Forecasts(Seq(Summary(1, "cloudy", 20)))))
    )

    when(
      clientMock.forecastUri(90.0, 180.00)
    ).thenReturn(
      IO(Right[Throwable, ForecastUri](ForecastUri(weatherUri)))
    )
    val request = org.http4s.Request[IO](Method.GET, uri"/weather?latitude=0.00&longitude=00.0000")

    for {
      response <- httpRoutes(request).value.unsafeRunSync()
    } yield response.status shouldBe BadRequest
  }
}
