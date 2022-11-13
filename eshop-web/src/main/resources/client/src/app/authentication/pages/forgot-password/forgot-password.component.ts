import { Component, OnInit, OnDestroy } from '@angular/core';
import { AffiliateUtil } from 'sag-common';
import { CountryUtil } from 'src/app/core/utils/country.util';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-forgot-password',
    templateUrl: './forgot-password.component.html',
    styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
    supportedLangCodes: any;

    constructor(
    ) { }

    ngOnInit() {
        this.supportedLangCodes = CountryUtil.getSupportedLangCodes(environment.affiliate);
    }
}
