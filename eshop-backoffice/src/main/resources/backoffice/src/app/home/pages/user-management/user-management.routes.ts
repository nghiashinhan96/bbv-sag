import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { UserManagementComponent } from './user-management.component';
import { UserDetailComponent } from './components/user-detail/user-detail.component';
import { CanDeactivateGuard } from 'src/app/core/services/deactive-guard.service';
import { HomeAuthResolver } from '../../home-auth-resolver.service';

export const routes: Routes = [
    {
        path: '',
        component: UserManagementComponent,
        resolve: {
            isAuthed: HomeAuthResolver,
        },
    },
    {
        path: 'detail',
        component: UserDetailComponent,
        resolve: {
            isAuthed: HomeAuthResolver,
        },
        canDeactivate: [CanDeactivateGuard],
    },
];

export const UserManagementRoutes: ModuleWithProviders = RouterModule.forChild(
    routes
);
