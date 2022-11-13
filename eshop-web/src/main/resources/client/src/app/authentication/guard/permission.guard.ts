import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, CanActivate } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserService } from 'src/app/core/services/user.service';

@Injectable({
    providedIn: 'root'
})
export class PermissionGuard implements CanActivate {
    constructor(
        private router: Router,
        private userService: UserService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        const permissions = route.data.permission ? [route.data.permission] : [];
        return this.userService.hasPermissions(permissions).pipe(map(hasPermission => {
            if (!hasPermission) {
                this.router.navigateByUrl('/404');
            }
            return !!hasPermission;
        }));
    }
}
