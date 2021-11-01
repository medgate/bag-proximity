package ch.bag.screening.storage.statistics;

import static ch.bag.screening.domain.error.NotFoundException.notFound;
import static ch.bag.screening.domain.recommendation.ConversationSummaryError.RECOMMENDATION_CODE_NOT_FOUND;
import static ch.bag.screening.storage.statistics.StatisticsSqlMapper.toScreening;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.size;
import static org.apache.commons.lang3.StringUtils.isBlank;

import ch.bag.screening.domain.answer.CompletedUserAnswer;
import ch.bag.screening.domain.answer.Exchange;
import ch.bag.screening.domain.answer.StatisticsStore;
import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.AnswerId;
import ch.bag.screening.domain.node.Node;
import ch.bag.screening.domain.node.QuestionId;
import ch.bag.screening.domain.node.SymptomSeverity;
import ch.bag.screening.domain.screening.Screening;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Slf4j
public class StatisticsSqlStore implements StatisticsStore {

  private final int RECOMMENDATION_CODE_LENGTH = 4;
  private final int RECOMMENDATION_CODE_MAX_ATTEMPTS = 10;

  private final QuestionSqlRepository questionSqlRepository;
  private final AnswerSqlRepository answerSqlRepository;
  private final RecommendationSqlRepository recommendationSqlRepository;
  private final ScreeningSqlRepository screeningSqlRepository;
  private final ExchangeSqlRepository exchangeSqlRepository;

  @Override
  @Transactional
  public Screening saveScreeningStatistics(final CompletedUserAnswer userAnswer) {
    // Store the initial screening session for the user without exchanges
    final ScreeningEntity screening = screeningSqlRepository.save(buildScreeningEntity(userAnswer));
    // Map and store exchanges for the screening session
    final Map<QuestionId, QuestionEntity> questions = findQuestionsMap(userAnswer);
    final Map<AnswerId, AnswerEntity> answers = findAnswersMap(userAnswer);
    screening.setExchanges(buildExchangeEntities(userAnswer, screening, questions, answers));
    screening.setContactDates(
        buildContactDateEntities(userAnswer.getProfile().getContactDates(), screening));
    final ScreeningEntity savedScreeningContactDates = screeningSqlRepository.save(screening);
    return toScreening(savedScreeningContactDates);
  }

  private static Set<ContactEntity> buildContactDateEntities(
      final List<Instant> contactDates, final ScreeningEntity screening) {
    return emptyIfNull(contactDates).stream()
        .filter(Objects::nonNull)
        .map(contactDate -> buildContactDateEntity(contactDate, screening))
        .collect(Collectors.toSet());
  }

  private static ContactEntity buildContactDateEntity(
      final Instant contactDate, final ScreeningEntity screening) {
    return ContactEntity.builder().contactDate(contactDate).screening(screening).build();
  }

  private ScreeningEntity buildScreeningEntity(final CompletedUserAnswer userAnswer) {
    return ScreeningEntity.builder()
        .recommendationCode(generateRandomRecommendationCode(userAnswer))
        .version(userAnswer.getVersion())
        .age(userAnswer.getProfile().getAge())
        .gender(userAnswer.getProfile().getGender())
        .canton(userAnswer.getProfile().getCanton().getCode())
        .language(userAnswer.getProfile().getLocale().getLanguage())
        .submittedAt(userAnswer.getSubmittedAt())
        .recommendation(findRecommendation(userAnswer))
        .build();
  }

  private String generateRandomRecommendationCode(final CompletedUserAnswer userAnswer) {
    final SymptomSeverity recommendationSeverity =
        SymptomSeverity.valueOf(findRecommendation(userAnswer).getSeverity());

    // Only HIGH severity indicates a call to the hotline must be made
    if (!SymptomSeverity.HIGH.equals(recommendationSeverity)) {
      return null;
    }

    int attempts = 0;
    while (attempts < RECOMMENDATION_CODE_MAX_ATTEMPTS) {
      final String recommendationCode =
          RandomStringUtils.randomAlphanumeric(RECOMMENDATION_CODE_LENGTH).toUpperCase();
      if (!screeningSqlRepository.existsByRecommendationCode(recommendationCode)) {
        return recommendationCode;
      }
      attempts++;
    }

    LOG.warn("Unable to find a unique recommendation code in {} attempts", attempts);
    return null;
  }

