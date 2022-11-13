import { OffersService } from 'src/app/offers/services/offers.services';
import { SubShoppingBasketService } from 'src/app/core/services/sub-shopping-basket.service';
import { of } from 'rxjs/internal/observable/of';
import { catchError } from 'rxjs/internal/operators/catchError';
import { map } from 'rxjs/internal/operators/map';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { isEmpty, uniq } from 'lodash';

import { forkJoin } from 'rxjs';

import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { UserService } from 'src/app/core/services/user.service';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { OFFER_STATUS } from 'src/app/offers/enums/offers.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SagMessageData } from 'sag-common';
import { ORDER_TYPE } from '../../core/enums/shopping-basket.enum';
import {
    Constant,
    API_TIMEOUT_EXCEEDED_ERROR,
    ERP_TIMEOUT_EXCEEDED_ERROR,
    EMAIL_ERROR, NOT_AVAILABLE, TIMEOUT_EXCEEDED_ERROR
} from 'src/app/core/conts/app.constant';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { ShoppingBasketTransferModel } from '../models/shopping-basket-transfer.model';
import { environment } from 'src/environments/environment';
import { AbsSetting } from 'src/app/models/setting/abs-setting.model';
import { ShoppingBasketContextModel } from '../models/shopping-basket-context.model';
import { BasketPriceSummaryModel } from '../models/basket-price-summary-model';
import { CustomerOrderTypeService } from './customer-order-type.service';
import { SalesOrderTypeService } from './sales-order-type.service';
import { OrderDashboardListItemModel } from 'src/app/order-dashboard/models/order-dashboard-list-item.model';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { ArticlesService, AvailabilityUtil, AVAILABILITY_INFO } from 'sag-article-detail';
import { NUMBER_ITEMS_REQUEST_AVAILABILITY, ArticleSortUtil } from 'sag-article-list';
import * as moment from 'moment-timezone';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
@Injectable()
export class ShoppingOrderService {
    private baseUrl = environment.baseUrl;
    private orderTypeContext;

    constructor(
        private http: HttpClient,
        private router: Router,
        private creditLimitService: CreditLimitService,
        private articlesService: ArticlesService,
        private subShoppingBasketService: SubShoppingBasketService,
        private customerOrderTypeService: CustomerOrderTypeService,
        private salesOrderTypeService: SalesOrderTypeService,
        private offersService: OffersService,
        public shoppingBasketService: ShoppingBasketService,
        public appContextService: AppContextService,
        private userService: UserService,
        public appStorage: AppStorageService,
        private fbRecordingService: FeedbackRecordingService,
        private dmsService: ActiveDmsProcessor,
    ) {
        this.orderTypeContext = this.userService.userDetail.isSalesOnBeHalf ? this.salesOrderTypeService : this.customerOrderTypeService;
    }

    get basket() {
        return this.shoppingBasketService.currentSubBasket;
    }

    initOrderConditions(isSalesOnBeHalf: boolean) {
        if (isSalesOnBeHalf) {
            this.appContextService.initAppContext()
                .subscribe((res: any) => this.userService.updateNetPriceSetting(res && res.userPriceContext || {}));
        } else {
            this.appContextService.initAppContext();
        }
    }

    initialTotalSection(basket: ShoppingBasketModel): BasketPriceSummaryModel {
        if (!basket) {
            return {} as BasketPriceSummaryModel;
        }
        const userPrice = this.userService.userPrice;
        return {
            subTotal: basket.initSubTotal(userPrice, this.appStorage.currentStateVatConfirm) || 0,
            vatTotal: basket.initVatTotal(userPrice) || 0,
            totalExclVat: basket.initTotalExclVat(userPrice) || 0,
            total: basket.initTotalInclVat(userPrice) || 0,
            existedCouponCode: basket.couponCode || '',
            discount: basket.discount || 0,
            subTotalExlVat: basket.initSubTotal(userPrice) || 0, // for PDP,
            subTotalWithVat: basket.initSubTotal(userPrice, this.appStorage.currentStateVatConfirm) || 0 // for PDP
        };
    }

