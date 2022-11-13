import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { map } from 'rxjs/internal/operators/map';
import { get } from 'lodash';
import { OrderHistoryFilter, OrderHistoryFilterRequest } from '../models/order-history/order-history-filter.model';
import { OrderHistoryDetail, OrderHistoryDetailRequest } from '../models/order-history/order-history-detail.model';
import { OrderHistoryCartResponse } from '../models/order-history/order-history-cart-response.model';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { SaleOrderHistory } from '../models/sale-order-history/sale-order-history.model';
import { SagCurrencyPipe } from 'sag-currency';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { OrderHistoryBusinessService } from './order-history-business.service';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class OrderHistoryService {
    private readonly ORDER_HISTORY_FILTER_URL = 'order/history/filters';
    private readonly ORDER_HISTORY_SEARCH_URL = 'order/history';
    private readonly ORDER_HISTORY_DETAIL_URL = 'order/history/detail';
    private readonly ADD_ORDER_HISTORY_TO_CART_URL = 'cart/order-history/add';

    private readonly SALE_ORDER_HISTORY_SEARCH_URL = 'order/sale/history';
    private readonly SALE_ORDER_DETAIL_URL = 'order/history/detail';

    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private translateService: TranslateService,
        private http: HttpClient,
        private fbRecording: FeedbackRecordingService,
        private currencyPipe: SagCurrencyPipe,
        private shoppingBasketService: ShoppingBasketService,
        private orderHistoryBusiness: OrderHistoryBusinessService
    ) { }

    getOrderHistoryFilter() {
        const url = `${this.BASE_URL}${this.ORDER_HISTORY_FILTER_URL}`;
        return this.http.get(url).pipe(map((response) => new OrderHistoryFilter(response)));
    }

    searchOrderHistory(body: OrderHistoryFilterRequest) {
        const url = `${this.BASE_URL}${this.ORDER_HISTORY_SEARCH_URL}`;
        return this.http.post(url, body).pipe(
            map((response: OrderHistoryDetail[]) => response.map(item => new OrderHistoryDetail(item))),
            catchError(() => of(null))
        );
    }

    searchOrderHistoryDetail(body: OrderHistoryDetailRequest) {
        const url = `${this.BASE_URL}${this.ORDER_HISTORY_DETAIL_URL}`;
        return this.http.post(url, body).pipe(map((response: any) => {
            const order = new OrderHistoryDetail(response);
            order.orderItems.forEach(it => {
                it.deliveryInformation = this.orderHistoryBusiness.buildDeliveryInfo(it.article.availabilities, it.sendMethodDesc);
                it.showPriceType = order.showPriceType;

                const grossPrice = get(it, 'article.displayedPrice.price', 0) || get(it, 'article.price.price.grossPrice', 0);

                if (grossPrice) {
                    it.grossPrice = this.currencyPipe.transform(grossPrice);
                } else {
                    it.grossPrice = this.translateService.instant('ARTICLE.PRICE.ARTICLE_WITHOUT_PRICE');
                    it.priceType = '';
                }
            });
            return order;
        }));
    }

    addOrderHistoryToCart(body: OrderHistoryDetailRequest) {
        const url = `${this.BASE_URL}${this.ADD_ORDER_HISTORY_TO_CART_URL}`;
        return this.http.post(url, body, {
            params: {
                shopType: SHOPPING_BASKET_ENUM.DEFAULT
            }
        }).pipe(map((response: any) => {
            const basket = new ShoppingBasketModel(response);
            this.shoppingBasketService.updateOtherProcess(basket);

            if (response.items) {
                this.fbRecording.recordShoppingCartItems(response.items, false);
            }
            return new OrderHistoryCartResponse(response);
        }));
    }

    getSaleOrders(body: any) {
        const url = `${this.BASE_URL}${this.SALE_ORDER_HISTORY_SEARCH_URL}`;
        return this.http.post(url, body).pipe(
            map((response: any) => {
                return {
                    rows: response.content.map(item => {
                        const row = { ...item, totalPrice: this.currencyPipe.transform(item.totalPrice) };
                        return new SaleOrderHistory(row);
                    }),
                    currentPage: response.pageable.pageNumber,
                    totalPages: response.totalPages,
                    itemsPerPage: response.pageable.pageSize,
                    totalItems: response.totalElements
                };
            })
        );
    }

    getOrderDetail(order: SaleOrderHistory) {
        const body = {
            orderId: order.id,
            orderNumber: order.orderNumber,
            affiliate: environment.affiliate,
            customerNr: order.customerNr
        };

        const url = `${this.BASE_URL}${this.SALE_ORDER_DETAIL_URL}`;
        return this.http.post(url, body);
    }
}
