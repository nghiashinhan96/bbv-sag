import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { SagAuthStorageService } from '../services/sag-auth-storage.service';
import { SagAuthService } from '../services/sag-auth.service';


@Injectable()
export class SagAuthenGuard implements CanActivate, CanActivateChild {

    constructor(
        private router: Router,
        private authStorage: SagAuthStorageService,
        private authService: SagAuthService
    ) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

        if (!this.authStorage.token || this.authService.isTokenExpired(this.authStorage.token)) {
            if (state.url.indexOf('/404') === -1) {
                this.router.navigate(['login'], { queryParams: { returnUrl: state.url } });
            } else {
                this.router.navigate(['login']);
            }
            return false;
        }
        return true;
    }

    canActivateChild(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (!this.authStorage.token || this.authService.isTokenExpired(this.authStorage.token)) {
            if (state.url.indexOf('/404') === -1) {
                this.router.navigate(['login'], { queryParams: { returnUrl: state.url } });
            } else {
                this.router.navigate(['login']);
            }
            return false;
        }
        return true;
    }
}
