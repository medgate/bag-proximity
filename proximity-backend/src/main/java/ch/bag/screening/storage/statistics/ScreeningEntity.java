package ch.bag.screening.storage.statistics;

import ch.bag.screening.domain.profile.Gender;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "screenings")
public class ScreeningEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "recommendation_code")
  private String recommendationCode;

  @Column(name = "version", nullable = false)
  private String version;

  @Column(name = "age", nullable = false)
  private int age;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(name = "language", nullable = false)
  private String language;

  @Column(name = "canton", nullable = false)
  private String canton;

  @Column(name = "submitted_at", nullable = false)
  private Instant submittedAt;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "recommendation_id")
  private RecommendationEntity recommendation;

  @OneToMany(mappedBy = "screening", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy("contactDate ASC")
  @EqualsAndHashCode.Exclude
  private Set<ContactEntity> contactDates;

  @OneToMany(mappedBy = "screening", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @OrderBy("ordering ASC")
  @EqualsAndHashCode.Exclude
  private List<ExchangeEntity> exchanges;
}
