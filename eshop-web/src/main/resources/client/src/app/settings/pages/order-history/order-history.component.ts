import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Observable, of } from 'rxjs';
import { finalize, map, catchError } from 'rxjs/operators';

import { OrderHistoryDetail } from '../../models/order-history/order-history-detail.model';
import { OrderHistoryFilter, OrderHistoryFilterRequest } from '../../models/order-history/order-history-filter.model';
import { OrderHistoryService } from '../../services/order-history.service';
import { UserService } from 'src/app/core/services/user.service';
import { TranslateService } from '@ngx-translate/core';
import { OrderDatePipe } from 'src/app/shared/connect-common/pipes/order-date.pipe';
@Component({
    selector: 'connect-order-history',
    templateUrl: './order-history.component.html',
    styleUrls: ['./order-history.component.scss'],
    providers: [OrderDatePipe]
})
export class OrderHistoryComponent implements OnInit {
    orderHistoryFilter: Observable<OrderHistoryFilter>;
    orderHistoryResult: Observable<OrderHistoryDetail[]>;

    errorMessage: string;
    isSalesDdatAuthed = false;
    isLoginOnBehalf = false;
    isSearching = false;
    request: OrderHistoryFilterRequest;
    constructor(
        private orderHistoryService: OrderHistoryService,
        private route: ActivatedRoute,
        public userService: UserService,
        private orderDatePipe: OrderDatePipe,
        private translateService: TranslateService
    ) { }

    ngOnInit() {
        this.route.data.subscribe(data => {
            this.errorMessage = null;
            if (data.error) {
                this.errorMessage = 'Not found order detail';
            }
        });
        this.isSalesDdatAuthed = this.userService.userDetail.salesUser;
        this.isLoginOnBehalf = this.userService.userDetail.isSalesOnBeHalf;

        this.orderHistoryFilter = this.orderHistoryService.getOrderHistoryFilter();
    }

    search({ request, done }: { request: OrderHistoryFilterRequest, done: () => void }) {
        this.errorMessage = '';
        this.isSearching = true;
        this.request = request;
        this.orderHistoryResult = this.orderHistoryService.searchOrderHistory(request).pipe(
            map((data: OrderHistoryDetail[]) => {
                data.forEach(item => {
                    item.date = this.orderDatePipe.transform(item.date);
                    item.status = item.status ? this.translateService.instant('SETTINGS.MY_ORDER.STATUS.' + item.status) : '';
                    item.source = item.source ? this.translateService.instant('SETTINGS.MY_ORDER.' + item.source) : '';
                });
                return data;
            }),
            catchError(err => {
                this.errorMessage = 'MESSAGE_SEARCH_NOT_FOUND.ORDER_HISTORY';
                return of(null);
            }),
            finalize(() => {
                done();
                this.isSearching = false;
            })
        );
    }
}
