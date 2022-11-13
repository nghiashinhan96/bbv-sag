import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType } from '../enums/event-type.enums';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { OrderBasketItemEvent } from './order-basket-item.event.model';
import { EventUtil } from '../utils/event.util';
import { get } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

export class OrderEvent extends MetadataLogging {
    eventType = AnalyticEventType.SHOPPING_ORDER_EVENT;
    orderId: string;
    orderBasketValidation: string;
    orderType: string;
    orderDelivery: string;
    orderTransactionType: string;
    orderShoppingBasket: OrderBasketItemEvent[] = [];

    constructor (metadata: MetadataLogging | any, user: UserDetail, data: any, extras?: any) {
        super(metadata, user);
        if (data) {
            this.orderId = data.orderNr || data.frontEndBasketNr;
        }
        if (extras) {
            this.orderBasketValidation = extras.orderBasketValidation;
            this.orderType = extras.orderType;
            this.orderDelivery = extras.orderDelivery;
            this.orderTransactionType = extras.orderTransactionType;
            this.orderShoppingBasket = extras.orderShoppingBasket.map((item: any) => {
                if (AffiliateUtil.isSb(environment.affiliate)) {
                    const orderAvailabilities = get(data, 'orderAvailabilities');
                    if (orderAvailabilities && orderAvailabilities.length > 0 && item.articleItem) {
                        const articleRes = orderAvailabilities.find(art => art.article.artid === item.articleItem.artid);
                        if (articleRes) {
                            item.quantity = articleRes.availabilityResponse.quantity;
                        }
                    }
                }

                return new OrderBasketItemEvent(item)
                    .buildTotalPriceEventData(extras.isNettoPriceDisplay);
            });
        }
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            order_id: this.orderId,
            order_basket_validation: this.orderBasketValidation,
            order_type: this.orderType,
            order_delivery: this.orderDelivery,
            order_transaction_type: this.orderTransactionType,
            order_shopping_basket: this.orderShoppingBasket.map(item => EventUtil.normalize(item.toEventRequest()))
        };
    }

    toTransferBasketEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            axedit_order_id: this.orderId,
            axedit_order_basket_validation: this.orderBasketValidation,
            axedit_order_delivery: this.orderDelivery,
            axedit_order_transaction_type: this.orderTransactionType,
            axedit_order_shopping_basket: this.orderShoppingBasket.map(item => EventUtil.normalize(item.toEventRequest()))
        };
    }

    toTransferOfferEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            axoffer_basket_validation: this.orderBasketValidation,
            axoffer_delivery: this.orderDelivery,
            axoffer_transaction_type: this.orderTransactionType,
            axoffer_shopping_basket: this.orderShoppingBasket.map(item => EventUtil.normalize(item.toEventRequest()))
        };
    }
}
