package ch.bag.screening.domain.answer;

import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.Node;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Exchange {
  private final Node question;
  private final Answer answer;
}
