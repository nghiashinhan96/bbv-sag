import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ValidatorFn, ValidationErrors } from '@angular/forms';

import { ChangePassword, ChangePasswordModel } from '../../models/change-password.model';
import { SagMessageData } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';
import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';

@Component({
    selector: 'connect-change-password-form',
    templateUrl: './change-password-form.component.html',
    styleUrls: ['./change-password-form.component.scss']
})
export class ChangePasswordFormComponent implements OnInit, OnDestroy {
    @Output() changePasswordDataEmitter = new EventEmitter<{ request: ChangePasswordModel, callback: any }>();

    changePasswordForm: FormGroup;
    result: SagMessageData;
    isFormDisable = false;

    private passwordPattern: RegExp = /^(?=.*\d)(?=.*[a-zA-Z])([a-zA-Z0-9\\#$£\/! @\-.]{8,14})$/;

    constructor(
        private fb: FormBuilder,
        private userService: UserService,
        private appAuthConfigService: AppAuthConfigService
    ) { }

    ngOnInit() {
        this.isFormDisable = this.userService.userDetail.isSalesOnBeHalf;
        this.initForm();
    }

    ngOnDestroy() {

    }

    onSubmit(callback) {
        if (this.changePasswordForm.valid) {
            this.changePasswordForm.disable();
            const redirectUrl = `${this.appAuthConfigService.redirectUrl}`;
            const request = new ChangePassword().toUpdatePasswordRequest({ ...this.changePasswordForm.value, redirectUrl });
            this.changePasswordDataEmitter.emit({
                request,
                callback: (notify: SagMessageData) => {
                    this.reset(notify);
                    callback();
                }
            });
        }
    }

    get oldPassword() { return this.changePasswordForm.get('oldPassword').value; }
    get newPassword() { return this.changePasswordForm.get('newPassword').value; }
    get confirmPassword() { return this.changePasswordForm.get('confirmPassword').value; }

    private reset(message: SagMessageData) {
        this.changePasswordForm.reset();
        this.changePasswordForm.enable();
        this.result = message;
    }

    private initForm() {
        this.changePasswordForm = this.fb.group({
            oldPassword: ['', [Validators.required]],
            newPassword: ['', [Validators.required, Validators.pattern(this.passwordPattern)]],
            confirmPassword: ['', [Validators.required]]
        }, { validators: passwordMatchesValidator });

        if (this.isFormDisable) {
            this.changePasswordForm.disable();
        }
    }
}

export const passwordMatchesValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const newPassword = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');

    return newPassword && confirmPassword && newPassword.value !== confirmPassword.value ?
        { passwordNotMatch: true } : null;
};
