import { Component, Input, OnInit } from "@angular/core";
import { Answer } from 'src/app/screening/answer.model';
import { QuestionNode } from 'src/app/screening/question-node.model';

@Component({
  selector: "bag-exchange",
  templateUrl: "./exchange.component.html",
  styleUrls: ["./exchange.component.scss"]
})
export class ExchangeComponent implements OnInit {
  @Input() public questionNode: QuestionNode;
  @Input() public answer: Answer;
  ngOnInit(): void {}

  public getSeverityClass(answerId: string): string {
    switch (answerId) {
      case "ANSWER_YES":
        return "answer-yes";
      case "ANSWER_IDK":
        return "answer-idk";
      case "ANSWER_NO":
        return "answer-no";
      default:
        return "answer-no";
    }
  }
}
