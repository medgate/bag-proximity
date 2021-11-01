package ch.bag.screening.domain.error;

import java.util.function.Supplier;

public class NotFoundException extends DomainException {

  public NotFoundException(final LocalizedError localizedError, final Object... args) {
    super(localizedError, args);
  }

  public static Supplier<NotFoundException> notFound(
      final LocalizedError localizedError, final Object... args) {
    return () -> new NotFoundException(localizedError, args);
  }
}
