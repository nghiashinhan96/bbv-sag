import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { SagAuthModule, SagAuthConfigService } from 'sag-auth';
import { AuthenticationRoutingModule } from './authentication-routing.module';
import { LoginComponent } from './pages/login/login.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { ForgotPasswordFormComponent } from './pages/forgot-pass-form/forgot-password-form.component';
import { VerifyCodeComponent } from './pages/verify-code/verify-code.component';
import { AppAuthConfigService } from './services/app-auth.config.service';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';
import { ErrorComponent } from './pages/error/error.component';

@NgModule({
    declarations: [
        LoginComponent,
        ForgotPasswordComponent,
        ForgotPasswordFormComponent,
        ResetPasswordComponent,
        VerifyCodeComponent,
        ErrorComponent
    ],
    imports: [
        CommonModule,
        AuthenticationRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        ReactiveFormsModule,
        SagAuthModule.forRoot(),
        ConnectCommonModule
    ],
    providers: [
        { provide: SagAuthConfigService, useExisting: AppAuthConfigService }
    ]
})
export class AuthenticationModule { }
