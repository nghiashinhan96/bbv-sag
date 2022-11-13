import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';

import { ShoppingBasketService } from '../core/services/shopping-basket.service';

@Injectable()
export class ShoppingBasketResolver implements Resolve<any> {

    constructor(private router: Router, private shoppingBasketService: ShoppingBasketService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const basketType = route.queryParams.basket || '';
        this.shoppingBasketService.setBasketType(basketType);
        this.shoppingBasketService.toggleMiniBasket(false);
        return basketType;
    }
}
