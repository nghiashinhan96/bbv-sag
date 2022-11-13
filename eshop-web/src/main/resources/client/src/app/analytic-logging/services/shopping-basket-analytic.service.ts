import { Injectable } from '@angular/core';
import { first } from 'rxjs/operators';
import uuid from 'uuid/v4';
import { AnalyticLoggingService } from './analytic-logging.service';
import { GoogleAnalyticsService } from './google-analytics.service';
import { ShoppingBasketEventType, AnalyticEventType } from '../enums/event-type.enums';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ArticleBasketModel } from 'sag-article-detail';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';

@Injectable({ providedIn: 'root' })
export class ShoppingBasketAnalyticService {
    constructor(
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService
    ) { }

    sendAddArticleGaData(miniBasket: ShoppingBasketModel, data: ArticleBasketModel) {
        setTimeout(() => {
            if (!miniBasket || !miniBasket.items || !miniBasket.items.length) {
                return;
            }
            const changedArt = miniBasket.items
                .find(basketItem => basketItem.cartKey === data.cartKey);
            if (changedArt && changedArt.articleItem) {
                const index = miniBasket.items.indexOf(changedArt);
                const gaArticle = {
                    ...changedArt.articleItem,
                    index
                };
                const googleAnalyticsArgs = this.gaService.prepareProductFieldObjectArgs(gaArticle, data.amount);
                this.gaService.updateBasketProduct(googleAnalyticsArgs, data.rootModalName || '');
            }
        });
    }

    sendShoppingBasketEvent(article: ShoppingBasketItemModel, cartItems: any[], customBrands?: any, callback?: Function) {
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                article,
                {
                    source: ShoppingBasketEventType.SHOPPING_BASKET_PAGE,
                    customBrands
                }
            );

            if (callback) {
                callback(eventData);
            }

            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .subscribe();
        });
    }

    sendCustomPriceShoppingBasketEvent(cartItems: ShoppingBasketItemModel[]) {
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                cartItems,
                {
                    source: ShoppingBasketEventType.SHOPPING_BASKET_PAGE
                }
            );

            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .subscribe();
        });
    }

    sendConfirmOrderEventData(currentBasket: ShoppingBasketModel) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                currentBasket,
                {
                    source: ShoppingBasketEventType.BASKET
                }
            );

            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendAddCouponEventData(basket: ShoppingBasketModel) {
        if (!basket || !basket.items) {
            return;
        }
        setTimeout(() => {
            const couponeEventData = this.analyticService.createCouponEventData(basket);
            this.analyticService
                .postEventFulltextSearch(couponeEventData, AnalyticEventType.COUPON_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendShoppingOrderEventData(
        currentBasket: ShoppingBasketModel,
        error: any,
        response: any,
        deliveryTypeCode: string,
        orderType: string,
        orderTrans: any
    ) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }

        const cartKeys = response && response.cartKeys || [];

        setTimeout(() => {
            const extras = {
                orderBasketValidation: error && error.code || 200,
                orderType,
                orderDelivery: deliveryTypeCode || null,
                orderTransactionType: orderTrans,
                orderShoppingBasket: currentBasket.items.filter(item => cartKeys.indexOf(item.cartKey) !== -1)
            };
            const eventData = this.analyticService.createShoppingOrderEventData(response, extras);
            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_ORDER_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendThuleEventData(basketItems: any[]) {
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                basketItems,
                {
                    source: ShoppingBasketEventType.THULE
                }
            );
            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendFastscanEventData(selectedArticleId: string, cartItems: any) {
        setTimeout(() => {
            const updatedArticle = cartItems.filter(item => item.articleItem.pimId === selectedArticleId);

            const eventData = this.analyticService.createShoppingBasketEventData(
                updatedArticle[0],
                {
                    source: ShoppingBasketEventType.FAST_SCAN
                }
            );

            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }

    sendShoppingOrderGaData(currentBasket: ShoppingBasketModel, response: any) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }
        setTimeout(() => {
            const purchaseActionDataArgs = this.buildPurchasedActionDataArgs(currentBasket, response);
            const orderNumber = response && response.orderNr || `timeout_${uuid()}`;
            purchaseActionDataArgs.summary.transaction_id = orderNumber;
            this.gaService.purchase(purchaseActionDataArgs);
        });
    }

    sendAddShoppingInfoEventData(currentBasket: ShoppingBasketModel, shoppingBasketContext: ShoppingBasketContextModel) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }
        setTimeout(() => {
            const data = {
                articles: this.createPurchaseActionData(currentBasket.items),
                value: currentBasket.subTotalWithNet
            }
            this.gaService.addShippingInfo(data, shoppingBasketContext);
        });
    }

    sendAddPaymentInfoEventData(currentBasket: ShoppingBasketModel, shoppingBasketContext: ShoppingBasketContextModel) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }
        setTimeout(() => {
            const data = {
                articles: this.createPurchaseActionData(currentBasket.items),
                value: currentBasket.subTotalWithNet
            }
            this.gaService.addPaymentInfo(data, shoppingBasketContext);
        });
    }

    private buildPurchasedActionDataArgs(currentBasket: ShoppingBasketModel, response: any) {
        const cartKeys = response && response.cartKeys || [];
        const result = {
            articles: this.createPurchaseActionData(currentBasket.items.filter(item => cartKeys.indexOf(item.cartKey) !== -1)),
            summary: {
                transaction_id: '',
                value: response.subTotalWithNet,
                tax: response.vatTotalWithNet
            }
        };
        return result;
    }

    private createPurchaseActionData(shoppingBasketItems: any) {
        const actionData = [];
        shoppingBasketItems.forEach(basketItem => {
            const article = basketItem.articleItem;
            if (basketItem.basketItemSourceDesc && basketItem.basketItemSourceId) {
                article.basketItemSourceDesc = basketItem.basketItemSourceDesc;
                article.basketItemSourceId = basketItem.basketItemSourceId;
            }
            actionData.push(this.gaService.createProductFieldObject(article));
        });
        return actionData;
    }

    sendExportOciEventData(currentBasket: ShoppingBasketModel) {
        if (!currentBasket || !currentBasket.items) {
            return;
        }
        setTimeout(() => {
            const eventData = this.analyticService.createShoppingBasketEventData(
                currentBasket,
                {
                    source: ShoppingBasketEventType.OCI
                }
            );

            this.analyticService
                .postEventFulltextSearch(eventData, AnalyticEventType.SHOPPING_BASKET_EVENT)
                .pipe(first())
                .toPromise();
        });
    }
}
