import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { AuthenticationRoutingModule } from './authentication-routing.module';
import { AuthenticationComponent } from './authentication.component';
import { AuthenGuard } from '../authentication/guards/authen.guard';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { LoginComponent } from './pages/login/login.component';
import { ErrorComponent } from './pages/error/error.component';
import { AuthService } from './services/auth.service';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { BackOfficeCommonModule } from '../shared/common/bo-common.module';
import { ForgotPasswordFormComponent } from './components/forgot-password-form/forgot-password-form.component';

@NgModule({
    declarations: [
        LoginComponent,
        AuthenticationComponent,
        ErrorComponent,
        LoginFormComponent,
        ForgotPasswordFormComponent
    ],
    imports: [
        CommonModule,
        BackOfficeCommonModule,
        AuthenticationRoutingModule,
        TranslateModule,
        TabsModule.forRoot(),
        ReactiveFormsModule
    ],
    providers: [
        AuthenGuard,
        AuthService
    ]
})
export class AuthenticationModule { }
