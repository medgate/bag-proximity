package ch.bag.screening.storage.statistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "exchanges")
public class ExchangeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "ordering", nullable = false)
  private int ordering;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "question_id")
  private QuestionEntity question;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "answer_id")
  private AnswerEntity answer;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "screening_id")
  private ScreeningEntity screening;
}
