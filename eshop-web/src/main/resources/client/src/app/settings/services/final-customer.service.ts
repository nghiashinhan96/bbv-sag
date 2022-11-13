import { Injectable } from '@angular/core';
import { FinalUserOrderHistoryResponse } from '../models/order-history/order-history-response.model';
import { FinalCustomerOrder } from '../models/final-customer/final-customer-order.model';
import { FinalCustomerResponse } from '../models/final-customer/final-customer-response.model';
import { FinalCustomerSearchCriteria } from '../models/final-customer/final-customer-criteria.model';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/operators';
import { FinalCustomerTemplate } from '../models/final-customer/final-customer-template';
import { FinalCustomerDetail } from '../models/final-customer/final-customer-detail.model';
import { OrderHistoryBusinessService } from './order-history-business.service';

@Injectable({
    providedIn: 'root'
})
export class FinalCustomerService {

    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private orderHistoryBusiness: OrderHistoryBusinessService
    ) { }

    getCustomerTemplate() {
        const url = `${this.BASE_URL}final-customer/profile/template`;
        return this.http.get(url)
            .pipe(map(res => new FinalCustomerTemplate(res)));
    }

    getSelectedCustomer(id: number) {
        const url = `${this.BASE_URL}final-customer/${id}/selected`;
        return this.http.post(url, null)
            .pipe(map(res => new FinalCustomerTemplate(res)));
    }

    searchFinalCustomers(page: number, size: number, req: FinalCustomerSearchCriteria) {
        const url = `${this.BASE_URL}final-customer/search?page=${page}&size=${size}`;
        return this.http.post(url, req)
            .pipe(map(response => new FinalCustomerResponse(response)));
    }

    createFinalCustomer(body: FinalCustomerDetail) {
        const url = `${this.BASE_URL}final-customer/create`;
        return this.http.post(url, body);
    }

    updateFinalCustomer(finalCustomerId: number, body: FinalCustomerDetail) {
        const url = `${this.BASE_URL}final-customer/${finalCustomerId}/update`;
        return this.http.post(url, body);
    }

    deleteFinalCustomer(finalCustomerId: number) {
        const url = `${this.BASE_URL}final-customer/${finalCustomerId}/delete`;
        return this.http.delete(url);
    }

    getOrders(req: any) {
        const url = `${this.BASE_URL}order/final-customer-order/search`;
        return this.http.post(url, req)
            .pipe(
                map((res: any) => {
                    const data = new FinalUserOrderHistoryResponse({
                        ...res,
                        data: res.content,
                        total: res.totalElements,
                        pageNr: res.number
                    });
                    return data;
                })
            );
    }

    getOrderDetail(finalCustomerOrderId: number) {
        const url = `${this.BASE_URL}order/final-customer/${finalCustomerOrderId}`;
        return this.http.get(url)
            .pipe(map(res => {
                let customerOrder = new FinalCustomerOrder(res);
                customerOrder.items.forEach(order=>{
                    order.deliveryInformation = this.orderHistoryBusiness.buildFCDeliveryInfo(order.availabilities);
                });
                return customerOrder; 
            }));
    }
}
