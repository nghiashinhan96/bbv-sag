import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { HomeAuthResolver } from '../../home-auth-resolver.service';
import { LicenseSearchResultsComponent } from './components/license-result/license-search-result.component';

export const routes: Routes = [
  {
    path: '',
    component: LicenseSearchResultsComponent,
    resolve: {
      isAuthed: HomeAuthResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class LicenseSearchRoutes {}