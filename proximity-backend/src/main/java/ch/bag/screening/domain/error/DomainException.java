package ch.bag.screening.domain.error;

public abstract class DomainException extends RuntimeException {

  private final LocalizedError localizedError;

  DomainException(
      final Throwable cause, final LocalizedError localizedError, final Object... args) {
    super(String.format(localizedError.getDefaultMessage(), args), cause);
    this.localizedError = localizedError;
  }

  DomainException(final LocalizedError localizedError, final Object... args) {
    super(String.format(localizedError.getDefaultMessage(), args));
    this.localizedError = localizedError;
  }

  @Override
  public String getLocalizedMessage() {
    return localizedError.getLocalizedMessage();
  }
}
