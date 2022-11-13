import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate, CanActivateChild } from '@angular/router';
import { Injectable } from '@angular/core';
import { UserService } from '../core/services/user.service';
import { map } from 'rxjs/internal/operators/map';
import { SALE_ACCESSIBLE_URLS } from '../core/conts/app.constant';

@Injectable()
export class LayoutResolver implements CanActivateChild {

    constructor(private router: Router, private userService: UserService) { }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = childRoute.data && childRoute.data.page || '';
        const path = state.url;
        if (this.userService.userDetail && this.userService.userDetail.salesUser && SALE_ACCESSIBLE_URLS.indexOf(path) === -1) {
            this.router.navigateByUrl('/home');
            return false;
        }
        return true;
    }
}
