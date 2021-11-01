package ch.bag.screening.web.api.screening;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NodeDto {
  private String questionId;
  private String question;
  private String description;
  private List<AnswerDto> answers;
}
