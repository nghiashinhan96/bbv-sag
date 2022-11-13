import { Router, CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/core/services/user.service';
import { filter, map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class WholesalerGuard implements CanActivate {
    constructor(
        private router: Router,
        private userService: UserService
    ) { }

    canActivate(): Observable<boolean> {
        return this.userService.userDetail$.pipe(
            filter(user => !!user && !!user.id),
            map(user => {
                if (user.hasWholesalerPermission && !user.isSalesOnBeHalf) {
                    return true;
                } else {
                    this.router.navigate(['/404']);
                    return false;
                }
            })
        );
    }
}
