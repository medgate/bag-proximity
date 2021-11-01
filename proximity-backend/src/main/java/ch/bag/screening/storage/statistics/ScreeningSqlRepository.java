package ch.bag.screening.storage.statistics;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningSqlRepository extends JpaRepository<ScreeningEntity, Long> {
  boolean existsByRecommendationCode(String recommendationCode);

  Optional<ScreeningEntity> findByRecommendationCode(final String recommendationCode);
}
