package ch.bag.screening.storage.statistics;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerSqlRepository extends JpaRepository<AnswerEntity, Integer> {
  List<AnswerEntity> findAllByVersionAndNameIn(String version, List<String> names);
}
