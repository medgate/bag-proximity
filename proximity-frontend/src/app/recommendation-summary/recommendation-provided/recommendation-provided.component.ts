import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Severity } from '../../recommendation/severity.enum';
import { Recommendation } from "../../recommendation/recommendation.model";
import { TranslateService } from '@ngx-translate/core';
import { RedirectService } from '../../core/redirect/redirect.service';

@Component({
  selector: "bag-recommendation-provided",
  templateUrl: "./recommendation-provided.component.html",
  styleUrls: ["./recommendation-provided.component.scss"],
})
export class RecommendationProvidedComponent {
  @Input() public recommendation: Recommendation;
  @Output() public reset = new EventEmitter<boolean>();

  constructor(
    private readonly redirectService: RedirectService,
    private readonly translate: TranslateService
  ) {}

  public getSeverityClass(severity: Severity): string {
    switch (severity) {
      case Severity.HIGH:
        return "severity-high";
      case Severity.MEDIUM:
        return "severity-medium";
      case Severity.LOW:
        return "severity-low";
      default:
        return "severity-low";
    }
  }

  public redirectBagWebsite() {
    this.redirectService.redirectBagWebsite(this.translate.getDefaultLang());
  }

  public resetSummary(): void {
    this.reset.emit(true);
  }
}
