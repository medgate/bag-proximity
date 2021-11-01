package ch.bag.screening.web.api.screening;

import ch.bag.screening.domain.node.SymptomSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
  private String id;
  private String title;
  private String description;
  private SymptomSeverity severity;
}
