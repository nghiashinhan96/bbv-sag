import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { BehaviorSubject, of, Subscription, Subject, Observable } from 'rxjs';
import { map } from 'rxjs/internal/operators/map';
import { catchError, finalize } from 'rxjs/operators';
import { get } from 'lodash';

import { SHOPPING_BASKET_ENUM } from '../enums/shopping-basket.enum';
import { CreditLimitService } from './credit-limit.service';
import { ErrorCodeEnum } from '../enums/error-code.enum';
import { API_TIMEOUT_EXCEEDED_ERROR, ERP_TIMEOUT_EXCEEDED_ERROR, EMAIL_ERROR, TIMEOUT_EXCEEDED_ERROR } from '../conts/app.constant';
import { SagMessageData } from 'sag-common';
import { SubOrderBasketModel } from '../models/sub-order-basket.model';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ShoppingBasketModel } from '../models/shopping-basket.model';
import { environment } from 'src/environments/environment';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { ARTICLE_TYPE } from 'sag-article-list';
import { ArticleAvailabilityModel } from 'sag-article-detail';
import { BasketItemSource } from 'src/app/analytic-logging/models/basket-item-source.model';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';

@Injectable({
    providedIn: 'root'
})
export class ShoppingBasketService {
    private miniBasketSubject = new BehaviorSubject<ShoppingBasketModel>(null);
    private basketQuantitySubject = new BehaviorSubject<number>(null);
    private toogleMiniBasketSubject = new Subject<boolean>();
    private removeItemInBasketSubject = new Subject<any>();
    private currentSubBasketSubject = new BehaviorSubject<ShoppingBasketModel>(null);
    private responseOrdersSubject = new BehaviorSubject<any>(null);
    updateItemInBasketSubject = new Subject<any>();

    miniBasket: ShoppingBasketModel;
    basketType = SHOPPING_BASKET_ENUM.DEFAULT;
    isEnabled = true;
    responseOrders: any;

    private quantitySubscription: Subscription;
    private minBasketSubscription: Subscription;
    private responseOrdersSubscription: Subscription;
    private baseUrl = environment.baseUrl;
    currentSubBasket: ShoppingBasketModel;

    constructor(
        private http: HttpClient,
        private creditLimitService: CreditLimitService,
        private appStorageService: AppStorageService,
        private fbRecordingService: FeedbackRecordingService,
        private gaService: GoogleAnalyticsService
    ) { }

    initBasket() {
        this.currentSubBasket = null;
        this.currentSubBasketSubject.next(this.currentSubBasket);
        this.miniBasket = null;
        this.miniBasketSubject.next(null);
        this.basketQuantitySubject.next(0);
        this.isEnabled = true;
        this.basketType = SHOPPING_BASKET_ENUM.DEFAULT;
        this.responseOrders = null;
        this.responseOrdersSubject.next(null);
    }

    clearBasket() {
        if (this.basketType === SHOPPING_BASKET_ENUM.FINAL) {
            this.currentSubBasket = null;
            this.currentSubBasketSubject.next(this.currentSubBasket);
            this.appStorageService.subOrderBasket = null;
        } else {
            this.miniBasket = null;
            this.miniBasketSubject.next(null);
            this.basketQuantitySubject.next(0);
        }
        this.appStorageService.goodReceiver = null;
    }

    updateMiniBasket(shoppingBasket: ShoppingBasketModel) {
        this.miniBasketSubject.next(shoppingBasket);
        this.loadCartQuantity();
        // cause the quanity is calculated by server;
        // this.basketQuantitySubject.next(shoppingBasket.numberOfOrderPos);
    }

