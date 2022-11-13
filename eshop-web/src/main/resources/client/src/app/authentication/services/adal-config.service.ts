import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AdalConfigService {
    public get adalConfig(): any {
        return {
            tenant: 'sagag.onmicrosoft.com',
            clientId: `${environment.sso_clientid}`,
            redirectUri: window.location.origin + `${environment.sso_appcontext}`,
            postLogoutRedirectUri: window.location.origin + `${environment.sso_appcontext}`
        };
    }
}
