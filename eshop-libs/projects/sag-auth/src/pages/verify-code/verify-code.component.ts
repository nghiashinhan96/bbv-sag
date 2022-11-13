import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { SagAuthStorageService } from '../../services/sag-auth-storage.service';
import { SagAuthConfigService } from '../../services/sag-auth.config';
import { SAG_AUTH_TOKEN_EXPIRED } from '../../constants/sag-auth';
import { TOKEN_STATE } from '../../enums/token-state.enum';
import { SagMessageData } from 'sag-common';
import { SagAuthService } from '../../services/sag-auth.service';

@Component({
    selector: 'sag-auth-verify-code',
    templateUrl: './verify-code.component.html',
    styleUrls: ['./verify-code.component.scss']
})
export class SagVerifyCodeComponent implements OnInit {

    private readonly SAG_AUTH_INVALID_CODE = 'FORGOT_PASSWORD.INVALID_CODE';

    @Input() set queryParams(params: { code: string, reg: string }) {
        // the case user click link in email
        if (params) {
            const { code, reg } = params;
            if (code) {
                this.verifyCodeForm.get('inputCode').setValue(code);
                this.localStorageService.hashUsernameCode = reg;
            } else {
                this.notifier = { message: 'FORGOT_PASSWORD.CODE_IN_EMAIL', type: 'SUCCESS' };
            }
        } else {
            this.notifier = { message: 'FORGOT_PASSWORD.CODE_IN_EMAIL', type: 'SUCCESS' };
        }

    }
    @Output() gobackForgotPasswordEmitter = new EventEmitter();
    @Output() gobackResetPasswordEmitter = new EventEmitter();

    notifier: SagMessageData;
    verifyCodeForm: FormGroup;

    private spinner;

    constructor(
        private fb: FormBuilder,
        private localStorageService: SagAuthStorageService,
        private authService: SagAuthService,
        private config: SagAuthConfigService
    ) {
        this.spinner = config.spinner;
        this.buildVerifyCodeForm();
    }

    ngOnInit() {
    }

    onSubmit() {
        this.verifyCode();
    }

    private buildVerifyCodeForm() {
        this.verifyCodeForm = this.fb.group({
            inputCode: [''],
        });
    }

    // Step2 : User enter code from email or link. Verify to BE -> Passed -> move to reset password
    private verifyCode() {
        this.spinner.start();
        const inputCode = this.verifyCodeForm.get('inputCode').value;
        const hashUsernameCode = this.localStorageService.hashUsernameCode;
        if (!hashUsernameCode) {
            this.spinner.stop();
            this.notifier = { message: this.SAG_AUTH_INVALID_CODE, type: 'ERROR' };
            return;
        }
        this.authService.checkCode(inputCode, hashUsernameCode).subscribe(result => {
            this.spinner.stop();
            switch (result) {
                case TOKEN_STATE.VALID:
                    this.notifier = null;
                    this.localStorageService.secureCode = inputCode;
                    this.gobackResetPasswordEmitter.emit();
                    break;
                case TOKEN_STATE.INVALID:
                    this.notifier = { message: this.SAG_AUTH_INVALID_CODE, type: 'ERROR' };
                    break;
                case TOKEN_STATE.EXPIRED:
                    this.notifier = { message: SAG_AUTH_TOKEN_EXPIRED, type: 'ERROR' };
                    break;
            }
        }, () => {
            this.spinner.stop();
            this.notifier = { message: this.SAG_AUTH_INVALID_CODE, type: 'ERROR' };
        });
    }
}
