package ch.bag.screening.domain.node;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Answer {
  private final AnswerId id;
  private final AnswerType type;
  private final String text;
  private final NodeId nextNodeId;
  private final Recommendation recommendation;
}
