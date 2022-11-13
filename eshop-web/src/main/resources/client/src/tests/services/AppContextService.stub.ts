import { Subject } from "rxjs";

export class AppContextServiceStub {
    public shoppingBasketContext$ = new Subject<any>();
}