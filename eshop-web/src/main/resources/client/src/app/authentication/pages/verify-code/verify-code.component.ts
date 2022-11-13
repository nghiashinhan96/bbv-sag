import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'connect-verify-code',
    templateUrl: './verify-code.component.html',
})
export class VerifyCodeComponent implements OnInit {

    constructor(
        public activatedRoute: ActivatedRoute,
        private router: Router
    ) { }

    ngOnInit() {

    }

    gobackForgotPassword() {
        this.router.navigateByUrl('forgotpassword');
    }

    gobackResetPassword() {
        this.router.navigateByUrl('forgotpassword/reset');
    }
}
