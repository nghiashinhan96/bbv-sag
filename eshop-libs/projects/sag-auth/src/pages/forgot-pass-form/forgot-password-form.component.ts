import { Component, OnInit, OnDestroy, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs/operators';

import { ForgotPasswordValidator } from '../../services/forgot-password-validator';
import { SagAuthStorageService } from '../../services/sag-auth-storage.service';
import { SagAuthService } from '../../services/sag-auth.service';
import { SagAuthConfigService } from '../../services/sag-auth.config';
import { Subscription } from 'rxjs';

@Component({
    selector: 'sag-auth-forgot-password-form',
    templateUrl: './forgot-password-form.component.html',
    styleUrls: ['./forgot-password-form.component.scss']
})
export class SagForgotPasswordFormComponent implements OnInit, OnDestroy {
    @Input() redirectPage: string;
    @Output() gobackLoginEmitter = new EventEmitter();
    @Output() gobackVerifyCodeEmitter = new EventEmitter();

    errorMessageForgotPassword: string;
    // flag loading for waiting service
    isLoaded = true;

    passForgotForm: FormGroup;
    private affiliate: string;
    private spinner: any;
    private configRedirectUrl: string;

    private subs = new Subscription();

    constructor(
        private fb: FormBuilder,
        public router: Router,
        private config: SagAuthConfigService,
        private forgotPasswordValidator: ForgotPasswordValidator,
        private appStorage: SagAuthStorageService,
        private authService: SagAuthService) {

        this.affiliate = config.affiliate;
        this.spinner = config.spinner;
        this.configRedirectUrl = config.redirectUrl;
    }

    ngOnInit() {
        this.buildForgotPassForm();
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onSubmit() {
        this.sendCode();
    }

    changeInputForgetPassword(isInputEmail: boolean) {
        if (isInputEmail) {
            this.passForgotForm.get('inputUsername').setValue('');
        } else {
            this.passForgotForm.get('inputEmail').setValue('');
        }
    }

    // Step1 : User call service to generate code to email, recieve a unique hash and store at FE.
    private sendCode() {
        const { inputEmail, inputUsername } = this.passForgotForm.value;
        this.errorMessageForgotPassword = this.forgotPasswordValidator
            .validateInputBeforeSendCode(inputUsername, inputEmail);
        if (this.errorMessageForgotPassword) {
            return;
        }
        const langCode = this.appStorage.langCode;
        this.isLoaded = false;
        this.spinner.start();
        const redirectUrl = `${this.configRedirectUrl}${this.redirectPage}`;
        this.subs.add(this.authService.sendCode(this.affiliate,
            inputEmail ? inputEmail : inputUsername, langCode, redirectUrl).pipe(finalize(() => this.spinner.stop()))
            .subscribe(
                response => {
                    this.appStorage.hashUsernameCode = response;
                    this.isLoaded = true;
                    this.gobackVerifyCodeEmitter.emit();
                },
                (error) => {
                    this.isLoaded = true;
                    this.errorMessageForgotPassword = error && (`FORGOT_PASSWORD.ERROR_MESSAGE.${error.error_code}`) || '';
                }
            ));
    }

    private buildForgotPassForm() {
        this.passForgotForm = this.fb.group({
            inputEmail: [''],
            inputUsername: ['']
        });
    }
}
