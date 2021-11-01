import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from 'src/app/profile/profile.service';

@Component({
  selector: 'bag-screening-intro',
  templateUrl: './screening-intro.component.html',
  styleUrls: ['./screening-intro.component.scss']
})
export class ScreeningIntroComponent implements OnInit {

  private MAX_CONTACT_DATES = 10;

  constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    private profileService: ProfileService) { }
  
  ngOnInit(): void {
    this.processURLParams();
  }

  private processURLParams(): void {
    this.activatedRoute.queryParams.subscribe(params => {
        const dates = params['contactDates'];
        if (dates) {
          const datesArray: Date[] = this.buildDatesArray(dates);
          this.profileService.setContactDates(datesArray);
          this.clearURLParams();
        }
    });
  }

  private buildDatesArray(dates: string): Date[] {
    let datesArray = dates.split(',').map(date => new Date(date));
    datesArray = datesArray.slice(0, this.MAX_CONTACT_DATES);
    datesArray = datesArray.filter(date => this.isValidDate(date));
    return datesArray;
  }

  private isValidDate(date: Date): boolean {
    return !isNaN(date.getTime());
  }

  private clearURLParams(): void {
    this.router.navigate(
      ['.'], 
      { relativeTo: this.activatedRoute, queryParams: {} }
    );
  }
}
