package weatherapi.routes.models

import weatherapi.domain.{Latitude, Longitude}

final case class Coordinates(latitude: Latitude, longitude: Longitude)