    loadMiniBasket(callback?: (data: any) => void) {
        const url = `${this.baseUrl}cart/quickview`;
        if (this.minBasketSubscription) {
            this.minBasketSubscription.unsubscribe();
        }
        this.minBasketSubscription = this.http.get(url).pipe(
            catchError(err => of(new ShoppingBasketModel())),
            map((basket: any) => {
                if (basket && basket.items) {
                    this.fbRecordingService.recordShoppingCartItems(basket.items, false);
                }
                return new ShoppingBasketModel(basket);
            })
        ).subscribe(shoppingBasket => {
            this.miniBasket = shoppingBasket;
            this.basketQuantitySubject.next(this.miniBasket.numberOfOrderPos);
            this.miniBasketSubject.next(shoppingBasket);
            if (callback) {
                callback(shoppingBasket);
            }
        });
    }

    loadCartQuantity() {
        const url = `${this.baseUrl}cart/position/count`;
        if (this.quantitySubscription) {
            this.quantitySubscription.unsubscribe();
        }
        this.quantitySubscription = this.http.get(url).pipe(
            catchError(err => of(0))
        ).subscribe((count: number) => {
            this.basketQuantitySubject.next(count);
        });
    }

    updateResponseOrders(orders: any) {
        this.responseOrdersSubject.next(orders);
    }

