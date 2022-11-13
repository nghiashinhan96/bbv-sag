import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SagAuthenGuard } from 'sag-auth';

const routes: Routes = [
  {
    path: '',
    canActivate: [SagAuthenGuard],
    canActivateChild: [SagAuthenGuard],
    loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
