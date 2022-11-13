import { Injectable } from '@angular/core';
import { Resolve, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { OrderHistoryDetail, OrderHistoryDetailRequest } from '../models/order-history/order-history-detail.model';
import { OrderHistoryService } from './order-history.service';
import { Observable } from 'rxjs/internal/Observable';
import { take, mergeMap, catchError, finalize } from 'rxjs/operators';
import { of, EMPTY } from 'rxjs';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Injectable()
export class OrderHistoryDetailResolverService implements Resolve<OrderHistoryDetail> {

    constructor(private orderHistoryService: OrderHistoryService, private router: Router) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
        Observable<OrderHistoryDetail> | Observable<never> | Observable<any> {
        const body: OrderHistoryDetailRequest = {
            orderId: route.queryParamMap.get('orderId') || '',
            orderNumber: route.paramMap.get('orderNumber')
        };

        return this.orderHistoryService.searchOrderHistoryDetail(body).pipe(
            mergeMap(orderHistoryDetail => {
                if (orderHistoryDetail) {
                    return of(orderHistoryDetail);
                } else {
                    this.router.navigate(['/order-history']);
                    return of({ error: 'NOT_FOUND' });
                }
            }),
            finalize(() => SpinnerService.stop())
        );
    }
}
