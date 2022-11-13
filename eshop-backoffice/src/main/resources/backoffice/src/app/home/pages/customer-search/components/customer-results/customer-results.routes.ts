import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CustomerResultsComponent } from './customer-results.component';
import { HomeAuthResolver } from 'src/app/home/home-auth-resolver.service';

export const routes: Routes = [
    {
        path: '',
        component: CustomerResultsComponent,
        resolve: {
            isAuthed: HomeAuthResolver,
        },
    },
];

export const CustomerResultsRoutes: ModuleWithProviders = RouterModule.forChild(
    routes
);
