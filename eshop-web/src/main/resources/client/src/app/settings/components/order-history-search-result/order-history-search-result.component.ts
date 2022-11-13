import { Component, ElementRef, Inject, Input, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { first, finalize, catchError } from 'rxjs/operators';
import { get } from 'lodash';
import { SagTableColumn } from 'sag-table';
import { AnalyticEventType, ShoppingBasketEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { OrderHistoryCartResponse } from '../../models/order-history/order-history-cart-response.model';
import { OrderHistoryDetail, OrderHistoryDetailRequest } from '../../models/order-history/order-history-detail.model';
import { OrderItem } from '../../models/order-history/order-item.model';
import { OrderHistoryBusinessService } from '../../services/order-history-business.service';
import { OrderHistoryService } from '../../services/order-history.service';
import { OrderHistoryFilterRequest } from '../../models/order-history/order-history-filter.model';
import { BasketItemSourceDesc } from 'src/app/analytic-logging/enums/basket-item-source-desc.enum';
import { PageScrollService } from 'ngx-page-scroll-core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'connect-order-history-search-result',
    templateUrl: './order-history-search-result.component.html',
    styleUrls: ['./order-history-search-result.component.scss']
})
export class OrderHistorySearchResultComponent implements OnInit {
    @ViewChild('viewDetailTemplate', { static: true }) viewDetailTemplate: TemplateRef<any>;
    @ViewChild('cartTemplate', { static: true }) cartTemplate: TemplateRef<any>;
    @ViewChild('orderHistoryDetail', {read: ElementRef, static: false }) orderHistoryDetailElement: ElementRef;

    @Input() result: Observable<OrderHistoryDetail[]>;
    @Input() request: OrderHistoryFilterRequest;
    columns: SagTableColumn[] = [];
    selectedRow: OrderHistoryDetail;
    detail: Observable<OrderHistoryDetail>;

    constructor(
        private router: Router,
        private route: ActivatedRoute,
        private analyticService: AnalyticLoggingService,
        private orderHistoryService: OrderHistoryService,
        private orderHistoryBusiness: OrderHistoryBusinessService,
        private pageScrollService: PageScrollService,
        @Inject(DOCUMENT) private document: any
    ) { }

    ngOnInit() {
        this.columns = this.orderHistoryBusiness
            .buildTableColumns([this.viewDetailTemplate]);
    }

    onViewOrderDetail(event, order: OrderHistoryDetail) {
        event.preventDefault();
        event.stopPropagation();
        SpinnerService.start();

        this.selectedRow = order;

        const body: OrderHistoryDetailRequest = {
            orderId: String(order.id || ''),
            orderNumber: order.nr
        };

        this.detail = this.orderHistoryService.searchOrderHistoryDetail(body).pipe(
            finalize(() => {
                SpinnerService.stop();
                this.pageScrollService.scroll({
                    document: this.document,
                    scrollTarget: this.orderHistoryDetailElement && this.orderHistoryDetailElement.nativeElement || null,
                    scrollInView: true,
                    duration: 400
                });
            })
        );
    }

    async addToShoppingBasket(callback) {
        let basketItemSource = this.analyticService.createBasketItemSource(BasketItemSourceDesc.ORDER_HISTORY);
        SpinnerService.start();
        const body = {
            orderId: `${this.selectedRow.id}`,
            orderNumber: this.selectedRow.nr,
            basketItemSourceId: basketItemSource.basketItemSourceId,
            basketItemSourceDesc: basketItemSource.basketItemSourceDesc
        } as OrderHistoryDetailRequest;

        if (!body.orderId && !body.orderNumber) {
            return;
        }

        const detailOrder = await this.orderHistoryService.searchOrderHistoryDetail(body)
            .pipe(catchError(() => of(null)))
            .toPromise();

        this.orderHistoryService.addOrderHistoryToCart(body)
            .pipe(finalize(() => SpinnerService.stop()))
            .subscribe(addedOrder => {
                if (callback) {
                    callback(true);
                }
                this.sendEvent(detailOrder, addedOrder);
            }, () => {
                if (callback) {
                    callback(false);
                }
            });
    }

    private sendEvent(order: OrderHistoryDetail, response: OrderHistoryCartResponse) {
        setTimeout(() => {
            let totalPriceOfSelectedOrderDetail = 0;
            let selectedArticleIds = [];
            if (order && order.orderItems && order.orderItems.length) {
                selectedArticleIds = order.orderItems.map(item => item.article.artid);
                totalPriceOfSelectedOrderDetail = order
                    .orderItems
                    .map(item => get(item, 'article.price.price.totalNetPrice', 0))
                    .reduce((pre, cur) => pre + cur, 0);

                const items = [];

                (response && response.items || []).forEach(item => {
                    const orderItem =
                        order.orderItems.find(
                            p => p.article.artid === item.articleItem.artid && p.vehicle.vehid === item.vehicle.vehid
                        );
                    if (orderItem) {
                        item.quantity = orderItem.article.amountNumber;
                        items.push(item);
                    }
                });

                response.items = items;
            }
            const event = this.analyticService.createShoppingBasketEventData(
                response,
                {
                    source: ShoppingBasketEventType.ORDER_HISTORY,
                    selectedArticles: selectedArticleIds,
                    totalPrice: totalPriceOfSelectedOrderDetail
                }
            );
            this.analyticService.postEventFulltextSearch(event, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }
}
