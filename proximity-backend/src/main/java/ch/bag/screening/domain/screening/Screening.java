package ch.bag.screening.domain.screening;

import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.profile.UserProfile;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Screening {
  private Long id;
  private Recommendation recommendation;
  private String version;
  private UserProfile userProfile;
  private Instant submittedAt;
}
