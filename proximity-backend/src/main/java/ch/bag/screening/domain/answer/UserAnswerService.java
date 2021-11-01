package ch.bag.screening.domain.answer;

import static ch.bag.screening.domain.answer.UserAnswerValidator.validateCompletedUserAnswer;

import ch.bag.screening.domain.screening.Screening;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserAnswerService {

  private final Clock clock;
  private final StatisticsStore statisticsStore;

  public CompletedUserAnswer saveUserScreeningAnswers(final CompletedUserAnswer userAnswer) {
    validateCompletedUserAnswer(userAnswer);
    final CompletedUserAnswer answer = userAnswer.withSubmittedAt(now());
    final Screening screening = statisticsStore.saveScreeningStatistics(answer);
    final CompletedUserAnswer savedAnswer =
        answer.withRecommendationCode(screening.getRecommendation().getRecommendationCode());
    logProvidedRecommendation(savedAnswer);
    return savedAnswer;
  }

  private static void logProvidedRecommendation(final CompletedUserAnswer userAnswer) {
    LOG.info(
        "Provided user recommendation for tree {} is {} with recommendation code {}",
        userAnswer.getVersion(),
        userAnswer.getRecommendationId(),
        userAnswer.getRecommendationCode());
  }

  private Instant now() {
    return clock.instant();
  }
}
