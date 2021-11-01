import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { HttpBackend, HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ConversationSummary } from './conversation-summary.model';

@Injectable({
  providedIn: "root",
})
export class ConversationSummaryService {
  private resource = `${environment.apiBasePath}/recommendations`;
  private http: HttpClient;

  constructor(handler: HttpBackend) {
    this.http = new HttpClient(handler);
  }

  public getConversationSummary(
    recommendationCode: string,
    language: string
  ): Observable<ConversationSummary> {
    let resource = `${this.resource}/${recommendationCode}/${language}`;
    return this.http.get<ConversationSummary>(resource);
  }
}
