package ch.bag.screening.web.api.recommendation;

import static ch.bag.screening.web.api.answer.UserAnswerMapper.toExchangeDtos;
import static ch.bag.screening.web.api.profile.UserProfileMapper.toUserProfileDto;
import static ch.bag.screening.web.api.screening.ScreeningMapper.toRecommendationDto;

import ch.bag.screening.domain.recommendation.ConversationSummary;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConversationSummaryMapper {
  static ConversationSummaryDto toConversationSummaryDto(
      final ConversationSummary conversationSummary) {
    return ConversationSummaryDto.builder()
        .userProfile(toUserProfileDto(conversationSummary.getUserProfile()))
        .exchanges(toExchangeDtos(conversationSummary.getExchanges()))
        .recommendation(toRecommendationDto(conversationSummary.getRecommendation()))
        .build();
  }
}
