package ch.bag.screening.domain.language;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.ITALIAN;

import java.util.List;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Languages {

  public static final Locale DEFAULT_LOCALE = Locale.GERMAN;

  public static List<Locale> availableLocales() {
    return List.of(DEFAULT_LOCALE, FRENCH, ITALIAN, ENGLISH);
  }

  public static boolean isAvailableLocale(final Locale locale) {
    return locale != null && availableLocales().contains(locale);
  }
}
