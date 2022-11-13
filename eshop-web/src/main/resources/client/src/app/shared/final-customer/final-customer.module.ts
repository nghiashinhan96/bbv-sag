import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FinalCustomerFormModalComponent } from './components/final-customer-form-modal/final-customer-form-modal.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { SagCurrencyModule } from 'sag-currency';
import { ConnectCommonModule } from '../connect-common/connect-common.module';
import { ReactiveFormsModule } from '@angular/forms';
import { FinalAdminUserPasswordFormComponent } from './components/final-admin-user-password-form/final-admin-user-password-form.component';
import { FinalAdminUserProfileFormComponent } from './components/final-admin-user-profile-form/final-admin-user-profile-form.component';
import { FinalAdminUserModalComponent } from './components/final-admin-user-modal/final-admin-user-modal.component';



@NgModule({
    declarations: [
        FinalCustomerFormModalComponent,
        FinalAdminUserPasswordFormComponent,
        FinalAdminUserProfileFormComponent,
        FinalAdminUserModalComponent
    ],
    imports: [
        CommonModule,
        NgSelectModule,
        TranslateModule,
        ConnectCommonModule,
        ReactiveFormsModule,
        SagCurrencyModule
    ],
    exports: [
        FinalCustomerFormModalComponent,
        FinalAdminUserPasswordFormComponent,
        FinalAdminUserProfileFormComponent
    ],
    entryComponents: [
        FinalCustomerFormModalComponent,
        FinalAdminUserModalComponent
    ]
})
export class FinalCustomerModule { }
