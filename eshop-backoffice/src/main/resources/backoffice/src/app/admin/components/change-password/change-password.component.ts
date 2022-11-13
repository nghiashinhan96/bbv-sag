import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';

import { NotificationModel } from '../../../shared/models/notification.model';
import { ChangePasswordModel } from '../../model/change-password.model';
import { BOValidator } from 'src/app/core/utils/validator';
import { User } from 'src/app/authentication/models/user.model';
import { UserService } from 'src/app/core/services/user.service';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-admin-change-password',
    templateUrl: './change-password.component.html',
    host: { class: 'row' }
})
export class ChangePasswordComponent implements OnInit {

    profilePasswordForm: FormGroup;
    notifierPassword: NotificationModel;
    // notifierUpdateUser: NotificationModel;
    private user: User;

    constructor(
        private userService: UserService,
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        this.userService.curUser.subscribe(user => {
            this.user = user;
            this.buildPasswordForm();
        });
    }

    updatePassword() {
        const password = this.profilePasswordForm.value.pass1;
        this.userService.updateSystemAdminPassword(new ChangePasswordModel({ password, redirectUrl: window.location.origin }))
            .subscribe(res => {
                this.notifierPassword = { messages: ['PASSWORD.MESSAGE_SUCCESSFUL'], status: true };
            }, error => {
                this.notifierPassword = { messages: [error.message], status: false };
            });
    }

    private buildPasswordForm() {
        this.profilePasswordForm = this.fb.group({
            pass1: [EMPTY_STRING, [Validators.required, BOValidator.passwordValidator]],
            pass2: [EMPTY_STRING, Validators.required]
        }, { validator: BOValidator.passwordMatchesValidator });
    }
}
