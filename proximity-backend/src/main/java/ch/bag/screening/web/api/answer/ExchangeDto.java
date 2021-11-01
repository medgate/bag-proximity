package ch.bag.screening.web.api.answer;

import ch.bag.screening.web.api.screening.AnswerDto;
import ch.bag.screening.web.api.screening.NodeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeDto {
  private NodeDto node;
  private AnswerDto answer;
}
