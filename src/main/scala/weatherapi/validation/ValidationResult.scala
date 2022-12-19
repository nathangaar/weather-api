package weatherapi.validation

import cats.data.ValidatedNec
import weatherapi.validation.Errors.DomainValidation

object ValidationResult {
  type ValidationResult[A] = ValidatedNec[DomainValidation, A]
}
