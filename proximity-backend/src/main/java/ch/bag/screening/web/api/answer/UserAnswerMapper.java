package ch.bag.screening.web.api.answer;

import static ch.bag.screening.web.api.profile.UserProfileMapper.toUserProfile;
import static ch.bag.screening.web.api.profile.UserProfileMapper.toUserProfileDto;
import static ch.bag.screening.web.api.screening.ScreeningMapper.toAnswerDto;
import static ch.bag.screening.web.api.screening.ScreeningMapper.toNodeDto;
import static ch.bag.screening.web.api.screening.ScreeningMapper.toRecommendation;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import ch.bag.screening.domain.answer.CompletedUserAnswer;
import ch.bag.screening.domain.answer.Exchange;
import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.AnswerId;
import ch.bag.screening.domain.node.Node;
import ch.bag.screening.domain.node.NodeId;
import ch.bag.screening.domain.node.QuestionId;
import ch.bag.screening.domain.node.RecommendationId;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserAnswerMapper {

  static CompletedUserAnswer toCompletedUserAnswer(final CompletedUserAnswerDto answer) {
    return CompletedUserAnswer.builder()
        .version(trimToEmpty(answer.getVersion()))
        .profile(toUserProfile(answer.getProfile()))
        .exchanges(toExchanges(answer.getExchanges()))
        .recommendationId(RecommendationId.fromString(answer.getRecommendationId()))
        .build();
  }

  static CompletedUserAnswerDto toCompletedUserAnswerDto(final CompletedUserAnswer answer) {
    return CompletedUserAnswerDto.builder()
        .recommendationCode(answer.getRecommendationCode())
        .version(trimToEmpty(answer.getVersion()))
        .profile(toUserProfileDto(answer.getProfile()))
        .exchanges(toExchangeDtos(answer.getExchanges()))
        .recommendationId(answer.getRecommendationId().get())
        .build();
  }

  private static List<Exchange> toExchanges(final List<ExchangeDto> exchanges) {
    return emptyIfNull(exchanges).stream()
        .filter(Objects::nonNull)
        .map(UserAnswerMapper::toExchange)
        .collect(toList());
  }

  private static Exchange toExchange(final ExchangeDto exchange) {
    final Node question =
        Node.builder()
            .description(exchange.getNode().getDescription())
            .answers(emptyList())
            .question(exchange.getNode().getQuestion())
            .questionId(QuestionId.fromString(exchange.getNode().getQuestionId()))
            .build();
    final Answer answer =
        Answer.builder()
            .type(exchange.getAnswer().getType())
            .recommendation(toRecommendation(exchange.getAnswer().getRecommendation()))
            .nextNodeId(NodeId.empty())
            .id(AnswerId.fromString(exchange.getAnswer().getId()))
            .text(exchange.getAnswer().getText())
            .build();
    return Exchange.builder().question(question).answer(answer).build();
  }

  public static List<ExchangeDto> toExchangeDtos(final List<Exchange> exchanges) {
    return emptyIfNull(exchanges).stream()
        .filter(Objects::nonNull)
        .map(UserAnswerMapper::toExchangeDto)
        .collect(toList());
  }

  private static ExchangeDto toExchangeDto(final Exchange exchange) {
    return ExchangeDto.builder()
        .node(toNodeDto(exchange.getQuestion()))
        .answer(toAnswerDto(exchange.getAnswer()))
        .build();
  }
}
