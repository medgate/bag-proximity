package ch.bag.screening.storage.statistics;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationSqlRepository extends JpaRepository<RecommendationEntity, Integer> {
  Optional<RecommendationEntity> findByVersionAndName(String version, String name);
}
