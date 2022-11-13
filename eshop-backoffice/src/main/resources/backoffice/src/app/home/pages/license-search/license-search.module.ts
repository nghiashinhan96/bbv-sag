import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { SagTableModule } from 'sag-table';
import { SharedModules } from '../../../shared/components/shared-components.module';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { LicenseSearchRoutes } from './license-search.routes';
import { LicenseSearchResultsComponent } from './components/license-result/license-search-result.component';
import { LicenseSearchService } from './services/license-search.service';
import { LicenseExportService } from './services/license-export.service';
import { LicenseUpdateService } from './services/license-update.service';
import { LicenseEditModalComponent } from './components/license-edit-modal/license-edit-modal.component';
import { DatepickerModule } from 'ngx-bootstrap/datepicker';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { CustomerService } from '../../services/customer/customer.service';
import { LicenseDeleteModalComponent } from './components/license-delete-modal/license-delete-modal.component';

@NgModule({
    declarations: [
        LicenseSearchResultsComponent,
        LicenseEditModalComponent,
        LicenseDeleteModalComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        SharedModules,
        FormsModule,
        ReactiveFormsModule,
        NgSelectModule,
        HomeMenuModule,
        SagTableModule,
        ModalModule.forRoot(),
        LicenseSearchRoutes,
        DatepickerModule,
        TranslateModule,
        SagTableModule,
        AngularMyDatePickerModule
    ],
    entryComponents: [LicenseEditModalComponent, LicenseDeleteModalComponent],
    providers: [LicenseSearchService, LicenseExportService, LicenseUpdateService, CustomerService]
})
export class LicenseSearchModule { }