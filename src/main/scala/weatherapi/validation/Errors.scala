package weatherapi.validation

import scala.util.control.NoStackTrace
import io.circe.Encoder

object Errors {

  sealed abstract class DomainValidation extends NoStackTrace { def errorMessage: String }

  final case object LatitudeInvalid extends DomainValidation {
    val errorMessage: String = "Latitute must be between -90.0 and 90.0"
  }

  final case object LongitudeInvalid extends DomainValidation {
    val errorMessage: String = "Longitude must be between -180.0 and 180"
  }

  final case class DomainErrors(errors: Seq[DomainValidation]) extends NoStackTrace

  implicit val encodeValidation: Encoder[DomainValidation] =
    Encoder.encodeString.contramap[DomainValidation](_.errorMessage)

  implicit val encodeErrors: Encoder[DomainErrors] =
    Encoder.forProduct1("errors")(err => err.errors)
}
