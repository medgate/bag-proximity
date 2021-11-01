package ch.bag.screening.web.api.answer;

import static ch.bag.screening.web.api.answer.UserAnswerMapper.toCompletedUserAnswer;
import static ch.bag.screening.web.api.answer.UserAnswerMapper.toCompletedUserAnswerDto;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import ch.bag.screening.domain.answer.CompletedUserAnswer;
import ch.bag.screening.domain.answer.UserAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/v1/screening/answers", produces = APPLICATION_JSON_VALUE)
public class UserAnswerController {

  private final UserAnswerService userAnswerService;

  @PostMapping(path = "/completed", consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public CompletedUserAnswerDto postUserCompletedScreeningAnswers(
      @RequestBody final CompletedUserAnswerDto answerDto) {
    final CompletedUserAnswer userAnswer = toCompletedUserAnswer(answerDto);
    final CompletedUserAnswer savedAnswer = userAnswerService.saveUserScreeningAnswers(userAnswer);
    return toCompletedUserAnswerDto(savedAnswer);
  }
}
