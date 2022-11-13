import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { DatepickerModule } from 'ngx-bootstrap/datepicker';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { SagTableModule } from 'sag-table';

import { UserFormComponent } from './user-form.component';
import { UserFormDataManagementService } from '../../../../services/user-form-data-management.service';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { NumberModule } from 'src/app/core/modules/number/number.module';
import { SharedDirectiveModule } from 'src/app/shared/directive/shared-directive.module';

@NgModule({
    declarations: [UserFormComponent],
    imports: [
        DatepickerModule,
        NgSelectModule,
        SagTableModule,
        TranslateModule,
        SharedModules,
        NumberModule,
        FormsModule,
        ReactiveFormsModule,
        TabsModule,
        CommonModule,
        SharedDirectiveModule,
    ],
    exports: [UserFormComponent],
    providers: [UserFormDataManagementService],
})
export class UserFormModule { }
