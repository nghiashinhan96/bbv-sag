import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DatePipe } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { DatepickerModule } from 'ngx-bootstrap/datepicker';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';

import { CustomerDetailsComponent } from './customer-details.component';
import { CustomerDetailInfoComponent } from '../customer-detail-info/customer-detail-info.component';
import { CustomerDetailSettingComponent } from '../customer-detail-setting/customer-detail-setting.component';
import { CustomerDetailLicenceComponent } from '../customer-detail-licence/customer-detail-licence.component';
import { LicenceComponent } from '../licence/licence.component';
import { LicenceDeleteDialogComponent } from '../licence-delete-dialog/licence-delete-dialog.component';
import { CustomerDetailRoutes } from './customer-details.routes';
import { CustomerDetailUsersModule } from '../customer-detail-users/customer-detail-users.module';
import { CustomerService } from 'src/app/home/services/customer/customer.service';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { HomeMenuModule } from 'src/app/home/home-menu/home-menu.module';

@NgModule({
    declarations: [
        CustomerDetailsComponent,
        CustomerDetailInfoComponent,
        CustomerDetailSettingComponent,
        CustomerDetailLicenceComponent,
        LicenceComponent,
        LicenceDeleteDialogComponent,
    ],
    imports: [
        CustomerDetailRoutes,
        CommonModule,
        SharedModules,
        DatepickerModule,
        FormsModule,
        ReactiveFormsModule,
        NgSelectModule,
        HomeMenuModule,
        CustomerDetailUsersModule,
        TranslateModule,
        SagTableModule,
        AngularMyDatePickerModule
    ],
    exports: [CustomerDetailsComponent],
    providers: [CustomerService, DatePipe],
})
export class CustomerDetailsModule { }
