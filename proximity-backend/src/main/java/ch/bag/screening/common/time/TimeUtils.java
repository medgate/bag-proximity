package ch.bag.screening.common.time;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class TimeUtils {

  public static final DateTimeFormatter SWISS_DATE_FORMATTER =
      swissDateFormatter("yyyy-MM-dd HH:mm:ss");

  public static final DateTimeFormatter SWISS_DATE_FORMATTER_FILE_NAME =
      swissDateFormatter("yyyy_MM_dd_HHmmss");

  public static DateTimeFormatter swissDateFormatter(final String pattern) {
    return DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.of("Europe/Zurich"))
        .withLocale(Locale.ENGLISH);
  }

  public static String toSwissFormattedDateTime(
      final Instant date, final DateTimeFormatter dateTimeFormatter) {
    if (date == null) {
      return EMPTY;
    }
    return dateTimeFormatter.format(date);
  }
}
