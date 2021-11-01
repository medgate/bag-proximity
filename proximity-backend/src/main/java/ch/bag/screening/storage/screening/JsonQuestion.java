package ch.bag.screening.storage.screening;

import java.util.Locale;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonQuestion {
  private String id;
  private Map<Locale, String> title;
  private Map<Locale, String> description;
}
