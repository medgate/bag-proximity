package ch.bag.screening.domain.error;

import java.util.function.Supplier;

public class ValidationException extends DomainException {

  public ValidationException(final LocalizedError localizedError, final Object... args) {
    super(localizedError, args);
  }

  public static Supplier<ValidationException> invalidRequest(
      final LocalizedError localizedError, final Object... args) {
    return () -> new ValidationException(localizedError, args);
  }
}
