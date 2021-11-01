import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { Profile } from '../profile.model';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { ALL_SWISS_CANTONS, Canton } from '../canton.model';
import { ProfileService } from '../profile.service';
import { Router } from '@angular/router';
import { Language } from '../../core/languages/language.enum';


@Component({
  selector: "bag-profile-form",
  templateUrl: "./profile-form.component.html",
  styleUrls: ["./profile-form.component.scss"],
})
export class ProfileFormComponent implements OnInit {
  public profileForm: FormGroup;
  public cantons: Canton[] = [];
  public filteredCantonOptions: Observable<Canton[]>;
  public isIEOrEdge = /msie\s|trident\/|edge\//i.test(
    window.navigator.userAgent
  );
  public loading: boolean = false;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly profileService: ProfileService,
    private readonly router: Router
  ) { }

  public ngOnInit(): void {
    this.initializeProfile();
    this.sortCantonsByName();
    this.buildProfileForm();
    this.filterCantonAutoComplete();
  }

  private initializeProfile() {
    const contactDates = this.profileService.getCurrentProfile().contactDates;
    this.profileService.resetProfile({ contactDates });
    this.profileService.profileChanges.subscribe((profile: Profile) => {
      this.sortCantonsByName();
      const selectedCanton = this.cantons.find(
        (canton) => canton.id === this.profileForm.get("canton").value?.id
      );
      this.profileForm.patchValue({ canton: selectedCanton });
      this.profileForm.patchValue({ contactDates });
    });
  }

  private buildProfileForm() {
    this.profileForm = this.formBuilder.group({
      age: ["", [Validators.required, Validators.min(0), Validators.max(120)]],
      gender: ["", Validators.required],
      canton: ["", Validators.required],
      contactDates: [""],
      shareContactDates: ["", Validators.required]
    });
    this.profileForm.get("canton").setValidators([
      (control: AbstractControl): ValidationErrors | null => {
        const exists = ALL_SWISS_CANTONS.find(
          (canton) => canton.id === control.value?.id
        );
        return !exists ? { invalid: true } : null;
      },
    ]);
  }

  public checkAgeMaxLength(): boolean {
    return String(this.profileForm.get("age").value).length !== 3;
  }

  private sortCantonsByName(): void {
    this.cantons = ALL_SWISS_CANTONS.map((canton) =>
      this.toCanton(canton)
    ).sort((canton1: Canton, canton2: Canton) => {
      const name1 = this.removeAccents(canton1.name);
      const name2 = this.removeAccents(canton2.name);
      if (name1 > name2) {
        return 1;
      }
      if (name1 < name2) {
        return -1;
      }
      return 0;
    });
  }

  private toCanton(canton: any): Canton {
    return {
      id: canton.id,
      name: canton.name[this.getCurrentLanguage()],
    } as Canton;
  }

  public getCurrentLanguage(): Language {
    return this.profileService.getCurrentProfile().language;
  }

  public filterCantonAutoComplete() {
    this.filteredCantonOptions = this.profileForm
      .get("canton")
      .valueChanges.pipe(
        startWith(""),
        map((value) => (typeof value === "string" ? value : value?.name)),
        map((value) => this.filterCantons(value))
      );
  }

  private filterCantons(userInput: string): Canton[] {
    if (!userInput) {
      return this.cantons;
    }
    const partialName = this.removeAccents(userInput);
    return this.cantons.filter((canton) =>
      this.removeAccents(canton.name).startsWith(partialName)
    );
  }

  public displayCanton(canton?: Canton): string | undefined {
    return canton ? canton.name : undefined;
  }

  private removeAccents(value: string): string {
    const accents =
      "ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëðÇçÐÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿýŽž";
    const accentsOut =
      "AAAAAAaaaaaaOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz";
    const str = value.split("");
    const strLen = str.length;
    for (let i = 0; i < strLen; i++) {
      let x = accents.indexOf(str[i]);
      if (x !== -1) {
        str[i] = accentsOut[x];
      }
    }
    return str.join("").toLowerCase();
  }

  public submitForm() {
    this.loading = true;
    this.profileService.changeProfile(this.buildProfile());
    this.router.navigateByUrl("/screening/questions");
  }

  private buildProfile(): Profile {
    const formValue = this.profileForm.value;
    return {
      gender: formValue.gender,
      age: formValue.age,
      canton: formValue.canton.id,
      contactDates: formValue.shareContactDates ? formValue.contactDates : [],
      language: this.getCurrentLanguage(),
    } as Profile;
  }

  public isNextButtonDisabled(): boolean {
    if (!this.profileService.getCurrentProfile().contactDates.length) {
      return !this.profileForm.get("gender").valid
          || !this.profileForm.get("age").valid 
          || !this.profileForm.get("canton").valid;
    }
    return !this.profileForm.valid || this.loading;
  }
}
