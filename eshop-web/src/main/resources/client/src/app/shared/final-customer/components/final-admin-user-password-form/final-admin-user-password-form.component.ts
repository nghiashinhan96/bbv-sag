import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Validator } from 'src/app/core/utils/validator';
import { ProfileModel } from 'src/app/settings/models/final-user-admin/user-profile.model';
import { SagMessageData } from 'sag-common';

@Component({
    selector: 'connect-final-admin-user-password-form',
    templateUrl: 'final-admin-user-password-form.component.html',
    styleUrls: ['final-admin-user-password-form.component.scss']
})
export class FinalAdminUserPasswordFormComponent implements OnInit {
    @Input() profile: ProfileModel;
    @Output() save = new EventEmitter();

    passwordForm: FormGroup;

    isUpdating = false;
    result: SagMessageData;

    constructor(
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        this.passwordForm = this.fb.group({
            newPassword: ['', [Validators.required, Validator.passwordValidator]],
            confirmPassword: ['', Validators.required]
        }, { validator: Validator.passwordMatchesValidator });
    }

    get newPassword() { return this.passwordForm.get('newPassword') as FormControl; }
    get confirmPassword() { return this.passwordForm.get('confirmPassword') as FormControl; }

    onSubmit() {
        if (this.passwordForm.invalid) {
            return;
        }

        this.isUpdating = true;
        const data = {
            id: this.profile.id,
            password: this.passwordForm.value.newPassword
        };

        this.save.emit({
            data,
            onSuccess: () => {
                this.result = { type: 'SUCCESS', message: 'SETTINGS.PROFILE.MESSAGE_SUCCESSFUL' } as SagMessageData;
                this.isUpdating = false;
            },
            onError: (error) => {
                this.result = { type: 'ERROR', message: error } as SagMessageData;
                this.isUpdating = false;
            }
        });
    }
}
