import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, switchMap, finalize } from 'rxjs/operators';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs/internal/observable/of';

import { CustomerDataModel } from 'src/app/core/models/customer-data.model';
import { UserService } from 'src/app/core/services/user.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { OrderHistoryService } from 'src/app/settings/services/order-history.service';
import { environment } from 'src/environments/environment';
import { OrderHistoryDetailRequest } from 'src/app/settings/models/order-history/order-history-detail.model';

@Injectable({
    providedIn: 'root'
})
export class CustomerSearchService {

    private baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private userService: UserService,
        private appStore: AppStorageService,
        private orderHistoryService: OrderHistoryService
    ) { }

    getCustomerInfo(body) {
        const url = `${this.baseUrl}customers/search`;
        return this.http.post(url, body, { observe: 'body' });
    }

    getSaleOrders(body: any) {
        const url = `${this.baseUrl}order/sale/history`;
        return this.http.post(url, body);
    }

    switchUser(customerNr, orderHistory?: OrderHistoryDetailRequest) {
        return this.getCustomerInfo({
            customerNr,
            affiliate: environment.affiliate
        }).pipe(
            switchMap((res: CustomerDataModel) => {
                if (!!res && !!res.customer) {
                    if (!!res.customer.nr) {
                        this.appStore.shopCustomer = {
                            key: res.customer.nr.toString(),
                            value: res.isShopCustomer
                        };
                    }
                    return this.userService.authorizeCustomer(res.customer, res.admin)
                        .pipe(
                            finalize(() => {
                                if (!!orderHistory) {
                                    this.orderHistoryService.addOrderHistoryToCart(orderHistory).subscribe();
                                }
                            }),
                            map(() => {
                                res.customer.token = this.appStore.appToken;
                                res.customer.isShopCustomer = res.isShopCustomer;
                                this.userService.userDetail.isShopCustomer = res.isShopCustomer;
                                this.appStore.addCustomer({ ...res.customer });
                                return true;
                            }));
                }
                return of(false);
            }));
    }

    getCustomerBranches() {
        const defaultBranchId = this.userService.userDetail.defaultBranchId;
        if (!defaultBranchId) {
            return of(null);
        }
        const companyName = this.userService.userDetail.companyName;
        const body = { companyName, defaultBranchId };
        const url = `${this.baseUrl}customers/branches`;
        return this.http.post(url, body).pipe(map((res: any) => res && res.branches || []));
    }

    viewCustomerDetailInfo(affiliate: string, custNr: string): Observable<CustomerDataModel> {
        const url = `${this.baseUrl}customers/view?affiliate=${affiliate}&custNr=${custNr}`;
        return this.http.get<CustomerDataModel>(url);
    }
}
