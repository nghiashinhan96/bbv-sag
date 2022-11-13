import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'connect-reset-password',
    templateUrl: './reset-password.component.html'
})
export class ResetPasswordComponent implements OnInit {

    constructor(
        private router: Router
    ) { }

    ngOnInit() {
    }

    gobackForgotPassword() {
        this.router.navigateByUrl('forgotpassword');
    }
}
