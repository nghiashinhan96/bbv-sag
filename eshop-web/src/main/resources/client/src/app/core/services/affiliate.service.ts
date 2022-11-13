import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';

import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { AppAuthConfigService } from 'src/app/authentication/services/app-auth.config.service';


@Injectable({
    providedIn: 'root'
})
export class AffiliateService {
    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient, private authConfig: AppAuthConfigService) { }

    public getAffiliateSettings(affiliate) {
        const url = `${this.baseUrl}settings/` + affiliate;
        return this.http.get(url, { observe: 'body' }).pipe(map((res: any) => {
            if (AffiliateUtil.isEhCh(affiliate)) {
                this.authConfig.redirectUrl = !!res && res.eh_portal_url ? `${res.eh_portal_url}/` : '';
            } else {
                this.authConfig.redirectUrl = !!res && res.default_url || '';
            }
            return res;
        }));
    }

    getJsonEventList() {
        const url = `${environment.baseUrl}analytics/settings`;
        return this.http.get(url)
            .pipe(
                catchError(() => of(null))
            );
    }

    getOutletBoxInfo() {
        const url = `${environment.baseUrl}training/sso/outlet`;
        return this.http.get(url)
            .pipe(
                catchError(() => of(null))
            );
    }
}