    get isAllBasketItemsAvailable() {
        return (this.miniBasket && this.miniBasket.items || []).filter(item => {
            if (item.vin || item.itemType === ARTICLE_TYPE.ARTICLE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]) {
                return false;
            }
            return this.hasValidAvailability(item.articleItem.availabilities);
        }).length > 0;
    }

    private hasValidAvailability(availabilities: ArticleAvailabilityModel[]) {
        if (!availabilities || availabilities.length === 0) {
            return false;
        }
        // no backOrder avail
        return availabilities.filter(item => item.backOrder).length === 0;
    }

    get miniBasket$() {
        return this.miniBasketSubject.asObservable();
    }

    get basketQuantity$() {
        return this.basketQuantitySubject.asObservable();
    }

    get toogleMiniBasket$() {
        return this.toogleMiniBasketSubject.asObservable();
    }

    get removedArticles$() {
        return this.removeItemInBasketSubject.asObservable();
    }

    get updateArticles$() {
        return this.updateItemInBasketSubject.asObservable();
    }

    get currentSubBasket$() {
        return this.currentSubBasketSubject.asObservable();
    }

    get finalCustomerId() {
        return get(this.appStorageService.subOrderBasket, 'finalOrder.orgId');
    }

    get responseOrders$() {
        return this.responseOrdersSubject.asObservable();
    }

    setBasketType(type: SHOPPING_BASKET_ENUM) {
        this.basketType = type || SHOPPING_BASKET_ENUM.DEFAULT;
    }

    toggleMiniBasket(isEnabled) {
        this.isEnabled = isEnabled;
        this.toogleMiniBasketSubject.next(this.isEnabled);
    }

    updateOtherProcess(basket) {
        if (this.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
            this.miniBasket = new ShoppingBasketModel(basket);
            this.miniBasketSubject.next(this.miniBasket);
            // this.loadCartQuantity();
            this.basketQuantitySubject.next(this.miniBasket.numberOfOrderPos);
        } else {
            this.currentSubBasket = new ShoppingBasketModel(basket);
            this.currentSubBasketSubject.next(this.currentSubBasket);
        }
        this.creditLimitService.updateCreditCardInfo();
    }

    removeAllBasket() {
        const url = `${this.baseUrl}cart/article/remove/all?shopType=${this.basketType}`;
        return this.http.post(url, null).pipe(
            map((res: any) => {
                const currentMiniBasket = {...this.miniBasket};
                this.updateOtherProcess(null);

                if (res && res.items) {
                    this.fbRecordingService.recordShoppingCartItems(res.items);
                }
                // emit reload in article List
                if(this.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
                    const cartKeys = (currentMiniBasket && currentMiniBasket.items || []).map(item => item.cartKey);
                    this.removeItemInBasketSubject.next(cartKeys);
                }

                return new ShoppingBasketModel();
            }),
            catchError(error => {
                // Due to API return nothing even sucess then to know error return -1
                return of(ErrorCodeEnum.UNKNOWN_ERROR);
            }));
    }

    removeBasketItem(body: { cartKeys: string[], reloadAvail?: boolean }) {
        const url = `${this.baseUrl}cart/article/remove?shopType=${this.basketType}`;
        return this.http.post(url, body).pipe(map((basket: any) => {
            this.gaService.removeFromBasket(this.miniBasket, body.cartKeys);
            this.updateOtherProcess(basket);
            if (basket && basket.items) {
                this.fbRecordingService.recordShoppingCartItems(basket.items);
            }
            // emit reload in article List
            this.removeItemInBasketSubject.next(body.cartKeys);
            return new ShoppingBasketModel(basket);
        }));
    }

    addItemToCart(body) {
        const url = `${this.baseUrl}cart/article/add?shopType=${this.basketType}`;
        return this.http.post(url, body).pipe(map((basket: any) => {
            this.updateOtherProcess(basket);
            if (basket && basket.items) {
                this.fbRecordingService.recordShoppingCartItems(basket.items);
            }
            return new ShoppingBasketModel(basket);
        }));
    }

    updateCartItem(body: any) {
        const url = `${this.baseUrl}cart/article/update?shopType=${this.basketType}`;
        return this.http.post(url, body).pipe(map((basket: any) => {
            this.updateOtherProcess(basket);
            if (basket && basket.items) {
                this.fbRecordingService.recordShoppingCartItems(basket.items);
            }
            return new ShoppingBasketModel(basket);
        }));
    }

    loadCachedBasket(isUsedForFetchingTheLatestData?: boolean): Observable<ShoppingBasketModel> {
        let observe: Observable<ShoppingBasketModel>;
        if (this.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
            const url = `${this.baseUrl}cart/view/cache?shopType=${SHOPPING_BASKET_ENUM.DEFAULT}`;
            observe = this.http.post<ShoppingBasketModel>(url, null).pipe(map((res: any) => {
                if (res && res.items) {
                    this.fbRecordingService.recordShoppingCartItems(res.items);
                }
                return res;
            }));
        } else if (this.basketType === SHOPPING_BASKET_ENUM.FINAL) {
            const url = `${this.baseUrl}cart/v2/checkout?cache=${false}&quickView=false&shopType=${SHOPPING_BASKET_ENUM.FINAL}`;
            observe = this.http.get<ShoppingBasketModel>(url);
        } else {
            observe = of(null);
        }
        return observe.pipe(
            catchError(error => {
                return of(null);
            }),
            map(res => {
                const cachedBasket = new ShoppingBasketModel(res);
                if(isUsedForFetchingTheLatestData) {
                    this.updateOtherProcess(res);
                    return cachedBasket;
                }
                // this cached basket need to refresh the avail
                (cachedBasket.items || []).forEach(cartItem => {
                    if (cartItem.articleItem) {
                        cartItem.articleItem.availabilities = null;
                    }
                });
                return cachedBasket;
            })
        );

    }

    loadShoppingBasket(getRaw?: boolean): Observable<ShoppingBasketModel> {
        let observe: Observable<ShoppingBasketModel>;
        if (this.basketType === SHOPPING_BASKET_ENUM.DEFAULT) {
            const url = `${this.baseUrl}cart/view?shopType=${this.basketType}`;
            observe = this.http.get<ShoppingBasketModel>(url).pipe(map((res: any) => {
                if (res && res.items) {
                    this.fbRecordingService.recordShoppingCartItems(res.items);
                }
                return res;
            }));
        } else if (this.basketType === SHOPPING_BASKET_ENUM.FINAL) {
            const url = `${this.baseUrl}cart/v2/checkout?cache=${false}&quickView=${false}&shopType=${SHOPPING_BASKET_ENUM.FINAL}`;
            observe = this.http.get<ShoppingBasketModel>(url);
        } else {
            observe = of(null);
        }
        return observe.pipe(
            map(res => {
                if (getRaw) {
                    return res;
                }
                this.updateOtherProcess(res);
                return new ShoppingBasketModel(res);
            }),
            catchError(error => {
                return of(null);
            })
        );
    }

    addSubBasket(orderDashboardListItemModel: OrderDashboardListItemModel, basketItemSource?: BasketItemSource) {
        const url = `${this.baseUrl}cart/v2/add/${orderDashboardListItemModel.id}/final-customer?shopType=${this.basketType}`;
        let param = basketItemSource && { basketItemSourceId: basketItemSource.basketItemSourceId, basketItemSourceDesc: basketItemSource.basketItemSourceDesc } || null;
        return this.http.post(url, null, {params: param}).pipe(
            map(res => {
                const basket = new ShoppingBasketModel(res);

                if (this.basketType === SHOPPING_BASKET_ENUM.FINAL) {
                    this.appStorageService.goodReceiver = null;
                    this.appStorageService.subOrderBasket = {
                        items: (basket.items || []).map((item) => {
                            return {
                                quantity: item.quantity,
                                vehicleId: item.vehicleId,
                                articleItem: {
                                    artid: item.articleItem.artid
                                }
                            };
                        }),
                        finalOrder: orderDashboardListItemModel
                    } as SubOrderBasketModel;
                }

                return basket;
            })
        );
    }

    addCoupon(couponCode: string) {
        const url = `${this.baseUrl}coupons/addToCart?couponCode=${couponCode}&shopType=${this.basketType}`;
        return this.http.post(url, null).pipe(
            map(basket => {
                this.updateOtherProcess(basket);
                return new ShoppingBasketModel(basket);
            })
        );
    }

    removeCoupon() {
        const url = `${this.baseUrl}coupons/remove?shopType=${this.basketType}`;
        return this.http.post(url, null).pipe(
            map(basket => {
                this.updateOtherProcess(basket);
                return new ShoppingBasketModel(basket);
            })
        );
    }

    createOfferToAx(body) {
        const url = `${this.baseUrl}order/v2/create/offer`;
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res && res[0]
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: this.getCreateOfferMessage(err && err.error),
                    type: 'ERROR',
                    extras: err
                } as SagMessageData);
            })
        );
    }

    transferBasket(body) {
        const url = `${this.baseUrl}order/v2/create/basket?shopType=${this.basketType}`;
        return this.transferBasketData(url, body);
    }

    getTotalNumberCart(userid, custNr) {
        const url = `${this.baseUrl}cart/total?userid=${encodeURIComponent(userid)}&custNr=${encodeURIComponent(custNr)}`;
        return this.http.get(url);
    }

    private transferBasketData(url, body) {
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res && res[0]
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: this.transferBasketErrorHandler(err && err.error),
                    type: 'ERROR',
                    extras: err
                } as SagMessageData);
            })
        );
    }

    private transferBasketErrorHandler(error) {
        const code = error.error_code || error.message;
        switch (code) {
            case API_TIMEOUT_EXCEEDED_ERROR:
            case ERP_TIMEOUT_EXCEEDED_ERROR:
                return 'ORDER.ERROR.TIMEOUT';
            case EMAIL_ERROR:
                return 'ORDER.ERROR.EMAIL_FAILED';
            default:
                return 'ORDER.ERROR.TRANSFER_FAILED';
        }
    }

    public getCreateOfferMessage(error) {
        switch (error.message) {
            case API_TIMEOUT_EXCEEDED_ERROR:
            case ERP_TIMEOUT_EXCEEDED_ERROR:
                return 'ORDER.ERROR.TIMEOUT';
            case EMAIL_ERROR:
                return 'ORDER.ERROR.EMAIL_FAILED';
            default:
                return 'ORDER.ERROR.CREATE_OFFER_FAILED';
        }
    }
}
