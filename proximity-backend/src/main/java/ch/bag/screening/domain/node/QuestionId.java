package ch.bag.screening.domain.node;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public final class QuestionId {

  private final String id;

  public static QuestionId empty() {
    return QuestionId.of(EMPTY);
  }

  public static QuestionId fromString(final String id) {
    return QuestionId.of(trimToEmpty(id));
  }

  public String get() {
    return id;
  }

  @Override
  public String toString() {
    return id;
  }
}
