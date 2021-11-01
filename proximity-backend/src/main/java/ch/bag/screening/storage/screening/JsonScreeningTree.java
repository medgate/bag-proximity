package ch.bag.screening.storage.screening;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonScreeningTree {
  private List<JsonQuestion> questions;
  private List<JsonAnswer> answers;
  private List<JsonRecommendation> recommendations;
  private List<JsonNode> nodes;
  private List<JsonCantonalRecommendation> cantons;
  private List<String> cantonalRecommendationFor;
}
