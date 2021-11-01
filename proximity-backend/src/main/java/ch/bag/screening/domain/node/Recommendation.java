package ch.bag.screening.domain.node;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Recommendation {
  private final RecommendationId id;
  private final String title;
  private final String description;
  private final SymptomSeverity severity;
  private final String recommendationCode;
}
