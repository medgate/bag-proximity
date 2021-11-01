package ch.bag.screening.domain.screening;

import static ch.bag.screening.domain.answer.UserAnswerError.EXPIRED_SCREENING;
import static ch.bag.screening.domain.answer.UserAnswerValidator.validateProfile;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import ch.bag.screening.domain.answer.Exchange;
import ch.bag.screening.domain.answer.StatisticsStore;
import ch.bag.screening.domain.error.ExpiredException;
import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.AnswerId;
import ch.bag.screening.domain.node.AnswerType;
import ch.bag.screening.domain.node.Node;
import ch.bag.screening.domain.node.NodeId;
import ch.bag.screening.domain.node.QuestionId;
import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.node.RecommendationId;
import ch.bag.screening.domain.profile.SwissCanton;
import ch.bag.screening.domain.profile.UserProfile;
import ch.bag.screening.domain.recommendation.ConversationSummary;
import ch.bag.screening.storage.screening.JsonAnswer;
import ch.bag.screening.storage.screening.JsonAnswerNode;
import ch.bag.screening.storage.screening.JsonCantonalRecommendation;
import ch.bag.screening.storage.screening.JsonNode;
import ch.bag.screening.storage.screening.JsonQuestion;
import ch.bag.screening.storage.screening.JsonRecommendation;
import ch.bag.screening.storage.screening.JsonScreeningTree;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ScreeningService {

  private static final int MAX_SCREENING_HOURS_FOR_SUMMARY = 24;

  private static final String LATEST_SCREENING_VERSION = "v1";

  private final ScreeningParser screeningParser;
  private final StatisticsStore statisticsStore;

  public InitialQuestion findInitialQuestion(final UserProfile userProfile) {
    validateProfile(userProfile);
    final String version = findScreeningVersion();
    final JsonScreeningTree screeningTree = findScreeningTree(version);
    final JsonNode firstNode = screeningTree.getNodes().get(0);
    final JsonQuestion question = findQuestionById(screeningTree, firstNode.getQuestion());
    return toInitialQuestion(firstNode, question, screeningTree, userProfile);
  }

  private static String findScreeningVersion() {
    return LATEST_SCREENING_VERSION;
  }

  private JsonScreeningTree findScreeningTree(final String version) {
    return screeningParser.readScreeningDecisionTree(version);
  }

  private static JsonQuestion findQuestionById(
      final JsonScreeningTree screeningTree, final String questionId) {
    return screeningTree.getQuestions().stream()
        .filter(question -> question.getId().equals(questionId))
        .findFirst()
        .orElse(null);
  }

  public Node answerScreeningQuestion(final PartialUserAnswer answer) {
    final JsonScreeningTree screeningTree = findScreeningTree(answer.getVersion());
    final NodeId lastNodeId = answer.getNodeIds().get(answer.getNodeIds().size() - 1);
    final JsonNode lastNode = findNodeById(screeningTree, lastNodeId);
    final JsonQuestion question = findQuestionById(screeningTree, lastNode.getQuestion());
    final SwissCanton swissCanton = answer.getProfile().getCanton();
    return toNode(lastNode, question, screeningTree, answer.getProfile(), swissCanton);
  }

  private static JsonNode findNodeById(final JsonScreeningTree screeningTree, final NodeId nodeId) {
    return screeningTree.getNodes().stream()
        .filter(node -> node.getId().equals(nodeId.get()))
        .findFirst()
        .orElse(null);
  }

  private static InitialQuestion toInitialQuestion(
      final JsonNode firstNode,
      final JsonQuestion question,
      final JsonScreeningTree jsonScreeningTree,
      final UserProfile userProfile) {
    return InitialQuestion.builder()
        .version(findScreeningVersion())
        .initialNodeId(NodeId.of(firstNode.getId()))
        // No Canton needed for initial question: by default BE
        .node(toNode(firstNode, question, jsonScreeningTree, userProfile, userProfile.getCanton()))
        .build();
  }

  private static Node toNode(
      final JsonNode jsonNode,
      final JsonQuestion jsonQuestion,
      final JsonScreeningTree jsonScreeningTree,
      final UserProfile userProfile,
      final SwissCanton swissCanton) {
    if (jsonQuestion == null) {
      return null;
    }
    return Node.builder()
        .questionId(QuestionId.of(jsonQuestion.getId()))
        .question(jsonQuestion.getTitle().getOrDefault(userProfile.getLocale(), EMPTY))
        .description(jsonQuestion.getDescription().getOrDefault(userProfile.getLocale(), EMPTY))
        .answers(toAnswers(jsonNode.getAnswers(), jsonScreeningTree, userProfile, swissCanton))
        .build();
  }

  private static List<Answer> toAnswers(
      final List<JsonAnswerNode> jsonAnswerNodes,
      final JsonScreeningTree jsonScreeningTree,
      final UserProfile userProfile,
      final SwissCanton swissCanton) {
    return emptyIfNull(jsonAnswerNodes).stream()
        .map(
            jsonAnswerNode -> toAnswer(jsonAnswerNode, jsonScreeningTree, userProfile, swissCanton))
        .collect(toList());
  }

  private static Answer toAnswer(
      final JsonAnswerNode jsonAnswerNode,
      final JsonScreeningTree jsonScreeningTree,
      final UserProfile userProfile,
      final SwissCanton swissCanton) {
    return Answer.builder()
        .id(AnswerId.of(jsonAnswerNode.getText()))
        .type(jsonAnswerNode.getType())
        .text(toAnswerText(jsonAnswerNode, jsonScreeningTree, userProfile.getLocale()))
        .nextNodeId(NodeId.fromString(jsonAnswerNode.getNode()))
        .recommendation(
            toRecommendation(
                jsonAnswerNode, jsonScreeningTree, userProfile.getLocale(), swissCanton))
        .build();
  }

  private static String toAnswerText(
      final JsonAnswerNode jsonAnswerNode,
      final JsonScreeningTree jsonScreeningTree,
      final Locale userLocale) {
    return findAnswer(jsonScreeningTree.getAnswers(), jsonAnswerNode.getText())
        .map(jsonAnswer -> jsonAnswer.getText().getOrDefault(userLocale, EMPTY))
        .orElse(EMPTY);
  }

  private static Optional<JsonAnswer> findAnswer(
      final List<JsonAnswer> jsonAnswers, final String answerId) {
    return emptyIfNull(jsonAnswers).stream()
        .filter(jsonAnswer -> jsonAnswer.getId().equals(answerId))
        .findFirst();
  }

  private static Recommendation toRecommendation(
      final JsonAnswerNode jsonAnswerNode,
      final JsonScreeningTree jsonScreeningTree,
      final Locale locale,
      final SwissCanton swissCanton) {
    return findRecommendationById(
        jsonScreeningTree.getRecommendations(),
        jsonScreeningTree.getCantons(),
        jsonScreeningTree.getCantonalRecommendationFor(),
        jsonAnswerNode.getRecommendation(),
        locale,
        swissCanton);
  }

  private static Recommendation findRecommendationById(
      final List<JsonRecommendation> jsonRecommendations,
      final List<JsonCantonalRecommendation> jsonCantonalRecommendations,
      final List<String> cantonalRecommendationFor,
      final String recommendationId,
      final Locale locale,
      final SwissCanton swissCanton) {
    if (isBlank(recommendationId)) {
      return null;
    }

    return emptyIfNull(jsonRecommendations).stream()
        .filter(jsonRecommendation -> jsonRecommendation.getId().equals(recommendationId))
        .findFirst()
        .map(
            jsonRecommendation ->
                toRecommendation(
                    jsonRecommendation,
                    jsonCantonalRecommendations,
                    cantonalRecommendationFor,
                    locale,
                    swissCanton))
        .orElse(null);
  }

  private static Recommendation toRecommendation(
      final JsonRecommendation jsonRecommendation,
      final List<JsonCantonalRecommendation> jsonCantonalRecommendations,
      final List<String> cantonalRecommendationFor,
      final Locale locale,
      final SwissCanton swissCanton) {

    final String description =
        getRecommendationDescription(
            jsonRecommendation,
            jsonCantonalRecommendations,
            cantonalRecommendationFor,
            swissCanton,
            locale);

    return Recommendation.builder()
        .id(RecommendationId.of(jsonRecommendation.getId()))
        .title(toTranslatedString(jsonRecommendation.getTitle(), locale))
        .description(description)
        .severity(jsonRecommendation.getSeverity())
        .build();
  }

  private static String getRecommendationDescription(
      final JsonRecommendation jsonRecommendation,
      final List<JsonCantonalRecommendation> jsonCantonalRecommendations,
      final List<String> cantonalRecommendationFor,
      final SwissCanton swissCanton,
      final Locale locale) {
    final String federalDescription =
        toTranslatedString(jsonRecommendation.getDescription(), locale);
    final String cantonalDescription =
        toTranslatedString(jsonRecommendation.getCantonalDescription(), locale);

    final String cantonalRecommendation =
        getCantonalRecommendation(
            jsonRecommendation,
            jsonCantonalRecommendations,
            cantonalRecommendationFor,
            swissCanton,
            locale);

    final StringBuilder description = new StringBuilder();
    if (!cantonalRecommendation.isEmpty() && !cantonalDescription.isEmpty()) {
      description.append(cantonalDescription);
    } else {
      description.append(federalDescription);
    }

    if (!cantonalRecommendation.isEmpty()) {
      description.append("<br><br>");
      description.append(cantonalRecommendation);
    }

    return description.toString();
  }

  private static String getCantonalRecommendation(
      final JsonRecommendation jsonRecommendation,
      final List<JsonCantonalRecommendation> jsonCantonalRecommendations,
      final List<String> cantonalRecommendationFor,
      final SwissCanton swissCanton,
      final Locale locale) {
    if (cantonalRecommendationFor == null) {
      return EMPTY;
    } else if (cantonalRecommendationFor.contains(jsonRecommendation.getId())) {
      final JsonCantonalRecommendation jsonCantonalRecommendation =
          jsonCantonalRecommendations.stream()
              .filter(canton -> canton.getId().equals(swissCanton.getCode()))
              .findAny()
              .orElse(null);
      if (jsonCantonalRecommendation != null) {
        return toTranslatedString(jsonCantonalRecommendation.getDescription(), locale);
      }
    }
    return EMPTY;
  }

  private static String toTranslatedString(
      final Map<Locale, String> localizedText, final Locale userLocale) {
    if (MapUtils.isEmpty(localizedText)) {
      return EMPTY;
    }
    return localizedText.getOrDefault(userLocale, EMPTY);
  }

  public ConversationSummary buildConversationSummary(
      final String recommendationCode, final String lang) {
    final Screening screening =
        statisticsStore.findScreeningByRecommendationCode(recommendationCode, lang);

    if (isScreeningExpired(screening.getSubmittedAt())) {
      throw new ExpiredException(EXPIRED_SCREENING, MAX_SCREENING_HOURS_FOR_SUMMARY);
    }

    final Locale locale = Locale.forLanguageTag(lang);

    final String version = screening.getVersion();
    final JsonScreeningTree jsonScreeningTree = findScreeningTree(version);

    final UserProfile userProfile = screening.getUserProfile();

    final List<Exchange> exchanges = statisticsStore.findExchangeEntities(screening.getId());
    final List<Exchange> populatedExchanges =
        populateExchanges(jsonScreeningTree, exchanges, locale);

    final Recommendation recommendation =
        findRecommendationById(
            jsonScreeningTree.getRecommendations(),
            jsonScreeningTree.getCantons(),
            jsonScreeningTree.getCantonalRecommendationFor(),
            screening.getRecommendation().getId().get(),
            locale,
            userProfile.getCanton());

    return buildConversationSummary(populatedExchanges, recommendation, userProfile);
  }

  private static boolean isScreeningExpired(final Instant submittedAt) {
    return Instant.now().isAfter(submittedAt.plus(MAX_SCREENING_HOURS_FOR_SUMMARY, HOURS));
  }

  private ConversationSummary buildConversationSummary(
      final List<Exchange> exchanges,
      final Recommendation recommendation,
      final UserProfile userProfile) {
    if (exchanges == null || recommendation == null || userProfile == null) {
      return null;
    }

    return ConversationSummary.builder()
        .userProfile(userProfile)
        .exchanges(exchanges)
        .recommendation(recommendation)
        .build();
  }

  private List<Exchange> populateExchanges(
      final JsonScreeningTree jsonScreeningTree,
      final List<Exchange> exchanges,
      final Locale locale) {
    return emptyIfNull(exchanges).stream()
        .map(exchange -> populateExchange(jsonScreeningTree, exchange, locale))
        .collect(toList());
  }

  private Exchange populateExchange(
      final JsonScreeningTree jsonScreeningTree, final Exchange exchange, final Locale locale) {

    final JsonQuestion jsonQuestion =
        findQuestionById(jsonScreeningTree, exchange.getQuestion().getQuestionId().get());
    final Node node =
        Node.builder()
            .questionId(QuestionId.of(jsonQuestion.getId()))
            .question(jsonQuestion.getTitle().getOrDefault(locale, EMPTY))
            .description(jsonQuestion.getDescription().getOrDefault(locale, EMPTY))
            .answers(emptyList())
            .build();

    final JsonAnswer jsonAnswer =
        findAnswer(jsonScreeningTree.getAnswers(), exchange.getAnswer().getId().get()).orElse(null);
    if (jsonAnswer == null) {
      return null;
    }
    final Answer answer =
        Answer.builder()
            .text(jsonAnswer.getText().getOrDefault(locale, EMPTY))
            .id(AnswerId.fromString(jsonAnswer.getId()))
            .nextNodeId(NodeId.empty())
            .recommendation(null)
            .type(AnswerType.NODE)
            .build();

    return Exchange.builder().question(node).answer(answer).build();
  }
}
