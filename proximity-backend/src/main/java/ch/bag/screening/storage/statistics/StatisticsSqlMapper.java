package ch.bag.screening.storage.statistics;

import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.node.RecommendationId;
import ch.bag.screening.domain.node.SymptomSeverity;
import ch.bag.screening.domain.profile.SwissCanton;
import ch.bag.screening.domain.profile.UserProfile;
import ch.bag.screening.domain.screening.Screening;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class StatisticsSqlMapper {
  public static Screening toScreening(final ScreeningEntity screening) {
    final UserProfile userProfile =
        UserProfile.builder()
            .age(screening.getAge())
            .gender(screening.getGender())
            .canton(SwissCanton.fromCode(screening.getCanton()))
            .contactDates(toContactDates(screening.getContactDates()))
            .locale(Locale.forLanguageTag(screening.getLanguage()))
            .build();

    final Recommendation recommendation =
        Recommendation.builder()
            .recommendationCode(screening.getRecommendationCode())
            .id(RecommendationId.fromString(screening.getRecommendation().getName()))
            .severity(SymptomSeverity.valueOf(screening.getRecommendation().getSeverity()))
            .build();
    return Screening.builder()
        .id(screening.getId())
        .version(screening.getVersion())
        .userProfile(userProfile)
        .recommendation(recommendation)
        .submittedAt(screening.getSubmittedAt())
        .build();
  }

  private static List<Instant> toContactDates(final Set<ContactEntity> contactDates) {
    return contactDates.stream().map(ContactEntity::getContactDate).collect(Collectors.toList());
  }
}
