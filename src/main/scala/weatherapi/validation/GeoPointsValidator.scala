package weatherapi.validation

import cats.implicits.{catsSyntaxTuple2Semigroupal, catsSyntaxValidatedIdBinCompat0}
import weatherapi.domain.{Latitude, Longitude}
import weatherapi.routes.models.GeoPoints
import weatherapi.validation.Errors.{LatitudeInvalid, LongitudeInvalid}

object GeoPointsValidator {
  import ValidationResult._

  private def validateLatitude(latitude: Latitude): ValidationResult[Latitude] =
    if (latitude > -90.0 && latitude < 90.0) latitude.validNec else LatitudeInvalid.invalidNec

  private def validateLongitude(longitude: Longitude): ValidationResult[Longitude] =
    if (longitude > -180.0 && longitude < 180.0) longitude.validNec else LongitudeInvalid.invalidNec

  def validate(latitude: Latitude, longitude: Longitude): ValidationResult[GeoPoints] =
    (validateLatitude(latitude), validateLongitude(longitude)).mapN(GeoPoints)
}
