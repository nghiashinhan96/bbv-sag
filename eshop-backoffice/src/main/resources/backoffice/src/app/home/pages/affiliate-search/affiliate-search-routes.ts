import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders, NgModule } from '@angular/core';

import { AffiliateResultsComponent } from './components/affiliate-results/affiliate-results.component';
import { HomeAuthResolver } from '../../home-auth-resolver.service';
import { AffiliateDetailsComponent } from './components/affiliate-details/affiliate-details.component';

export const routes: Routes = [
  {
    path: '',
    component: AffiliateResultsComponent,
    resolve: {
      isAuthed: HomeAuthResolver,
    },
  },
  {
    path: 'detail',
    component: AffiliateDetailsComponent,
    resolve: {
      isAuthed: HomeAuthResolver,
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AffiliateSearchRoutes {}
