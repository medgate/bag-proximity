package ch.bag.screening.storage.screening;

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
public class JsonCantonalRecommendation {
  private String id;
  private Map<Locale, String> description;
}
