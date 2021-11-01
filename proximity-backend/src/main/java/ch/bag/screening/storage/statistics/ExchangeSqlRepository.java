package ch.bag.screening.storage.statistics;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeSqlRepository extends JpaRepository<ExchangeEntity, Integer> {
  List<ExchangeEntity> findAllByScreeningId(Long screeningId);
}
