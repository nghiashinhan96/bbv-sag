import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CustomerDetailsComponent } from './customer-details.component';
import { HomeAuthResolver } from 'src/app/home/home-auth-resolver.service';

export const routes: Routes = [
  {
    path: '',
    component: CustomerDetailsComponent,
    resolve: {
      isAuthed: HomeAuthResolver,
    },
  },
];

export const CustomerDetailRoutes: ModuleWithProviders = RouterModule.forChild(
  routes
);
