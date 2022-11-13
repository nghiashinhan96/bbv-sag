import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { CustomerGroupListComponent } from './components/customer-group-list/customer-group-list.component';
import { CustomerGroupDetailComponent } from './components/customer-group-detail/customer-group-detail.component';
import { CustomerGroupResolver } from './services/customer-group.resolver';


export const routes: Routes = [
    {
        path: '',
        component: CustomerGroupListComponent
    },
    {
        path: ':collectionShortName',
        component: CustomerGroupDetailComponent,
        resolve: {
            data : CustomerGroupResolver
        }
    }
];

export const CustomerGroupRoute: ModuleWithProviders = RouterModule.forChild(routes);
