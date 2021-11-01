package ch.bag.screening.web.api.error;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
  private int status;
  private String reason;
  private String message;
  private String localizedMessage;
  private Instant occurredAt;
}