    updateOrderedOfferStatus() {
        const offer = this.appStorage.selectedOffer;

        if (offer) {
            offer.status = OFFER_STATUS.ORDERED;
            this.offersService.updateOffer(offer.updateRequestModel, false).subscribe(
                response => {
                    this.appStorage.selectedOffer = null;
                }, err => {
                    console.log(err);
                });
        } else {
            return;
        }
    }

    getOrderRequest(refTextKeyStore: any, customerMessage: string = '', currentBasket?: ShoppingBasketModel) {
        const priceInfo = this.initialTotalSection(currentBasket || this.basket);
        const refTextObj = this.appStorage.refText;
        const custRefTxt = refTextObj && refTextObj[refTextKeyStore] || '';
        const subOrderBasket = this.appStorage.subOrderBasket;

        let orderFrom;
        const dmsInfo = this.dmsService.getDmsInfo();
        if (dmsInfo) {
            orderFrom = DmsUtil.getDMSSalesOrgin();
        }

        return new ShoppingBasketTransferModel({
            context: this.appContextService.shoppingBasketContext,
            custRefTxt,
            msg: customerMessage,
            total: priceInfo.total,
            user: this.userService.userDetail,
            finalCustomer: this.appStorage.goodReceiver,
            saleInfo: this.userService.employeeInfo,
            finalCustomerOrderId: subOrderBasket && subOrderBasket.finalOrder ? subOrderBasket.finalOrder.id : null,
            items: this.buildOrderItems(currentBasket),
            timezone: moment.tz.guess(true),
            orderFrom
        }).orderRequest;
    }

    getSerbiaOrderRequest(currentBasket?: ShoppingBasketModel) {
        const priceInfo = this.initialTotalSection(currentBasket || this.basket);
        const custRefTxt = '';
        const customerMessage = '';
        const subOrderBasket = this.appStorage.subOrderBasket;

        return new ShoppingBasketTransferModel({
            context: this.appContextService.shoppingBasketContext,
            custRefTxt,
            msg: customerMessage,
            total: priceInfo.total,
            user: this.userService.userDetail,
            finalCustomer: this.appStorage.goodReceiver,
            saleInfo: this.userService.employeeInfo,
            finalCustomerOrderId: subOrderBasket && subOrderBasket.finalOrder ? subOrderBasket.finalOrder.id : null,
            items: this.buildOrderItems(currentBasket),
            timezone: moment.tz.guess(true)
        }).serbiaOrderRequest;
    }

    buildDataCheckOutPage(isCounterBasketMode, response: any, extras: any = {}, basket?: ShoppingBasketModel) {
        let navigatePath = '';
        let queryParams = {};
        const orderNrs = response.map(order => order.orderNr).filter(nr => nr);
        const orderTypes = response.map(order => order.orderType).filter(type => type);
        const error = response.some(order => order.errorMsg);
        const warningMsgs = uniq(response.map(order => order.warningMsgCode).filter(msg => msg));
        const orderTimeout = response.filter(order => order.errorMsg && order.errorMsg.key === TIMEOUT_EXCEEDED_ERROR);
        const articleName = this.getArticleName(orderTimeout, basket);

        if (isCounterBasketMode) {
            const counterOrder = response.find(item => item.orderType === ORDER_TYPE.COUNTER);
            navigatePath = counterOrder.workIds && counterOrder.workIds.length > 0 ? Constant.CHECKOUT_PAGE : Constant.HOME_PAGE;

            queryParams = {
                workIds: JSON.stringify(counterOrder.workIds),
                orderTransaction: counterOrder.transactionNr || NOT_AVAILABLE,
                counterPage: ORDER_TYPE.COUNTER,
                orderNumber: orderNrs.length > 0 ? JSON.stringify(orderNrs) : '',
                error: error && orderTimeout.length === 0,
                errorTimeout: orderTimeout.length > 0,
                articleName
            };
        } else {
            const basketContext = this.appContextService.shoppingBasketContext;
            const deliveryTypeCode = basketContext.deliveryType ? basketContext.deliveryType.descCode : '';
            navigatePath = Constant.CHECKOUT_PAGE;

            queryParams = this.userService.userDetail.isFinalUserRole ? {} : {
                isOrderRequest: !extras.isTransferBasket,
                orderType: JSON.stringify(orderTypes),
                deliveryType: deliveryTypeCode,
                orderNumber: orderNrs.length > 0 ? JSON.stringify(orderNrs) : '',
                orderTransaction: response.transactionNr || NOT_AVAILABLE,
                error: error && orderTimeout.length === 0,
                errorTimeout: orderTimeout.length > 0,
                warning: JSON.stringify(warningMsgs),
                articleName
            };
        }

        return { navigatePath, queryParams };
    }

