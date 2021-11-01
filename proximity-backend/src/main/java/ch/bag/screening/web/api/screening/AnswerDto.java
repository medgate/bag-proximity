package ch.bag.screening.web.api.screening;

import ch.bag.screening.domain.node.AnswerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {
  private String id;
  private AnswerType type;
  private String text;
  private String nextNodeId;
  private RecommendationDto recommendation;
}
