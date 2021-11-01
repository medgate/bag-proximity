package ch.bag.screening.storage.screening;

import ch.bag.screening.domain.node.AnswerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonAnswerNode {
  private AnswerType type;
  private String text;
  private String node;
  private String recommendation;
}
