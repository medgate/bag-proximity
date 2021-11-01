package ch.bag.screening.domain.error;

import java.util.function.Supplier;

public class ForbiddenException extends DomainException {

  public ForbiddenException(final LocalizedError localizedError, final Object... args) {
    super(localizedError, args);
  }

  public static Supplier<ForbiddenException> forbidden(
      final LocalizedError localizedError, final Object... args) {
    return () -> new ForbiddenException(localizedError, args);
  }
}