    getArticleName(orderTimeout, basket?: ShoppingBasketModel) {
        let articleName = '';

        if(orderTimeout.length > 0) {
            let cartKeys = [];
            orderTimeout.forEach(order => {
                cartKeys = cartKeys.concat(order.cartKeys);
            });

            const items = (basket.items || []).filter(item => cartKeys.indexOf(item.cartKey) > -1);
            if(items.length > 0) {
                let names = [];

                items.forEach(it => it.articleItem && names.push(`${it.articleItem.supplier} ${it.articleItem.artnrDisplay}`));

                articleName = names.join(',');
            }
        }

        return articleName;
    }

    handleOrderedSuccessful(response: any, refTextKeyStore: any, extras = {}, currentBasket: ShoppingBasketModel) {
        this.creditLimitService.resetCreditCardInfo(this.userService.userDetail);
        this.appStorage.cleanRefText(refTextKeyStore);

        if (this.userService.userDetail.hasWholesalerPermission) {
            if (this.appStorage.subOrderBasket.isOrigin) {
                this.addToOrdered(this.appStorage.subOrderBasket.finalOrder);
                this.subShoppingBasketService.getOverview();
            }
        }

        this.shoppingBasketService.clearBasket();
        this.shoppingBasketService.loadCartQuantity();

        this.initOrderConditions(this.userService.userDetail.isSalesOnBeHalf);

        // Update ordered offer
        this.updateOrderedOfferStatus();

        this.goToCheckOutPage(response, extras, currentBasket);
    }

    requestAvail(requestAvailItems, currentBasket: ShoppingBasketModel) {
        let index = 0;
        const requests = [];
        let availabilities = [];

        while (index < requestAvailItems.length) {
            const requestItemsBatch = requestAvailItems.slice(index, index + NUMBER_ITEMS_REQUEST_AVAILABILITY);
            requests.push(this.articlesService.getArticlesAvailabilityWithBatch(requestItemsBatch, requestItemsBatch.length));
            index += NUMBER_ITEMS_REQUEST_AVAILABILITY;
        }
        if (requests.length) {
            return forkJoin(requests).pipe(map(respones => {
                respones.forEach(res => {
                    const avails = this.articlesService.getDisplayedAvailabilities(res.items);
                    availabilities = [...availabilities, ...avails];
                    if (res.items) {
                        for (const key in res.items) {
                            if (res.items.hasOwnProperty(key)) {
                                const avai = res.items[key].availabilities;
                                this.fbRecordingService.recordAvailability(key, avai, true);
                            }

                        }
                    }
                });
                this.updateCartItemStatus(availabilities, currentBasket);
                return availabilities;
            }));
        } else {
            this.updateCartItemStatus(availabilities, currentBasket);
            return of([]);
        }
    }

    updateAvailInOrderList(avails, currentBasket: ShoppingBasketModel) {
        // updates avail in order list
        currentBasket.items.forEach((item) => {
            item.articleItem.availabilities = this.getAvailByPim(item.articleItem.pimId, avails);
        });
    }

    isSubBasketNotChanged(ignoreDisplayedPrice = false) {
        if (!this.basket) {
            return true;
        }

        if (isEmpty(this.basket.items)) {
            return true;
        }

        return this.basket.compareOrderBasket(
            this.appStorage.subOrderBasket.items || [], ignoreDisplayedPrice
        );
    }

