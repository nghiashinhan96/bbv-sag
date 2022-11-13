import { Component, OnInit, EventEmitter, Output, Input, OnDestroy } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthModel } from '../../models/auth.model';
import { SagAuthConfigService } from '../../services/sag-auth.config';
import { SagMessageData } from 'sag-common';
import { Subscription } from 'rxjs';
import { AffiliateUtil } from 'sag-common';

@Component({
    selector: 'sag-auth-login-form',
    templateUrl: './login-form.component.html',
    styleUrls: ['./login-form.component.scss']
})
export class SagLoginFormComponent implements OnInit, OnDestroy {
    @Input() error: SagMessageData;
    @Input() set loginData(data: AuthModel) {
        if (data) {
            this.loginForm.patchValue(data);
        }
    }
    @Input() affiliate;
    @Input() ssoWhitelist: string[] = [];

    @Output() ssoLoginEmitter = new EventEmitter();
    @Output() registationEmitter = new EventEmitter();
    @Output() loginInputEmitter = new EventEmitter();
    @Output() forgotPasswordEmitter = new EventEmitter();

    loginForm: FormGroup;
    passwordType = 'password';
    passwordChangeSub: Subscription;
    showSsoLogin = false;
    isRegistrationShown = false;

    constructor(private fb: FormBuilder, private config: SagAuthConfigService) {
        this.loginForm = this.fb.group({
            userName: ['', Validators.required],
            password: ['', Validators.required],
            affiliate: config.affiliate,
            isShownPassword: false
        });
    }

    ngOnInit() {
        this.passwordChangeSub = this.loginForm.get('isShownPassword').valueChanges.subscribe(val => {
            if (val) {
                this.passwordType = 'text';
            } else {
                this.passwordType = 'password';
            }
        });
        this.showSsoLogin = this.isShowSsoLogin() && !AffiliateUtil.isEhCh(this.affiliate);
        this.isRegistrationShown = !AffiliateUtil.isEhCh(this.affiliate);
    }

    ngOnDestroy(): void {
        if (this.passwordChangeSub) {
            this.passwordChangeSub.unsubscribe();
        }
    }

    onSubmit() {
        if (this.loginForm.valid) {
            this.loginInputEmitter.emit(this.loginForm.value);
        }
    }

    isShowSsoLogin() {
        let domain = window.location.origin;
        return this.ssoWhitelist.indexOf(domain) > -1;
    }
}
