package ch.bag.screening.web.api.profile;

import static ch.bag.screening.domain.language.Languages.DEFAULT_LOCALE;
import static ch.bag.screening.domain.language.Languages.isAvailableLocale;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import ch.bag.screening.domain.profile.SwissCanton;
import ch.bag.screening.domain.profile.UserProfile;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserProfileMapper {

  private static final int MAX_CONTACT_DATES = 10;

  public static UserProfile toUserProfile(final UserProfileDto profile) {
    if (profile == null) {
      return null;
    }
    return UserProfile.builder()
        .age(profile.getAge())
        .gender(profile.getGender())
        .contactDates(trimDatesLength(profile.getContactDates()))
        .canton(SwissCanton.fromCode(profile.getCanton()))
        .locale(toUserLocale(profile.getLanguage()))
        .build();
  }

  private static List<Instant> trimDatesLength(final List<Instant> contactDates) {
    if (contactDates.size() > MAX_CONTACT_DATES) {
      return contactDates.subList(0, MAX_CONTACT_DATES);
    }
    return contactDates;
  }

  public static UserProfileDto toUserProfileDto(final UserProfile profile) {
    if (profile == null) {
      return null;
    }
    return UserProfileDto.builder()
        .age(profile.getAge())
        .gender(profile.getGender())
        .contactDates(profile.getContactDates())
        .canton(profile.getCanton().getCode())
        .language(profile.getLocale().getLanguage())
        .build();
  }

  public static Locale toUserLocale(final String language) {
    final Locale locale = Locale.forLanguageTag(trimToEmpty(lowerCase(language)));
    return isAvailableLocale(locale) ? locale : DEFAULT_LOCALE;
  }
}
