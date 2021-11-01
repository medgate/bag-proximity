package ch.bag.screening.web.api.profile;

import ch.bag.screening.domain.profile.Gender;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
  private int age;
  private Gender gender;
  private List<Instant> contactDates;
  private String canton;
  private String language;
}
