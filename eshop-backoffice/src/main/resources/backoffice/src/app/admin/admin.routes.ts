import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { ChangePasswordComponent } from './components/change-password/change-password.component';

export const routes: Routes = [
    {
        path: '',
        component: ChangePasswordComponent
    },
    {
        path: 'change-password',
        component: ChangePasswordComponent
    }
];

export const AdminModuleRoutes: ModuleWithProviders = RouterModule.forChild(routes);
