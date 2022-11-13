import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { APP_TOKEN } from 'src/app/core/conts/app.constant';
import { LocalStorage } from 'ngx-webstorage';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';

@Injectable({ providedIn: 'root'})
export class AuthenGuard implements CanActivate, CanActivateChild {

    constructor(private router: Router, private appStorage: AppStorageService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (!this.appStorage.appToken) {
            this.router.navigate(['unauthorized']);
            return false;
        }
        return true;
    }

    canActivateChild(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (!this.appStorage.appToken) {
            this.router.navigate(['unauthorized']);
            return false;
        }
        return true;
    }
}
