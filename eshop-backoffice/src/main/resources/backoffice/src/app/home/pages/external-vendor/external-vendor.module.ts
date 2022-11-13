import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';
import { BsModalRef, ModalModule } from 'ngx-bootstrap/modal';

import { ExternalVendorDetailComponent } from './components/external-vendor-detail/external-vendor-detail.component';
import { ExternalVendorImportModalComponent } from './components/external-vendor-import-modal/external-vendor-import-modal.component';
import { ExternalVendorListComponent } from './components/external-vendor-list/external-vendor-list.component';
import { ExternalVendorRoutingModule } from './external-vendor-routing.module';
import { ExternalVendorService } from './services/external-vendor.service';
import { ExternalVendorDetailResolver } from './services/external-vendor-detail.resolver';
import { SharedModules } from 'src/app/shared/components/shared-components.module';


@NgModule({
    imports: [
        CommonModule,
        ExternalVendorRoutingModule,
        SharedModules,
        NgSelectModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule,
        SagTableModule,
        ModalModule.forRoot(),
    ],
    declarations: [
        ExternalVendorListComponent,
        ExternalVendorDetailComponent,
        ExternalVendorImportModalComponent
    ],
    providers: [ExternalVendorService, ExternalVendorDetailResolver],
    entryComponents: [ExternalVendorImportModalComponent]
})
export class ExternalVendorModule { }
