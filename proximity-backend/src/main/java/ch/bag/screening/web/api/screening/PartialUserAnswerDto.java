package ch.bag.screening.web.api.screening;

import ch.bag.screening.web.api.profile.UserProfileDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialUserAnswerDto {
  private String version;
  private UserProfileDto profile;
  private List<String> nodeIds;
}
