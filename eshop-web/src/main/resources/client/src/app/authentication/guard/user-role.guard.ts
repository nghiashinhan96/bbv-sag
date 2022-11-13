import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/core/services/user.service';
import { filter, map } from 'rxjs/operators';
import { USER_ROLE } from '../enums/user-role.enum';

@Injectable({
    providedIn: 'root'
})
export class UserRoleGuard implements CanActivate {
    constructor(
        private router: Router,
        private userService: UserService
    ) { }

    canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
        const role = route.data.role;
        const name = role && role.name;
        const negative = role && role.negative;

        return this.userService.userDetail$.pipe(
            filter(user => !!user && !!user.id),
            map(user => {
                if (!role) {
                    return true;
                }

                let hasRolePermission = false;
                switch (name) {
                    case USER_ROLE.FINAL_USER:
                        hasRolePermission = user.isFinalUserRole;
                        break;
                    case USER_ROLE.FINAL_USER_ADMIN:
                        hasRolePermission = user.finalUserAdminRole;
                        break;
                    case USER_ROLE.USER_ADMIN:
                        hasRolePermission = user.userAdminRole;
                        break;
                    case USER_ROLE.WHOLESALER_ADMIN:
                        hasRolePermission = user.hasWholesalerAdminPermission;
                        break;
                    default:
                        hasRolePermission = true;
                }

                if (negative) {
                    hasRolePermission = !hasRolePermission;
                }

                if (hasRolePermission) {
                    return true;
                } else {
                    this.router.navigate(['/404']);
                    return false;
                }
            })
        );
    }
}
