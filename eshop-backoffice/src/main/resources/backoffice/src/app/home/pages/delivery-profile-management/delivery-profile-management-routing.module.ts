import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DeliveryProfilesComponent } from './components/delivery-profiles/delivery-profiles.component';
import { DeliveryProfileDetailComponent } from './components/delivery-profile-detail/delivery-profile-detail.component';
const routes: Routes = [{
    path: '',
    pathMatch: 'full',
    redirectTo: 'profiles',
}, {
    path: 'profiles',
    component: DeliveryProfilesComponent
},
{
    path: 'new',
    component: DeliveryProfileDetailComponent,
},
{
    path: ':profileId',
    component: DeliveryProfileDetailComponent,
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DeliveryProfileManagementRoutingModule { }
