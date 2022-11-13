import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ModalModule } from 'ngx-bootstrap/modal';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';

import { DeliveryProfileManagementRoutingModule } from './delivery-profile-management-routing.module';
import { DeliveryProfilesComponent } from './components/delivery-profiles/delivery-profiles.component';
import { DeliveryProfileDetailComponent } from './components/delivery-profile-detail/delivery-profile-detail.component';
import { DeliveryProfileImporterComponent } from './components/delivery-profile-importer/delivery-profile-importer.component';
import { DeliveryProfileService } from './services/delivery-profile.service';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { SharedPipesModule } from 'src/app/shared/pipes/shared-pipes.module';

@NgModule({
    imports: [
        DeliveryProfileManagementRoutingModule,
        SharedModules,
        NgSelectModule,
        SagTableModule,
        TranslateModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        SharedPipesModule,
        ModalModule.forRoot(),
    ],
    declarations: [
        DeliveryProfilesComponent,
        DeliveryProfileDetailComponent,
        DeliveryProfileImporterComponent,
    ],
    providers: [
        DeliveryProfileService
    ],
    entryComponents: [DeliveryProfileImporterComponent]
})
export class DeliveryProfileManagementModule { }