    getOrderType(currentBasket: ShoppingBasketModel, basketContext: ShoppingBasketContextModel) {
        const absSetting = new AbsSetting(
            this.userService.userDetail.settings.customerAbsEnabled,
            this.userService.userDetail.settings.salesAbsEnabled
        );
        return this.orderTypeContext.get(currentBasket, basketContext, absSetting);
    }

    createOrderForFinalCustomer(body) {
        const url = `${this.baseUrl}order/v2/final-customer/create`;
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: this.transferBasketErrorHandler(err && err.error),
                    type: 'ERROR',
                    params: err
                } as SagMessageData);
            })
        );
    }

    createOrder(body, ksoDisabled: boolean) {
        return this.postOrder(body, ksoDisabled);
    }

    transferBasket(body) {
        const basketType = this.shoppingBasketService.basketType;
        const url = `${this.baseUrl}order/v2/create/basket?shopType=${basketType}`;
        return this.transferBasketData(url, body);
    }

    saveOrderChange(body) {
        const url = `${this.baseUrl}order/v2/final-customer/update`;
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: err.message,
                    type: 'ERROR',
                    params: err
                } as SagMessageData);
            })
        );
    }

    private addToOrdered(order: OrderDashboardListItemModel) {
        const newOrdered = [...this.appStorage.ordered];
        newOrdered.push(order);
        this.appStorage.ordered = [...newOrdered];
    }

    private transferBasketData(url, body) {
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res,
                    extras: {
                        isTransferBasket: true
                    }
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: this.transferBasketErrorHandler(err),
                    type: 'ERROR',
                    params: err
                } as SagMessageData);
            })
        );
    }

    public transferBasketErrorHandler(error) {
        switch (error.message) {
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

    private postOrder(body, ksoDisabled: boolean) {
        const basketType = this.shoppingBasketService.basketType;
        const url = `${this.baseUrl}order/v2/create?shopType=${basketType}&ksoDisabled=${ksoDisabled}`;
        return this.http.post(url, body).pipe(
            map(res => {
                return {
                    type: 'SUCCESS',
                    message: res
                } as SagMessageData;
            }),
            catchError(err => {
                return of({
                    message: this.transferBasketErrorHandler(err && err.error),
                    type: 'ERROR',
                    params: err
                } as SagMessageData);
            }));
    }

    private updateCartItemStatus(avails, currentBasket: ShoppingBasketModel) {
        currentBasket.items.forEach(item => {
            let newAvail = this.getAvailByPim(item.articleItem.pimId, avails) || [];
            newAvail = AvailabilityUtil.sortAvailWithLatestTime(newAvail);
            item.isUpdatedAvail = this.isAvailChanged(item.articleItem.availabilities, newAvail);
        });
    }

    private getAvailByPim(pimId, avails) {
        const availItem = avails.filter(item => item.key === pimId)[0];
        return availItem ? availItem.value : null;
    }

    private isAvailChanged(currentAvail, newAvail) {
        const currentAvailState = AvailabilityUtil.intialAvailSortState(currentAvail);
        const newAvailState = AvailabilityUtil.intialAvailSortState(newAvail);
        if (currentAvailState !== newAvailState) {
            return true;
        }
        if (currentAvailState === AVAILABILITY_INFO.IS_TOUR_STATE
            && !!currentAvail && currentAvail.length > 0
            && !!newAvail && newAvail.length > 0
            && currentAvail[0].arrivalTime !== newAvail[0].arrivalTime) {
            return true;
        }
        return false;
    }

    private buildOrderItems(currentBasket: ShoppingBasketModel) {
        return (currentBasket && currentBasket.items || []).filter(item => item.additionalTextDoc).map(item => {
            return {
                additionalTextDoc: item.additionalTextDoc,
                cartKey: item.cartKey
            };
        });
    }

    private goToCheckOutPage(response: any, extras = {}, basket?: ShoppingBasketModel) {
        const isCounterBasketMode = response.some(item => item.orderType === ORDER_TYPE.COUNTER);

        const page = this.buildDataCheckOutPage(isCounterBasketMode, response, extras, basket);

        if (page.navigatePath) {
            this.router.navigate([page.navigatePath], {
                queryParams: page.queryParams
            });
        }
    }
}
