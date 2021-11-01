package ch.bag.screening.domain.error;

public class ExpiredException extends DomainException {

  public ExpiredException(final LocalizedError localizedError, final Object... args) {
    super(localizedError, args);
  }
}
