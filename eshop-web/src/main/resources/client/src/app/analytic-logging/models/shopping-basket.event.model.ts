import { MetadataLogging } from './analytic-metadata.model';
import { AnalyticEventType, ShoppingBasketEventType } from '../enums/event-type.enums';
import { ShoppingBasketItemEvent } from './shopping-basket-item.event.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { EventUtil } from '../utils/event.util';
import { BasketItemSource } from './basket-item-source.model';

export class ShoppingBasketEvent extends MetadataLogging {
    eventType = AnalyticEventType.SHOPPING_BASKET_EVENT.toString();
    basketTotalValue: number;
    basketTotalItems: ShoppingBasketItemEvent[] = [];
    basketItemEventSource: string;
    basketItemAdded: ShoppingBasketItemEvent[] = [];
    basketItemValue: number;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(
        metadata?: MetadataLogging | any,
        user?: UserDetail,
        data?: any,
        sourceEvent?: string,
        selectedArticleIds?: string[],
        totalPrice?: number,
        isNettoPriceDisplay?: boolean,
        basketItemSource?: BasketItemSource
    ) {
        super(metadata, user);
        this.basketItemAdded = [];
        this.basketItemEventSource = sourceEvent;
        if (data) {
            this.basketItemValue = totalPrice;
            const existedArticleId = new Set(selectedArticleIds);
            const dataItems: any[] = data.items ? data.items : data;
            dataItems.forEach(item => {
                const event = new ShoppingBasketItemEvent(item, isNettoPriceDisplay, null, basketItemSource);
                if (existedArticleId.has(event.basketItemArticleId)) {
                    this.basketItemAdded.push(event);
                }
            });
            if (!this.basketItemValue && this.basketItemAdded.length) {
                this.basketItemValue = +this.basketItemAdded
                    .map(item => item.basketItemTotalPrice)
                    .reduce((pre, cur) => pre + cur, 0).toFixed(2);
            }
        }
        if (basketItemSource) {
            this.basketItemSourceId = basketItemSource.basketItemSourceId;
            this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
        }
    }

    buildShoppingBasketEventData(data: any, isNettoPriceDisplay?: boolean, customBrand?: any) {
        if (data && data.items) {
            this.basketTotalValue = data.subTotalWithNetAndVat;
        } else {
            this.basketTotalValue = (data || []).reduce((item, result) => {
                return result + (item && +item.subTotalWithNetAndVat || 0);
            }, 0);
        }
        const dataItems: any[] = data && data.items ? data.items : data;
        this.basketTotalItems = dataItems.map(item => new ShoppingBasketItemEvent(item, isNettoPriceDisplay, customBrand));

        return this;
    }

    buildInvoiceEventData(selectedArticles: string[], data: any, sourceEvent: string, isNettoPriceDisplay?: boolean) {
        this.basketItemEventSource = sourceEvent;

        const foundArticles = data.items.filter(item => {
            const found = selectedArticles.filter(article => {
                if (article === item.articleItem.artid) {
                    return article;
                }
            });
            if (found.length) {
                return item;
            }
        });
        const basketItemSource = {basketItemSourceId: this.basketItemSourceId, basketItemSourceDesc: this.basketItemSourceDesc};
        this.basketItemAdded = foundArticles.map(filtered => new ShoppingBasketItemEvent(filtered, isNettoPriceDisplay, null, basketItemSource));
        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);

        return this;
    }

    buildSavedBasketEventData(data: any, sourceEvent: string, isNettoPriceDisplay?: boolean) {
        this.basketItemAdded = [];
        this.basketItemEventSource = sourceEvent;

        this.basketItemAdded =
            data.map(filtered => new ShoppingBasketItemEvent(filtered, isNettoPriceDisplay, {brand: filtered.vehicle.vehicleBrand}));

        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);

        return this;
    }

    buildOfferEventData(data: any, isNettoPriceDisplay?: boolean) {
        const dataItems: any[] = data && data.items ? data.items : data;

        this.basketItemValue = dataItems
            .map((item) => (item.totalGrossPrice))
            .reduce((pre, cur) => pre + cur, 0);
        this.basketItemAdded = dataItems.map(item => {
            item.vehicle = {
                vehid: item.connectVehicleId
            };
            return new ShoppingBasketItemEvent(item, isNettoPriceDisplay);
        });

        return this;
    }

    buildArticleListEventData(addedArticle: any, sourceEvent: string, isNettoPriceDisplay?: boolean, customBrand?: any, basketItemSource?: BasketItemSource) {
        this.basketItemEventSource = sourceEvent;
        this.basketItemAdded = [new ShoppingBasketItemEvent(addedArticle, isNettoPriceDisplay, customBrand, basketItemSource)];

        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);

        return this;
    }

    buildShoppingBasketPageEventData(addedArticle: any, isNettoPriceDisplay?: boolean, customBrand?: any) {
        this.basketItemEventSource = ShoppingBasketEventType.BASKET.toString();
        if (Array.isArray(addedArticle)) {
            this.basketItemAdded = addedArticle.map(art => new ShoppingBasketItemEvent(art, isNettoPriceDisplay, customBrand));
        } else {
            this.basketItemAdded = [new ShoppingBasketItemEvent(addedArticle, isNettoPriceDisplay, customBrand)];
        }

        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);

        return this;
    }

    buildThulePageEventData(data: any, isNettoPriceDisplay?: boolean) {
        this.basketItemEventSource = ShoppingBasketEventType.THULE;
        const basketItemSource = {basketItemSourceId: this.basketItemSourceId, basketItemSourceDesc: this.basketItemSourceDesc};
        this.basketItemAdded = (data || []).map(filtered => new ShoppingBasketItemEvent(filtered, isNettoPriceDisplay, null, basketItemSource));
        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);

        return this;
    }

    buildFastScanEventData(data: any, isNettoPriceDisplay?: boolean) {
        this.basketItemAdded = [];
        this.basketItemEventSource = ShoppingBasketEventType.FAST_SCAN;
        const dataItems: any[] = [data];
        this.basketItemAdded = dataItems.map(d => new ShoppingBasketItemEvent(d, isNettoPriceDisplay));
        this.basketItemValue = this.basketItemAdded
            .map((item) => (item.basketItemTotalPrice))
            .reduce((pre, cur) => pre + cur, 0);
        return this;
    }

    toShoppingBasketEventRequest() {
        if (this.basketItemAdded) {
            const request = super.toRequest();

            return {
                ...request,
                basket_item_event_source: this.basketItemEventSource,
                basket_item_value: this.basketItemValue && (+this.basketItemValue || 0).toFixed(2),
                basket_item_added: this.basketItemAdded.map(item => {
                    return EventUtil.normalize(item.toEventRequest());
                }),
                basket_item_source_id: this.basketItemSourceId,
                basket_item_source_desc: this.basketItemSourceDesc
            };
        }
    }

    toWholeShoppingBasketEventRequest(isNettoPriceDisplay?: boolean) {
        this.eventType = AnalyticEventType.SHOPPING_BASKET_EVENT;
        if (this.basketTotalItems) {
            const request = super.toRequest();
            return {
                ...request,
                basket_item_event_source: this.basketItemEventSource,
                basket_total_value: this.basketTotalValue && (+this.basketTotalValue || 0).toFixed(2),
                basket_total_items: this.basketTotalItems.map(item => {
                    return EventUtil.normalize(item.toEventRequest());
                })
            };
        }
    }
}
