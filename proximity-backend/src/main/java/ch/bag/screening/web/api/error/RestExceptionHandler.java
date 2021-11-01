package ch.bag.screening.web.api.error;

import static ch.bag.screening.web.api.error.ErrorEncoder.encode;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import ch.bag.screening.domain.error.ExpiredException;
import ch.bag.screening.domain.error.ForbiddenException;
import ch.bag.screening.domain.error.NotFoundException;
import ch.bag.screening.domain.error.ValidationException;
import java.time.Clock;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RequiredArgsConstructor
@Order(HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler {

  private final Clock clock;

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorDto> handleNotFoundException(final NotFoundException error) {
    return encode(NOT_FOUND, error, now());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorDto> handleForbiddenError(final ForbiddenException error) {
    return encode(FORBIDDEN, error, now());
  }

  @ExceptionHandler(ExpiredException.class)
  public ResponseEntity<ErrorDto> handleForbiddenError(final ExpiredException error) {
    return encode(GONE, error, now());
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorDto> handleValidationError(final ValidationException error) {
    return encode(BAD_REQUEST, error, now());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorDto> handleRequestError(final IllegalArgumentException error) {
    return encode(BAD_REQUEST, error, now());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public final ResponseEntity<ErrorDto> handleMethodNotAllowedError(
      final HttpRequestMethodNotSupportedException error, final WebRequest request) {
    LOG.error("{}: {}", error.getMessage(), request.getDescription(false));
    return encode(METHOD_NOT_ALLOWED, error, now());
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorDto> handleInternalServerError(final Exception error) {
    LOG.error("Unhandled server error", error);
    return encode(INTERNAL_SERVER_ERROR, error, now());
  }

  private Instant now() {
    return clock.instant();
  }
}
