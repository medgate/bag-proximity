package ch.bag.screening.domain.node;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
public final class RecommendationId {

  private final String id;

  public static RecommendationId empty() {
    return RecommendationId.of(EMPTY);
  }

  public static RecommendationId fromString(final String id) {
    return RecommendationId.of(trimToEmpty(id));
  }

  public boolean isEmpty() {
    return isBlank(id);
  }

  public String get() {
    return id;
  }

  @Override
  public String toString() {
    return id;
  }
}
