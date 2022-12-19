package weatherapi.routes

import cats.data.EitherT.fromEither
import cats.data._
import cats.effect.IO
import cats.implicits._
import io.circe.literal._
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import weatherapi.clients.WeatherServiceClientImpl
import weatherapi.domain.{Latitude, Longitude}
import weatherapi.routes.models.WeatherResponse
import weatherapi.routes.models.WeatherResponse._
import weatherapi.validation.Errors.DomainErrors
import weatherapi.validation.CoordinatesValidator

object WeatherRoutes {

  type IOResult[T] = EitherT[IO, Throwable, T]

  object LatitudeQueryParamMatcher extends QueryParamDecoderMatcher[Latitude]("latitude")
  object LongitudeQueryParamMatcher extends QueryParamDecoderMatcher[Longitude]("longitude")

  def routes(Client: WeatherServiceClientImpl): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO] {}
    import dsl._
    HttpRoutes.of[IO] {
      case _ @GET -> Root / "weather" :? LatitudeQueryParamMatcher(latitude) +& LongitudeQueryParamMatcher(longitude) =>
        // Typically wouldn't make these routes heavy with business-esque logic
        (for {
          coord <- fromEither[IO](
            CoordinatesValidator.validate(latitude, longitude).toEither.leftMap(err => DomainErrors(err.toList))
          )
          forecastUri <- EitherT(Client.forecastUri(coord.latitude, coord.longitude))
          forecasts <- EitherT(Client.forecasts(forecastUri.uri))
          // Ensure forecasts are sorted by indice and pluck head
          primaryForecast = forecasts.summaries.sortBy(_.index).head
          response <- WeatherResponse(primaryForecast.shortForecast, primaryForecast.temperature).pure[IOResult]
        } yield response)
          .foldF(
            l =>
              // Not a great error "serialiazer". Really just want to capture validated
              l match {
                case validation: DomainErrors =>
                  BadRequest(validation.asJson)
                case err: Throwable =>
                  BadRequest(json"""{"errors": [${err.getMessage}] }""")
              },
            r => Ok(r.asJson),
          )
          .handleErrorWith { err =>
            println(err)
            InternalServerError(err.getMessage)
          }
    }
  }

}
