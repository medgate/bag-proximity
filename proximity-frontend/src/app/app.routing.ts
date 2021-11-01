import { Routes } from '@angular/router';
import { PageNotFoundComponent } from './core/errors/page-not-found/page-not-found.component';
import { ScreeningIntroComponent } from './screening/screening-intro/screening-intro.component';
import { ScreeningFormComponent } from './screening/screening-form/screening-form.component';
import { ProfileFormComponent } from './profile/profile-form/profile-form.component';
import { ConversationSummaryComponent } from './recommendation-summary/conversation-summary-component/conversation-summary.component';
import { HearingImpairedAssistanceComponent } from './hearing-impaired-assistance/hearing-impaired-assistance.component';

export const APP_ROUTES: Routes = [
  {
    path: '',
    redirectTo: 'screening', pathMatch: 'full'
  },
  {
    path: 'screening',
    children: [
      {
        path: '',
        component: ScreeningIntroComponent
      },
      {
        path: 'profile',
        component: ProfileFormComponent
      },
      {
        path: 'questions',
        component: ScreeningFormComponent
      }
    ]
  },
  {
    path: 'recommendations',
    component: ConversationSummaryComponent,
  },
  {
    path: 'page-not-found',
    component: PageNotFoundComponent
  }, 
  {
    path: 'hearing-impaired-assistance',
    component: HearingImpairedAssistanceComponent
  }, 
  {
    path: '**',
    redirectTo: 'page-not-found',
    pathMatch: 'full'
  }
];
