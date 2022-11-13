import { of, Subject } from "rxjs";

export class ShoppingBasketServiceStub {
    public currentSubBasket$ = new Subject<any>();

    loadCachedBasket() {
        return of();
    }
}