package ch.bag.screening.domain.profile;

import java.time.Instant;
import java.util.List;
import java.util.Locale;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserProfile {
  private final int age;
  private final Gender gender;
  private final SwissCanton canton;
  private final List<Instant> contactDates;
  private final Locale locale;
}
