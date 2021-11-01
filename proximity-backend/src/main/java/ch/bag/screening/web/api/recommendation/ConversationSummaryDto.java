package ch.bag.screening.web.api.recommendation;

import ch.bag.screening.web.api.answer.ExchangeDto;
import ch.bag.screening.web.api.profile.UserProfileDto;
import ch.bag.screening.web.api.screening.RecommendationDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummaryDto {
  private UserProfileDto userProfile;
  private List<ExchangeDto> exchanges;
  private RecommendationDto recommendation;
}
