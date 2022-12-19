package weatherapi.routes.models

import weatherapi.domain.{Latitude, Longitude}

final case class GeoPoints(latitude: Latitude, longitude: Longitude)
