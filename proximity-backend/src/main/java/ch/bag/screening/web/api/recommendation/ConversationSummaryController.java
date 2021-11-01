package ch.bag.screening.web.api.recommendation;

import static ch.bag.screening.web.api.recommendation.ConversationSummaryMapper.toConversationSummaryDto;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ch.bag.screening.domain.recommendation.ConversationSummary;
import ch.bag.screening.domain.screening.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/", produces = APPLICATION_JSON_VALUE)
public class ConversationSummaryController {

  private final ScreeningService screeningService;

  @GetMapping("/recommendations/{recommendationCode}/{lang}")
  public ConversationSummaryDto getSummary(
      @PathVariable final String recommendationCode, @PathVariable final String lang) {
    final ConversationSummary conversationSummary =
        screeningService.buildConversationSummary(recommendationCode, lang);
    return toConversationSummaryDto(conversationSummary);
  }
}
