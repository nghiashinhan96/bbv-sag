import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home.component';
import { HomeSearchComponent } from './pages/search/search.component';
import { HomeAuthResolver } from './home-auth-resolver.service';

const routes: Routes = [
    {
        path: '',
        component: HomeComponent,
        children: [
            {
                path: '',
                redirectTo: 'search',
                pathMatch: 'full'
            },
            {
                path: 'search',
                component: HomeSearchComponent
            },
            {
                path: 'search/affiliates',
                loadChildren: () =>
                    import('./pages/affiliate-search/affiliate-search.module').then(
                        (m) => m.AffiliateSearchModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver,
                },
            },
            {
                path: 'search/customer-groups',
                loadChildren: () =>
                    import('./pages/customer-group/customer-group.module').then(
                        (m) => m.CustomerGroupModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver,
                },
            },
            {
                path: 'search/customers',
                loadChildren: () =>
                    import('./pages/customer-search/customer-search.module').then(
                        (m) => m.CustomerSearchModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver,
                },
            },
            {
                path: 'search/users',
                loadChildren: () =>
                    import('./pages/user-management/user-management.module').then(
                        (m) => m.UserManagementModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'message',
                loadChildren: () =>
                    import('./pages/message/message.module').then(
                        (m) => m.MessageModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'sales',
                loadChildren: () =>
                    import('./pages/sales-management/sales-management.module').then(
                        (m) => m.SalesManagementModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'opening-day',
                loadChildren: () =>
                    import('./pages/opening-day-calendar/opening-day-calendar.module').then(
                        (m) => m.OpeningDayCalendarModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'branches',
                loadChildren: () =>
                    import('./pages/branches/branches.module').then(
                        (m) => m.BranchesModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'external-vendors',
                loadChildren: () =>
                    import('./pages/external-vendor/external-vendor.module').then(
                        (m) => m.ExternalVendorModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'delivery-profile-management',
                loadChildren: () =>
                    import('./pages/delivery-profile-management/delivery-profile-management.module').then(
                        (m) => m.DeliveryProfileManagementModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
            {
                path: 'search/licenses',
                loadChildren: () =>
                    import('./pages/license-search/license-search.module').then(
                        (m) => m.LicenseSearchModule
                    ),
                resolve: {
                    isAuthed: HomeAuthResolver
                }
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class HomeRoutingModule { }
