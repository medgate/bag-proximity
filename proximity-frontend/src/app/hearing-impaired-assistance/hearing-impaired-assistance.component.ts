import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { RedirectService } from '../core/redirect/redirect.service';
import { RecommendationCodeService } from '../recommendation/recommendation-code.service';

@Component({
  selector: 'bag-hearing-impaired-assistance',
  templateUrl: './hearing-impaired-assistance.component.html',
  styleUrls: ['./hearing-impaired-assistance.component.scss']
})
export class HearingImpairedAssistanceComponent implements OnInit {

  public contactEmail: string
  public  recommendationCode: string;

  constructor(
    private readonly redirectService: RedirectService,
    private readonly translate: TranslateService,
    private readonly recommendationCodeService: RecommendationCodeService
  ) {
  }
  
  ngOnInit(): void {
    this.setContactEmail();
    this.recommendationCode = this.recommendationCodeService.getCurrentRecommendationCode();
    console.log("recommendation code: " + this.recommendationCode);
  }

  private setContactEmail(): void {
    this.translate.get('hearingImpaired.email').subscribe((translated: string) => {
      this.contactEmail = this.translate.instant('hearingImpaired.email');
    });
  }

  public redirectBagWebsite() {
    this.redirectService.redirectBagWebsite(this.translate.getDefaultLang());
  }
}
