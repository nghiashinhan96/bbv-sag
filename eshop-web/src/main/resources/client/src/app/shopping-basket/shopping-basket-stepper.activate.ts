import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate } from '@angular/router';
import { Injectable } from '@angular/core';

const STEPS = {
    'shopping-basket/cart': 1,
    'shopping-basket/order': 2,
    'shopping-basket/checkout': 3
};
@Injectable()
export class ShoppingBasketStepperActivate implements CanActivate {

    constructor() { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentStep = this.getStep(location.href);
        const nextStep = this.getStep(state.url);
        if (nextStep <= currentStep) {
            return true;
        }
        return false;
    }

    private getStep(url) {
        const currentStep = Object.keys(STEPS).find(s => url.indexOf(s) !== -1);
        return STEPS[currentStep];
    }
}