  private RecommendationEntity findRecommendation(final CompletedUserAnswer userAnswer) {
    return recommendationSqlRepository
        .findByVersionAndName(userAnswer.getVersion(), userAnswer.getRecommendationId().get())
        .orElse(null);
  }

  private Map<QuestionId, QuestionEntity> findQuestionsMap(final CompletedUserAnswer userAnswer) {
    return questionSqlRepository
        .findAllByVersionAndNameIn(
            userAnswer.getVersion(), toQuestionIds(userAnswer.getExchanges()))
        .stream()
        .collect(toMap(question -> QuestionId.fromString(question.getName()), identity()));
  }

  private Map<AnswerId, AnswerEntity> findAnswersMap(final CompletedUserAnswer userAnswer) {
    return answerSqlRepository
        .findAllByVersionAndNameIn(userAnswer.getVersion(), toAnswerIds(userAnswer.getExchanges()))
        .stream()
        .collect(toMap(answer -> AnswerId.fromString(answer.getName()), identity()));
  }

  private static List<String> toQuestionIds(final List<Exchange> exchanges) {
    return emptyIfNull(exchanges).stream()
        .map(exchange -> exchange.getQuestion().getQuestionId().toString())
        .collect(toList());
  }

  private static List<String> toAnswerIds(final List<Exchange> exchanges) {
    return emptyIfNull(exchanges).stream()
        .map(exchange -> exchange.getAnswer().getId().toString())
        .collect(toList());
  }

  private static List<ExchangeEntity> buildExchangeEntities(
      final CompletedUserAnswer userAnswer,
      final ScreeningEntity screening,
      final Map<QuestionId, QuestionEntity> questions,
      final Map<AnswerId, AnswerEntity> answers) {
    final List<ExchangeEntity> exchanges = new ArrayList<>();
    for (int i = 0; i < size(userAnswer.getExchanges()); i++) {
      final Exchange exchange = userAnswer.getExchanges().get(i);
      final QuestionEntity question = questions.get(exchange.getQuestion().getQuestionId());
      final AnswerEntity answer = answers.get(exchange.getAnswer().getId());
      exchanges.add(buildExchangeEntity(i, screening, question, answer));
    }
    return exchanges;
  }

  private static ExchangeEntity buildExchangeEntity(
      final int ordering,
      final ScreeningEntity screening,
      final QuestionEntity question,
      final AnswerEntity answer) {
    return ExchangeEntity.builder()
        .ordering(ordering)
        .question(question)
        .answer(answer)
        .screening(screening)
        .build();
  }

  @Override
  public Screening findScreeningByRecommendationCode(
      final String recommendationCode, final String lang) {
    if (isBlank(recommendationCode)) {
      return null;
    }

    return toScreening(
        screeningSqlRepository
            .findByRecommendationCode(recommendationCode)
            .orElseThrow(notFound(RECOMMENDATION_CODE_NOT_FOUND)));
  }

  @Override
  public List<Exchange> findExchangeEntities(final Long screeningId) {
    final List<ExchangeEntity> exchangeEntities =
        exchangeSqlRepository.findAllByScreeningId(screeningId);

    exchangeEntities.sort(
        (ExchangeEntity e1, ExchangeEntity e2) -> e1.getOrdering() < e2.getOrdering() ? -1 : 1);

    return exchangeEntities.stream().map(StatisticsSqlStore::toExchange).collect(toList());
  }

  private static Exchange toExchange(final ExchangeEntity exchangeEntity) {
    return Exchange.builder()
        .question(
            Node.builder()
                .questionId(QuestionId.fromString(exchangeEntity.getQuestion().getName()))
                .build())
        .answer(
            Answer.builder().id(AnswerId.fromString(exchangeEntity.getAnswer().getName())).build())
        .build();
  }
}
