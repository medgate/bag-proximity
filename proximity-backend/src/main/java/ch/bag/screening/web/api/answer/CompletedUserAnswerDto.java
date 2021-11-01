package ch.bag.screening.web.api.answer;

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
public class CompletedUserAnswerDto {
  private String version;
  private UserProfileDto profile;
  private List<ExchangeDto> exchanges;
  private String recommendationId;
  private String recommendationCode;
}
