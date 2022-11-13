import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { MessengerData } from 'src/app/shared/common/components/messenger/messenger.component';

@Component({
    selector: 'backoffice-login-form',
    templateUrl: 'login-form.component.html',
    styleUrls: ['login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
    @Output() submitLogin = new EventEmitter();

    form: FormGroup;
    errorMsg: MessengerData;

    constructor(
        private fb: FormBuilder
    ) {

    }

    ngOnInit() {
        this.form = this.fb.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    onSubmit() {
        this.submitLogin.emit({ credentials: this.form.value, onError: (err) => this.errorMsg = err });
    }
}
