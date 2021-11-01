package ch.bag.screening.web.api.screening;

import static ch.bag.screening.web.api.profile.UserProfileMapper.toUserProfile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.Node;
import ch.bag.screening.domain.node.NodeId;
import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.node.RecommendationId;
import ch.bag.screening.domain.screening.InitialQuestion;
import ch.bag.screening.domain.screening.PartialUserAnswer;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public final class ScreeningMapper {

  static InitialQuestionDto toInitialQuestionDto(final InitialQuestion response) {
    return InitialQuestionDto.builder()
        .version(response.getVersion())
        .initialNodeId(response.getInitialNodeId().toString())
        .node(toNodeDto(response.getNode()))
        .build();
  }

  public static NodeDto toNodeDto(final Node node) {
    return NodeDto.builder()
        .questionId(node.getQuestionId().toString())
        .question(node.getQuestion())
        .description(node.getDescription())
        .answers(toAnswerDtos(node.getAnswers()))
        .build();
  }

  private static List<AnswerDto> toAnswerDtos(final List<Answer> answers) {
    return emptyIfNull(answers).stream().map(ScreeningMapper::toAnswerDto).collect(toList());
  }

  public static AnswerDto toAnswerDto(final Answer answer) {
    return AnswerDto.builder()
        .id(answer.getId().toString())
        .type(answer.getType())
        .text(answer.getText())
        .nextNodeId(answer.getNextNodeId().toString())
        .recommendation(toRecommendationDto(answer.getRecommendation()))
        .build();
  }

  public static RecommendationDto toRecommendationDto(final Recommendation recommendation) {
    if (recommendation == null) {
      return null;
    }
    return RecommendationDto.builder()
        .id(recommendation.getId().toString())
        .title(recommendation.getTitle())
        .description(recommendation.getDescription())
        .severity(recommendation.getSeverity())
        .build();
  }

  public static Recommendation toRecommendation(final RecommendationDto recommendation) {
    if (recommendation == null) {
      return null;
    }
    return Recommendation.builder()
        .id(RecommendationId.fromString(recommendation.getId()))
        .title(recommendation.getTitle())
        .description(recommendation.getDescription())
        .severity(recommendation.getSeverity())
        .build();
  }

  static PartialUserAnswer toPartialUserAnswer(final PartialUserAnswerDto answer) {
    return PartialUserAnswer.builder()
        .version(trimToEmpty(answer.getVersion()))
        .profile(toUserProfile(answer.getProfile()))
        .nodeIds(toNodesIds(answer.getNodeIds()))
        .build();
  }

  private static List<NodeId> toNodesIds(final List<String> nodeIds) {
    return emptyIfNull(nodeIds).stream()
        .filter(StringUtils::isNotBlank)
        .map(NodeId::fromString)
        .collect(toList());
  }
}
