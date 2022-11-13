import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ExternalVendorDetailComponent } from './components/external-vendor-detail/external-vendor-detail.component';
import { ExternalVendorListComponent } from './components/external-vendor-list/external-vendor-list.component';
import { ExternalVendorDetailResolver } from './services/external-vendor-detail.resolver';

const routes: Routes = [
    {
        path: '',
        component: ExternalVendorListComponent
    },
    {
        path: 'create',
        component: ExternalVendorDetailComponent,
        resolve: { externalVendor: ExternalVendorDetailResolver}
    },
    {
        path: ':id',
        component: ExternalVendorDetailComponent,
        resolve: { externalVendor: ExternalVendorDetailResolver}
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ExternalVendorRoutingModule { }
