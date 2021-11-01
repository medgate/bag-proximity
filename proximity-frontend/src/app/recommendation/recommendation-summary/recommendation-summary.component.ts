import { Component, Input, OnInit } from '@angular/core';
import { Severity } from '../severity.enum';
import { Recommendation } from '../recommendation.model';
import { TranslateService } from '@ngx-translate/core';
import { RedirectService } from '../../core/redirect/redirect.service';
import { RecommendationCodeService } from '../recommendation-code.service';

@Component({
  selector: 'bag-recommendation-summary',
  templateUrl: './recommendation-summary.component.html',
  styleUrls: ['./recommendation-summary.component.scss']
})
export class RecommendationSummaryComponent implements OnInit {

  @Input() public recommendation: Recommendation;
  @Input() public recommendationCode: string;

  constructor(
    private readonly redirectService: RedirectService,
    private readonly translate: TranslateService,
    private readonly recommendationCodeService: RecommendationCodeService
  ) {
  }

  ngOnInit(): void {
    this.recommendationCodeService.setRecommendationCode(this.recommendationCode);
  }

  public getSeverityClass(severity: Severity): string {
    switch (severity) {
      case Severity.HIGH:
        return 'severity-high';
      case Severity.MEDIUM:
        return 'severity-medium';
      case Severity.LOW:
        return 'severity-low';
      default:
        return 'severity-low';
    }
  }

  public redirectBagWebsite() {
    this.redirectService.redirectBagWebsite(this.translate.getDefaultLang());
  }
}
