package ch.bag.screening.domain.recommendation;

import ch.bag.screening.domain.error.LocalizedError;

public enum ConversationSummaryError implements LocalizedError {
  RECOMMENDATION_CODE_NOT_FOUND("Recommendation code not found");

  /** Default english message. Can contain format tokens for replacements (e.g. %s). */
  private final String defaultMessage;

  ConversationSummaryError(final String defaultMessage) {
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
