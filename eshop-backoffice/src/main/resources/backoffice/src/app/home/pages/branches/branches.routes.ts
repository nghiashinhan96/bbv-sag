import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BranchListComponent } from './components/branch-list/branch-list.component';
import { BranchDetailFormComponent } from './components/branch-detail-form/branch-detail-form.component';

const routes: Routes = [
    {
        path: '',
        component: BranchListComponent,
    },
    {
        path: 'create',
        component: BranchDetailFormComponent
    },
    {
        path: 'edit/:id',
        component: BranchDetailFormComponent
    }
];

export const BranchesRoutingModule: ModuleWithProviders = RouterModule.forChild(routes);
