import { Router, CanActivate, ActivatedRouteSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/core/services/user.service';
import { filter, map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class SaleGuard implements CanActivate {
    constructor(
        private router: Router,
        private userService: UserService
    ) { }

    canActivate(route: ActivatedRouteSnapshot): Observable<boolean> {
        const isSalesOnBeHalf = route.data.isSalesOnBeHalf || false;
        
        return this.userService.userDetail$.pipe(
            filter(user => !!user && !!user.id),
            map(user => {
                if (user.isSalesOnBeHalf === isSalesOnBeHalf) {
                    return true;
                } else {
                    this.router.navigate(['/404']);
                    return false;
                }
            })
        );
    }
}
