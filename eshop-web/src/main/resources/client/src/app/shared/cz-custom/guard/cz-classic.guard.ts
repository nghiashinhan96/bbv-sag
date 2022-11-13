import { Router, CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CzClassicGuard implements CanActivate {
    constructor(
        private router: Router
    ) { }

    canActivate(): boolean {
        if (AffiliateUtil.isAffiliateCZ(environment.affiliate)) {
            this.router.navigate(['/404']);
            return false;
        } else {
            return true;
        }
    }
}
