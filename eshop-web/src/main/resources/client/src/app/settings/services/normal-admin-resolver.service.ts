import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, Resolve, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs/internal/observable/of';
import { catchError } from 'rxjs/internal/operators/catchError';
import { mergeMap } from 'rxjs/internal/operators/mergeMap';
import { take } from 'rxjs/internal/operators/take';
import { AdminManagementUser } from '../models/normal-admin/user-admin-magement.model';
import { NormalAdminService } from './normal-admin.service';

@Injectable({
    providedIn: 'root'
})
export class NormalAdminResolverService implements Resolve<AdminManagementUser[]> {

    constructor(private normalAdminService: NormalAdminService, private router: Router) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
        Observable<AdminManagementUser[]> | Observable<never> | Observable<any> {

        return this.normalAdminService.getAllUsers().pipe(
            take(1),
            mergeMap(allUsers => {
                if (allUsers) {
                    return of(allUsers);
                } else {
                    this.router.navigate(['/administrator']);
                    return of({ error: 'NOT_FOUND' });
                }
            }),
            catchError(error => {
                this.router.navigate(['settings', 'administrator']);
                return of({ error: 'NOT_FOUND' });
            })
        );
    }
}
