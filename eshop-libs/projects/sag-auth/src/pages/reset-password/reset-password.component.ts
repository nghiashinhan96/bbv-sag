import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';

import { SagAuthStorageService } from '../../services/sag-auth-storage.service';
import { SagAuthConfigService } from '../../services/sag-auth.config';
import { SagAuthService } from '../../services/sag-auth.service';
import { SagValidator, SagMessageData } from 'sag-common';

@Component({
    selector: 'sag-auth-reset-password',
    templateUrl: './reset-password.component.html',
    styleUrls: ['./reset-password.component.scss']
})
export class SagResetPasswordComponent implements OnInit {

    @Output() gobackForgotPasswordEmitter = new EventEmitter();

    resetPasswordForm: FormGroup;
    hashUsernameCode: string;
    notifier: SagMessageData;
    isLoaded = true;

    private disable: any = null;
    constructor(
        private fb: FormBuilder,
        private location: Location,
        private appStorage: SagAuthStorageService,
        private authService: SagAuthService,
        private config: SagAuthConfigService,
        public router: Router
    ) { }

    ngOnInit() {
        const hash = this.appStorage.hashUsernameCode;
        if (!hash) {
            this.gobackForgotPasswordEmitter.emit();
        } else {
            this.hashUsernameCode = hash;
            this.buildPasswordForm();
        }
    }

    getBackState() {
        this.location.back();
    }

    // Allow user change password if (code + hash matched + password valid)
    resetPassword() {
        if (this.resetPasswordForm.valid) {
            this.notifier = null;
            const langCode = this.appStorage.langCode;
            const affiliate = this.config.affiliate;

            this.isLoaded = false;
            this.config.spinner.start();

            const code = this.appStorage.secureCode;
            if (code) {
                this.authService.resetPassword(
                    affiliate,
                    langCode,
                    this.resetPasswordForm.controls.newPassword.value,
                    code,
                    this.hashUsernameCode).subscribe(success => {
                        this.isLoaded = true;
                        this.config.spinner.stop();
                        this.appStorage.clearHashUsernameCode();
                        this.appStorage.clearSecureCode();
                        this.notifier = { message: 'SETTINGS.MESSAGE_SUCCESSFUL', type: 'SUCCESS' };
                        setTimeout(() => {
                            this.router.navigate(['login']);
                        }, 2000);
                    }, error => {
                        this.isLoaded = true;
                        this.config.spinner.stop();
                        const message = 'MY_ACCOUNT.PROFILE.PASSWORD_CHANGE.' + error.error_code;
                        this.notifier = { message, type: 'ERROR' };
                    });
            }
        }
        return false;
    }

    private buildPasswordForm() {
        this.resetPasswordForm = this.fb.group({
            newPassword: [{ value: '', disabled: this.disable }, [SagValidator.passwordValidator]],
            confirmPassword: [{ value: '', disabled: this.disable }],
        }, {
            validator: SagValidator.passwordMatchesValidator
        });
    }
}
