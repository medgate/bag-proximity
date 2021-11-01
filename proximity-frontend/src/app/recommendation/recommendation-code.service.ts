import { Injectable } from '@angular/core';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: "root",
})
export class RecommendationCodeService {
  constructor(
    private readonly storage: SessionStorageService,
  ) {
  }

  public getCurrentRecommendationCode(): string {
    return this.storage.retrieve("recommendationCode");
  }

  public setRecommendationCode(recommendationCode: string): void {
    this.storage.store("recommendationCode", recommendationCode);
  }
}
