import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { SubShoppingBasketService } from 'src/app/core/services/sub-shopping-basket.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-header-sub-basket',
    templateUrl: './header-sub-basket.component.html',
    styleUrls: ['./header-sub-basket.component.scss']
})
export class HeaderSubBasketComponent implements OnInit, OnDestroy {

    isLoaded = false;
    totalNewOrders = 0;
    totalMyCustomerOrders = 0;
    totalOrdered = 0;

    subs = new SubSink();
    pollSubject: Observable<any>;
    POLL_INTERVAL = 3 * 60 * 1000;

    constructor(private subShoppingBasket: SubShoppingBasketService) { }

    ngOnInit() {
        this.subs.sink = this.subShoppingBasket.subBasketOverview$.subscribe(res => {
            if (!!res) {
                this.isLoaded = true;
                this.totalNewOrders = res.newOrderQuantity;
                this.totalMyCustomerOrders = res.openOrderQuantity;
                this.totalOrdered = res.orderedQuantity;
            }
        });
        this.subShoppingBasket.getOverview();

        let pollInterval = this.POLL_INTERVAL;

        this.pollSubject = new Observable(observer => {
            let interval = setInterval(() => {
                observer.next();
            }, pollInterval);

            return () => {
                clearInterval(interval);
            };
        });

        this.subs.sink = this.pollSubject.subscribe(() => {
            this.subShoppingBasket.getOverview();
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    refreshTotalNumber(event) {
        event.preventDefault();
        event.stopPropagation();
        this.isLoaded = false;
        setTimeout(() => {
            this.subShoppingBasket.getOverview();
        });

    }

}
