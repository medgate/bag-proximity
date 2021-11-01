package ch.bag.screening.storage.statistics;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSqlRepository extends JpaRepository<QuestionEntity, Integer> {
  List<QuestionEntity> findAllByVersionAndNameIn(String version, List<String> names);
}
