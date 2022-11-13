import { Injectable } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    Resolve,
    Router,
    RouterStateSnapshot,
} from '@angular/router';

import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';

import { AuthService } from '../authentication/services/auth.service';

@Injectable()
export class HomeAuthResolver implements Resolve<boolean> {
    constructor(private router: Router, private authService: AuthService) { }

    resolve(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {
        return this.authService.isAuth$.pipe(
            take(1)
        );
    }
}
