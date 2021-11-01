package ch.bag.screening.domain.answer;

import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_EXCHANGES;
import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_PROFILE;
import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_PROFILE_CANTON;
import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_PROFILE_GENDER;
import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_RECOMMENDATION;
import static ch.bag.screening.domain.answer.UserAnswerError.EMPTY_SCREENING_VERSION;
import static ch.bag.screening.domain.answer.UserAnswerError.INVALID_CONTACT_DATES;
import static ch.bag.screening.domain.answer.UserAnswerError.INVALID_SCREENING_AGE;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

import ch.bag.screening.domain.error.ValidationException;
import ch.bag.screening.domain.profile.UserProfile;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class UserAnswerValidator {

  private static final int MAX_PROFILE_AGE = 120; // years
  private static final int MIN_CONTACT_DATE = 30; // days

  static void validateCompletedUserAnswer(final CompletedUserAnswer userAnswer) {
    if (isBlank(userAnswer.getVersion())) {
      throw new ValidationException(EMPTY_SCREENING_VERSION);
    }
    if (isEmpty(userAnswer.getExchanges())) {
      throw new ValidationException(EMPTY_SCREENING_EXCHANGES);
    }
    if (userAnswer.getRecommendationId().isEmpty()) {
      throw new ValidationException(EMPTY_SCREENING_RECOMMENDATION);
    }
    validateProfile(userAnswer.getProfile());
  }

  public static void validateProfile(final UserProfile profile) {
    if (profile == null) {
      throw new ValidationException(EMPTY_SCREENING_PROFILE);
    }
    if (!isValidAge(profile.getAge())) {
      throw new ValidationException(INVALID_SCREENING_AGE, MAX_PROFILE_AGE);
    }
    if (profile.getGender() == null) {
      throw new ValidationException(EMPTY_SCREENING_PROFILE_GENDER);
    }
    if (profile.getCanton() == null) {
      throw new ValidationException(EMPTY_SCREENING_PROFILE_CANTON);
    }
    if (!isValidContactDates(profile.getContactDates())) {
      throw new ValidationException(INVALID_CONTACT_DATES);
    }
  }

  private static boolean isValidAge(final int age) {
    return age >= 0 && age <= MAX_PROFILE_AGE;
  }

  private static boolean isValidContactDates(final List<Instant> contactDates) {
    return emptyIfNull(contactDates).stream().allMatch(UserAnswerValidator::isValidContactDate);
  }

  private static boolean isValidContactDate(final Instant contactDate) {
    final Instant now = Instant.now();
    final Instant oldestAllowed = now.minus(MIN_CONTACT_DATE, ChronoUnit.DAYS);
    return contactDate.isBefore(now) || contactDate.isAfter(oldestAllowed);
  }
}
