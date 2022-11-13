import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import { AnalyticLoggingService } from './analytic-logging.service';
import { AnalyticEventType } from '../enums/event-type.enums';

@Injectable({ providedIn: 'root' })
export class OrderAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService
    ) { }

    sendTransferBasketEventData({
        shoppingBasket,
        shoppingBasketContext,
        response,
        responseError,
        orderRequest,
        orderType,
        orderTransactionType
    }) {
        setTimeout(() => {
            const extras = {
                orderBasketValidation: responseError && responseError.code || '200',
                orderType,
                orderDelivery: orderRequest.orderCondition.deliveryTypeCode,
                orderTransactionType,
                orderShoppingBasket: shoppingBasket.items
            };
            let eventData: any;
            if (orderType === 'BASKET') {
                eventData = this.analyticService
                    .createTransferBasketEventData(
                        response,
                        extras
                    );
            } else {
                eventData = this.analyticService
                    .createTransferOfferEventData(
                        response,
                        extras
                    );
            }
            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_ORDER_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendReturnOrderEventData(items: any[]) {
        setTimeout(() => {
            const itemsEventData = items.map(item => ({
                returnTransId: +item.transId,
                returnArticleId: item.articleId,
                returnArticleName: item.articleName,
                returnQuantity: +item.returnQuantityFromUser || item.returnQuantity,
                returnReasonOfReturn: item.returnReason,
                returnQuarantine: item.quarantineText,
                returnBranchId: +item.branchId,
                returnCashDiscount: item.cashDiscount,
                returnOrderNr: item.orderNr,
                returnAxPaymentType: item.axPaymentType
            }));

            const eventData = this.analyticService.createFullTextSearchEventData(itemsEventData, {
                isReturnArticleSearchMode: true
            });
            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_ORDER_EVENT)
                .pipe(first())
                .toPromise();
        });
    }
}
