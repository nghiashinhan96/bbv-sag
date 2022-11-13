import { Injectable } from '@angular/core';
import { SagAuthConfigService } from 'sag-auth';
import { environment } from 'src/environments/environment';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AffiliateUtil } from 'sag-common';
import { APP_TOKEN } from 'src/app/core/conts/app.constant';

@Injectable({
    providedIn: 'root'
})
export class AppAuthConfigService extends SagAuthConfigService {

    constructor() {
        super();
        this.baseUrl = environment.baseUrl;
        this.tokenUrl = environment.tokenUrl;
        this.spinner = SpinnerService;
        this.affiliate = environment.affiliate;
        this.tokenKey = APP_TOKEN;
        this.isFinalUser = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
        this.timeout = 130000;
        this.redirectUrl = '';
    }
}
