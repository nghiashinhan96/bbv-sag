import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';

import { UserListComponent } from './user-list.component';
import { UserDataManagementService } from '../../../../services/user-data-management.service';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SharedModules } from 'src/app/shared/components/shared-components.module';

@NgModule({
    declarations: [UserListComponent],
    imports: [
        AngularMyDatePickerModule,
        NgSelectModule,
        SagTableModule,
        TranslateModule,
        SharedModules,
        CommonModule,
    ],
    exports: [UserListComponent],
    providers: [UserDataManagementService],
})
export class UserListModule { }
