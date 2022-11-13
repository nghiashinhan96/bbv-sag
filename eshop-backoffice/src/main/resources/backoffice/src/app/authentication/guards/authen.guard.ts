import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';

@Injectable()
export class AuthenGuard implements CanActivate, CanActivateChild {

    constructor(private router: Router, private appStorage: AppStorageService) { }

    canActivate(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (!this.appStorage.appToken) {
            this.router.navigate(['login']);
            return false;
        }
        return true;
    }

    canActivateChild(
        next: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
        if (!this.appStorage.appToken) {
            this.router.navigate(['login']);
            return false;
        }
        return true;
    }
}
