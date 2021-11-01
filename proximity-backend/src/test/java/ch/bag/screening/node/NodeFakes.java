package ch.bag.screening.node;

import static ch.bag.screening.domain.node.SymptomSeverity.LOW;
import static ch.bag.screening.domain.node.SymptomSeverity.MEDIUM;

import ch.bag.screening.domain.node.Answer;
import ch.bag.screening.domain.node.AnswerId;
import ch.bag.screening.domain.node.AnswerType;
import ch.bag.screening.domain.node.Node;
import ch.bag.screening.domain.node.NodeId;
import ch.bag.screening.domain.node.Recommendation;
import ch.bag.screening.domain.node.SymptomSeverity;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class NodeFakes {

  public static Node fakeRecommendationNode() {
    return Node.builder()
        .question("This is the latest question...")
        .description("No matter the question, our recommendations will be shown next")
        .answers(fakeRecommendationAnswers())
        .build();
  }

  public static Node fakeQuestionNode(final String question) {
    return Node.builder()
        .question(question)
        .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.")
        .answers(fakeAnswers())
        .build();
  }

  private static List<Answer> fakeRecommendationAnswers() {
    return List.of(
        recommendationNode("Yes", fakeRecommendation("Medium risk", MEDIUM)),
        recommendationNode("No", fakeRecommendation("Low risk", LOW)));
  }

  private static List<Answer> fakeAnswers() {
    return List.of(
        answerNode("Yes", NodeId.of("n1")),
        answerNode("No", NodeId.of("n1")),
        answerNode("I don't know", NodeId.of("n1")));
  }

  private static Recommendation fakeRecommendation(
      final String title, final SymptomSeverity severity) {
    return Recommendation.builder()
        .title(title)
        .description("Lorem ipsum dolor sit amet, blabla bla.")
        .severity(severity)
        .build();
  }

  private static Answer answerNode(final String text, final NodeId nextNodeId) {
    return Answer.builder()
        .id(AnswerId.empty())
        .type(AnswerType.NODE)
        .text(text)
        .nextNodeId(nextNodeId)
        .build();
  }

  private static Answer recommendationNode(final String text, final Recommendation recommendation) {
    return Answer.builder()
        .id(AnswerId.empty())
        .type(AnswerType.RECOMMENDATION)
        .text(text)
        .nextNodeId(NodeId.empty())
        .recommendation(recommendation)
        .build();
  }
}
