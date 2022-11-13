import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { HomeAuthResolver } from '../../home-auth-resolver.service';

export const routes: Routes = [
    {
        path: '',
        loadChildren: () =>
            import('./components/customer-results/customer-results.module').then(
                (m) => m.CustomerResultsModule
            ),
        resolve: {
            isAuthed: HomeAuthResolver,
        },
    },
    {
        path: 'detail',
        loadChildren: () =>
            import('./components/customer-details/customer-details.module').then(
                (m) => m.CustomerDetailsModule
            ),
        resolve: {
            isAuthed: HomeAuthResolver,
        },
    },
];

export const CustomerSearchRoutes: ModuleWithProviders = RouterModule.forChild(
    routes
);
