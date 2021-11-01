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
public class JsonNode {
  private String id;
  private String question;
  private List<JsonAnswerNode> answers;
}
