import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'connect-forgot-password-form',
    templateUrl: './forgot-password-form.component.html'
})
export class ForgotPasswordFormComponent implements OnInit {

    redirectPage = 'forgotpassword/verifycode';
    constructor(public router: Router) { }

    ngOnInit() { }

    gobackLogin() {
        this.router.navigateByUrl('login');
    }

    gobackVerifyCode() {
        this.router.navigateByUrl(this.redirectPage);
    }
}
