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
@Table(name = "questions")
public class QuestionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "version", nullable = false)
  private String version;

  @Column(name = "title_de", columnDefinition = "nvarchar", nullable = false)
  private String titleDe;

  @Column(name = "title_fr", columnDefinition = "nvarchar", nullable = false)
  private String titleFr;

  @Column(name = "title_it", columnDefinition = "nvarchar", nullable = false)
  private String titleIt;

  @Column(name = "title_en", columnDefinition = "nvarchar", nullable = false)
  private String titleEn;

  @Column(name = "description_de", columnDefinition = "ntext", nullable = false)
  private String descriptionDe;

  @Column(name = "description_fr", columnDefinition = "ntext", nullable = false)
  private String descriptionFr;

  @Column(name = "description_it", columnDefinition = "ntext", nullable = false)
  private String descriptionIt;

  @Column(name = "description_en", columnDefinition = "ntext", nullable = false)
  private String descriptionEn;
}
