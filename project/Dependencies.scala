import sbt._

object Dependencies {

  val Http4sVersion = "0.23.16"
  val CirceVersion = "0.14.3"
  val MunitVersion = "0.7.29"
  val LogbackVersion = "1.2.11"

  lazy val Config = Vector("com.typesafe" % "config" % "1.4.2")

  lazy val Http4s = Vector(
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-ember-client" % Http4sVersion,
    "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  )

  lazy val Cats = Seq(
    "org.typelevel" %% "cats-effect" % "3.4.2"
  )

  lazy val Circe = Seq(
    "io.circe" %% "circe-generic" % CirceVersion,
    "io.circe" %% "circe-literal" % CirceVersion,
    "io.circe" %% "circe-parser" % CirceVersion,
  )

  lazy val Logging = Seq(
    "ch.qos.logback" % "logback-classic" % LogbackVersion % Runtime
  )

  lazy val Sttp = Seq(
    "com.softwaremill.sttp.client3" %% "core" % "3.8.5",
    "com.softwaremill.sttp.client3" %% "async-http-client-backend-cats" % "3.8.5",
  )

  lazy val Tests = Seq(
    "org.scalatest" %% "scalatest" % "3.2.9" % Test,
    "org.scalamock" %% "scalamock" % "5.1.0" % Test,
  )

  val Libraries =
    Http4s ++ Sttp ++ Circe ++ Logging ++ Tests ++ Cats ++ Config
}
