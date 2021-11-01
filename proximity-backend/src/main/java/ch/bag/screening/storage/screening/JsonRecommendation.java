package ch.bag.screening.storage.screening;

import ch.bag.screening.domain.node.SymptomSeverity;
import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JsonRecommendation {
  private String id;
  private SymptomSeverity severity;
  private Map<Locale, String> title;
  private Map<Locale, String> description;
  private Map<Locale, String> cantonalDescription;
}
