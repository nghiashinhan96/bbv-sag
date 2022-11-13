import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { OrderDashboardListRequestModel } from '../models/order-dashboard-list-request.model';
import { OrderDashboardListModel } from '../models/order-dashboard-list.model';
import { Observable } from 'rxjs/internal/Observable';
import { catchError, map } from 'rxjs/operators';
import { FC_ORDER_EXPORT_TYPE } from '../enums/order-export-type';
import { of } from 'rxjs';
import { SagMessageData } from 'sag-common';

@Injectable({
    providedIn: 'root'
})
export class OrderDasboardService {
    private baseUrl = environment.baseUrl;
    constructor(private http: HttpClient) { }

    getOrders(req: OrderDashboardListRequestModel) {
        const url = `${this.baseUrl}order/final-customer-order/search`;
        return this.http.post<OrderDashboardListModel>(url, req).pipe(map(res => new OrderDashboardListModel(res)));
    }

    deleteOrder(openOrderId: number) {
        const url = `${this.baseUrl}order/final-customer-order/${openOrderId}/delete`;
        return this.http.delete(url);
    }

    exportFinalCustomerOrder(finalCustomerOrderId: number, exportType: FC_ORDER_EXPORT_TYPE, fileName: string) {
        const url = `${this.baseUrl}order/final-customer-order/${finalCustomerOrderId}/${exportType}`;
        return this.http.get(url, {
            responseType: 'arraybuffer',
        }).pipe(map((res: any) => {
            saveAs(new Blob([res], {type: "text/plain;charset=utf-8"}), fileName);
            return null;
        }), catchError(err => {
            return of({
                type: 'ERROR',
                message: 'CUSTOMER_INFO.MESSAGES.INTERAL_SERVER_ERROR'
            } as SagMessageData);
        }));
    }
}
