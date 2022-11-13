import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { MessengerData } from 'src/app/shared/common/components/messenger/messenger.component';
import { BOValidator } from 'src/app/core/utils/validator';

@Component({
    selector: 'backoffice-forgot-password-form',
    templateUrl: 'forgot-password-form.component.html',
    styleUrls: ['forgot-password-form.component.scss']
})
export class ForgotPasswordFormComponent implements OnInit {
    @Output() resetPassword = new EventEmitter();

    form: FormGroup;
    resultMessage: MessengerData;

    constructor(
        private fb: FormBuilder
    ) {

    }

    ngOnInit() {
        this.form = this.fb.group({
            email: ['', [Validators.required, BOValidator.emailValidator, BOValidator.emailDomain]]
        });
    }

    onSubmit() {
        if (this.form.invalid) {
            this.resultMessage = { type: 'ERROR', message: 'LOGIN.ERROR_MESSAGE.INVALID_EMAIL' } as MessengerData;
            return;
        }

        this.resetPassword.emit({
            data: this.form.value,
            onSuccess: (message) => this.resultMessage = message,
            onError: (err) => this.resultMessage = err
        });
    }
}
