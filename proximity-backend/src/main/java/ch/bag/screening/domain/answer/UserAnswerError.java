package ch.bag.screening.domain.answer;

import ch.bag.screening.domain.error.LocalizedError;

public enum UserAnswerError implements LocalizedError {
  EMPTY_SCREENING_VERSION("Screening version is mandatory"),
  EMPTY_SCREENING_EXCHANGES("Screening question and answer exchanges must not be empty"),
  EMPTY_SCREENING_PROFILE("Screening profile is mandatory"),
  INVALID_SCREENING_AGE("Screening age must be between 0 and %s"),
  EMPTY_SCREENING_PROFILE_GENDER("User gender is mandatory"),
  EMPTY_SCREENING_PROFILE_CANTON("User Swiss canton is mandatory"),
  INVALID_CONTACT_DATES("User contact dates must be recent and not in the future"),
  EMPTY_SCREENING_RECOMMENDATION("Screening recommendation is mandatory"),
  EXPIRED_SCREENING("Screening must be under %d days old");

  /** Default english message. Can contain format tokens for replacements (e.g. %s). */
  private final String defaultMessage;

  UserAnswerError(final String defaultMessage) {
    this.defaultMessage = defaultMessage;
  }

  @Override
  public String getDefaultMessage() {
    return defaultMessage;
  }

  @Override
  public String getLocalizedMessage() {
    return name();
  }
}
