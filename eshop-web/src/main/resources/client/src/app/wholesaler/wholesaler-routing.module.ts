import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WholesalerCustomersComponent } from './pages/wholesaler-customers/wholesaler-customers.component';
import { WholesalerCustomersUsersComponent } from './pages/wholesaler-customers-users/wholesaler-customers-users.component';
import { WholesalerComponent } from './pages/wholesaler/wholesaler.component';
import { OpeningDayComponent } from './pages/opening-day/opening-day.component';
import { OpeningTimeComponent } from './pages/opening-time/opening-time.component';
import { DeliveryProfileComponent } from './pages/delivery-profile/delivery-profile.component';
import { TourManagementComponent } from './pages/tour-management/tour-management.component';
import { MarginManagementComponent } from './pages/margin-management/margin-management.component';


const routes: Routes = [
    {
        path: '',
        component: WholesalerComponent,
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'wholesaler'
            },
            {
                path: 'wholesaler',
                component: WholesalerCustomersComponent
            },
            {
                path: 'wholesaler/:orgId',
                component: WholesalerCustomersUsersComponent
            },
            {
                path: 'opening-day',
                component: OpeningDayComponent
            },
            {
                path: 'opening-time',
                component: OpeningTimeComponent
            },
            {
                path: 'delivery-profile',
                component: DeliveryProfileComponent
            },
            {
                path: 'tour-management',
                component: TourManagementComponent
            },
            {
                path: 'margin-management',
                component: MarginManagementComponent
            },
            {
                path: '**',
                component: WholesalerCustomersComponent,
                redirectTo: 'wholesaler'
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class WholesalerRoutingModule { }
