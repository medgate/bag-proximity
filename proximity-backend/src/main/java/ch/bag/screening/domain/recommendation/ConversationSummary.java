package ch.bag.screening.domain.recommendation;

import ch.bag.screening.domain.answer.Exchange;
import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.profile.UserProfile;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ConversationSummary {
  private final UserProfile userProfile;
  private final List<Exchange> exchanges;
  private final Recommendation recommendation;
}
