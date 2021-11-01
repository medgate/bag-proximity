package ch.bag.screening.domain.screening;

import ch.bag.screening.domain.node.NodeId;
import ch.bag.screening.domain.profile.UserProfile;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class PartialUserAnswer {
  private final String version;
  private final UserProfile profile;
  private final List<NodeId> nodeIds;
  private final Instant submittedAt;
}
