import { NgModule, ModuleWithProviders } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';

import { ForgotPasswordValidator } from './services/forgot-password-validator';
import { SagAuthStorageService } from './services/sag-auth-storage.service';
import { SagAuthenGuard } from './guards/authen.guard';
import { SagAuthConfigService } from './services/sag-auth.config';
import { SagAuthService } from './services/sag-auth.service';
import { SagAuthHeaderComponent } from './pages/auth-header/auth-header.component';
import { SagAuthFooterComponent } from './pages/auth-footer/auth-footer.component';
import { SagCustomerRegistrationComponent } from './pages/customer-registration/customer-registration.component';
import { SagLoginFormComponent } from './pages/login-form/login-form.component';
import {
    SagPotentialCustomerRegistrationComponent
} from './pages/potential-customer-registration/potential-customer-registration.component';
import { SagForgotPasswordFormComponent } from './pages/forgot-pass-form/forgot-password-form.component';
import { SagResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { SagVerifyCodeComponent } from './pages/verify-code/verify-code.component';
import { SagCommonModule } from 'sag-common';

@NgModule({
    declarations: [
        SagPotentialCustomerRegistrationComponent,
        SagForgotPasswordFormComponent,
        SagResetPasswordComponent,
        SagVerifyCodeComponent,
        SagAuthHeaderComponent,
        SagAuthFooterComponent,
        SagLoginFormComponent,
        SagCustomerRegistrationComponent
    ],
    imports: [
        CommonModule,
        TranslateModule,
        TabsModule.forRoot(),
        ReactiveFormsModule,
        SagCommonModule
    ],
    exports: [
        SagPotentialCustomerRegistrationComponent,
        SagForgotPasswordFormComponent,
        SagResetPasswordComponent,
        SagVerifyCodeComponent,
        SagAuthHeaderComponent,
        SagAuthFooterComponent,
        SagLoginFormComponent,
        SagCustomerRegistrationComponent
    ]
})
export class SagAuthModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: SagAuthModule,
            providers: [
                SagAuthConfigService,
                SagAuthStorageService,
                SagAuthService,
                ForgotPasswordValidator,
                SagAuthenGuard
            ]
        };
    }
}
