import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable()
export class SagInContextResolver implements Resolve<any> {

    constructor(private router: Router) { }

    resolve(route: ActivatedRouteSnapshot) {
        return {
            params: route.params,
            queryParams: route.queryParams
        };
    }
}
