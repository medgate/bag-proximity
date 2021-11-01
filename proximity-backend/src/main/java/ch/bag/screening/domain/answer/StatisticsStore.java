package ch.bag.screening.domain.answer;

import ch.bag.screening.domain.screening.Screening;
import java.util.List;

public interface StatisticsStore {
  Screening saveScreeningStatistics(CompletedUserAnswer userAnswer);

  Screening findScreeningByRecommendationCode(final String recommendationCode, final String lang);

  List<Exchange> findExchangeEntities(final Long screeningId);
}
