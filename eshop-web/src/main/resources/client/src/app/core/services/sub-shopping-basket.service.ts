import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { map } from 'rxjs/operators';

import { environment } from 'src/environments/environment';
import { OrderDashboardOverviewModel } from 'src/app/order-dashboard/models/order-dashboard-overview.model';
import { ShoppingBasketRefModel } from 'src/app/shopping-basket/models/shopping-basket-ref.model';
import { AppStorageService } from './app-storage.service';

@Injectable({
    providedIn: 'root'
})
export class SubShoppingBasketService {

    private subBasketOverviewSubject = new BehaviorSubject<OrderDashboardOverviewModel>(null);
    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient    ) { }

    get subBasketOverview$() {
        return this.subBasketOverviewSubject.asObservable();
    }

    updateSubBasketOverview(data) {
        this.subBasketOverviewSubject.next(data);
    }

    getOverview() {
        const url = `${this.baseUrl}order/wholesaler/dashboard`;
        this.http.get(url).subscribe((res: OrderDashboardOverviewModel) => {
            this.subBasketOverviewSubject.next(res);
        });
    }

    getSubBasketRef(finalCustomerOrderId: number) {
        let url = `${this.baseUrl}order/final-customer-order/${finalCustomerOrderId}/reference-text`;
        return this.http.get(url).pipe(
            map(result => {
                return new ShoppingBasketRefModel(result);
            })
        )
    }

}
