import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { TabsModule } from 'ngx-bootstrap/tabs';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { ModalModule, BsModalService } from 'ngx-bootstrap/modal';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';

import { CustomerDetailUsersComponent } from './customer-detail-users.component';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { UserListModule } from '../../../user-management/components/user-list/user-list.module';
import { UserFormModule } from '../../../user-management/components/user-form/user-form.module';

@NgModule({
    declarations: [CustomerDetailUsersComponent],
    imports: [
        AngularMyDatePickerModule,
        NgSelectModule,
        SagTableModule,
        TranslateModule,
        TabsModule.forRoot(),
        SharedModules,
        UserListModule,
        UserFormModule,
        ModalModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
    ],
    exports: [CustomerDetailUsersComponent],
    providers: [BsModalService],
})
export class CustomerDetailUsersModule { }
