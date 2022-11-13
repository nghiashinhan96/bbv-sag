import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';

@Injectable()
export class CanDeactivateCheckoutGuard implements CanDeactivate<any> {
    canDeactivate(
        component: any,
        currentRoute: ActivatedRouteSnapshot,
        currentState: RouterStateSnapshot,
        nextState?: RouterStateSnapshot
    ): boolean {
        const nextUrl = nextState.url;
        return (nextUrl.indexOf('/shopping-basket/order') === -1 && nextUrl.indexOf('/shopping-basket/cart') === -1);
    }
}
