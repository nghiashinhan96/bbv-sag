import { Observable } from 'rxjs/internal/Observable';
import { CanActivateChild, Router, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Injectable } from "@angular/core";

import { UserService } from 'src/app/core/services/user.service';
import { CustomerUtil } from 'src/app/core/utils/customer.util';

@Injectable({
    providedIn: 'root'
})

export class DvseGuard implements CanActivateChild {
    constructor(
        private router: Router,
        private userService: UserService
    ) { }

    canActivateChild(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        const userDetail = this.userService && this.userService.userDetail || null;
        if (!!userDetail) {
            if (CustomerUtil.isDvseCustomer(userDetail.affiliateShortName)) {
                return true;
            }

            this.router.navigate(['/']);
        }

        return false;
    }
}