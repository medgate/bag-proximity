import { Component, ElementRef, OnInit, ViewChild } from "@angular/core";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { ALL_SWISS_CANTONS, Canton } from 'src/app/profile/canton.model';
import { Gender } from 'src/app/profile/gender.enum';
import { ProfileService } from 'src/app/profile/profile.service';
import { ConversationSummary } from '../conversation-summary.model';
import { ConversationSummaryService } from '../conversation-summary.service';

@Component({
  selector: "conversation-summary",
  templateUrl: "./conversation-summary.component.html",
  styleUrls: ["./conversation-summary.component.scss"],
})
export class ConversationSummaryComponent implements OnInit {
  private RECOMMENDATION_CODE_LENGTH = 4;
  public gender = Gender;
  public summaryForm: FormGroup;
  public canton: Canton;
  public conversationSummary: ConversationSummary;
  public loading: boolean = false;
  public minContactDate: Date;
  public maxContactDate: Date;
  public error = false;
  public recommendationCodeNotFound = false;
  public recommendationExpired = false;

  constructor(
    private readonly screeningService: ConversationSummaryService,
    private readonly formBuilder: FormBuilder,
    private readonly profileService: ProfileService
  ) {}

  public ngOnInit(): void {
    this.updateSummaryOnProfileChange();
    this.buildSummaryForm();
  }

  private updateSummaryOnProfileChange(): void {
    this.profileService.profileChanges.subscribe(() => {
      this.updateSummaryIfValidCode();
    });
  }

  private buildSummaryForm() {
    this.summaryForm = this.formBuilder.group({
      recommendationCode: [
        "",
        [
          Validators.required,
          Validators.minLength(this.RECOMMENDATION_CODE_LENGTH),
          Validators.maxLength(this.RECOMMENDATION_CODE_LENGTH),
        ],
      ],
    });
  }

  private isValidRecommendationCode(recommendationCode: string): boolean {
    let specialChars = /[ `!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?~]/;
    return (
      recommendationCode != null &&
      recommendationCode.length === this.RECOMMENDATION_CODE_LENGTH &&
      !specialChars.test(recommendationCode)
    );
  }

  public updateSummaryIfValidCode(): void {
    this.conversationSummary = null;
    let recommendationCode = this.summaryForm.get("recommendationCode").value;
    if (this.isValidRecommendationCode(recommendationCode)) {
      this.updateConversationSummary(recommendationCode);
    }
  }

  @ViewChild("recommendationCode") recommendationCodeField: ElementRef;
  public resetSummary(): void {
    this.summaryForm.patchValue({ recommendationCode: "" });
    this.summaryForm.reset(this.summaryForm.value);
    this.conversationSummary = null;
    this.recommendationCodeField.nativeElement.focus();
  }

  private updateConversationSummary(recommendationCode: string): void {
    this.recommendationCodeNotFound = false;
    this.recommendationExpired = false;
    let lang = this.getCurrentLanguage();
    this.screeningService
      .getConversationSummary(recommendationCode, lang)
      .subscribe(
        (conversationSummary: ConversationSummary) => {
          this.conversationSummary = conversationSummary;
          this.canton = this.cantonFromId(
            conversationSummary.userProfile.canton
          );
          this.error = null;
          this.loading = false;
        },
        (error) => {
          if (error.status === 404) {
            this.recommendationCodeNotFound = true;
          } else if (error.status === 410) {
            this.recommendationExpired = true;
          } else {
            this.error = error;
            this.loading = false;
          }
        }
      );
  }

  private cantonFromId(cantonId: string): Canton {
    const cantons = ALL_SWISS_CANTONS.map((canton) => this.toCanton(canton));
    return cantons.find((canton) => canton.id === cantonId);
  }

  private getCurrentLanguage(): string {
    return this.profileService.getCurrentProfile().language;
  }

  private toCanton(canton: any): Canton {
    return {
      id: canton.id,
      name: canton.name[this.getCurrentLanguage()],
    } as Canton;
  }
}
