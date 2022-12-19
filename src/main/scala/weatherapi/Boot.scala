package weatherapi

import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.Router
import weatherapi.routes.WeatherRoutes
import com.comcast.ip4s.{IpLiteralSyntax, Ipv4Address, Port}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import weatherapi.clients.WeatherServiceClientImpl
import com.typesafe.config.ConfigFactory

object Boot extends IOApp {
  val config = ConfigFactory.load()

  val host = config.getString("api.host")
  val port = config.getString("api.port")

  val weatherServiceClient = new WeatherServiceClientImpl()

  val HttpRoutes = Router[IO] {
    "/" -> WeatherRoutes.routes(weatherServiceClient)
  }.orNotFound

  val app = Logger.httpApp(true, true)(HttpRoutes)

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(Ipv4Address.fromString(host).getOrElse(ipv4"0.0.0.0"))
      .withPort(Port.fromString(port).getOrElse(port"8080"))
      .withHttpApp(app)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
