package ch.bag.screening.web.api.error;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.time.Instant;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@UtilityClass
class ErrorEncoder {

  static ResponseEntity<ErrorDto> encode(
      final HttpStatus httpStatus, final Throwable error, final Instant occurredAt) {
    return ResponseEntity.status(httpStatus).body(encodeError(httpStatus, error, occurredAt));
  }

  private static ErrorDto encodeError(
      final HttpStatus httpStatus, final Throwable error, final Instant occurredAt) {
    return ErrorDto.builder()
        .status(httpStatus.value())
        .reason(httpStatus.getReasonPhrase())
        .message(error.getMessage())
        .localizedMessage(trimToEmpty(error.getLocalizedMessage()))
        .occurredAt(occurredAt)
        .build();
  }
}
