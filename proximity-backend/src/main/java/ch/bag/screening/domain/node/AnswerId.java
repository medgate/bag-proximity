package ch.bag.screening.domain.node;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public final class AnswerId {

  private final String id;

  public static AnswerId empty() {
    return AnswerId.of(EMPTY);
  }

  public static AnswerId fromString(final String id) {
    return AnswerId.of(trimToEmpty(id));
  }

  public String get() {
    return id;
  }

  @Override
  public String toString() {
    return id;
  }
}
