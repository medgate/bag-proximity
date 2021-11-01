package ch.bag.screening.domain.node;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Node {
  private QuestionId questionId;
  private String question;
  private String description;
  private List<Answer> answers;
}
