package ch.bag.screening.storage.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answers")
public class AnswerEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "version", nullable = false)
  private String version;

  @Column(name = "text_de", columnDefinition = "nvarchar", nullable = false)
  private String textDe;

  @Column(name = "text_fr", columnDefinition = "nvarchar", nullable = false)
  private String textFr;

  @Column(name = "text_it", columnDefinition = "nvarchar", nullable = false)
  private String textIt;

  @Column(name = "text_en", columnDefinition = "nvarchar", nullable = false)
  private String textEn;
}
