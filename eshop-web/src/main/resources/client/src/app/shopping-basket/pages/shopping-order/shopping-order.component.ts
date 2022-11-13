import { switchMap } from 'rxjs/internal/operators/switchMap';
import { of } from 'rxjs/internal/observable/of';
import { Observable } from 'rxjs/internal/Observable';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';

import { map, tap, catchError } from 'rxjs/operators';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';

import { FinalCustomerAddressModel } from 'src/app/final-customer/models/final-customer-address.model';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { ShoppingBasketInfo, ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { ShoppingBasketRefModel } from 'src/app/shopping-basket/models/shopping-basket-ref.model';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { SubOrderBasketModel } from 'src/app/core/models/sub-order-basket.model';
import { SubShoppingBasketService } from 'src/app/core/services/sub-shopping-basket.service';
import { environment } from 'src/environments/environment';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { DateUtil } from 'src/app/core/utils/date.util';
import { TIME_OUT_MESSAGE, TIMEOUT_EXCEEDED_ERROR, APP_SHOPPING_CART_UPDATED_EVENT } from 'src/app/core/conts/app.constant';
import { BasketPriceSummaryModel } from '../../models/basket-price-summary-model';
import { ShoppingOrderService } from '../../services/shopping-order.service';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { DmsExportCommand } from 'src/app/dms/enums/dms.enum';
import { ExportOrder } from 'src/app/dms/models/export-order.model';
import { ShoppingBasketUtil } from '../../utils/shopping-basket.util';
import { throwError } from 'rxjs';
import { ShoppingBasketAnalyticService } from 'src/app/analytic-logging/services/shopping-basket-analytic.service';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { AffiliateEnum, AffiliateUtil, SagConfirmationBoxComponent, SagMessageData, BroadcastService } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';
import { ARTICLE_TYPE } from 'sag-article-list';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { get } from 'lodash';
import { ArticleListSearchStorageService } from 'src/app/article-list/services/article-list-storage.service';
import { AppCommonService } from 'src/app/core/services/app-common.service';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ArticleModel, AvailabilityUtil } from 'sag-article-detail';
import { OrderingUtil } from 'src/app/core/utils/ordering.util';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { OptimizelyService } from 'src/app/analytic-logging/services/optimizely.service';

const ORDER_TYPE_BASKET = 'BASKET';
const ORDER_TYPE_ORDER = 'ORDER';

@Component({
  selector: 'connect-shopping-order',
  templateUrl: './shopping-order.component.html',
  styleUrls: ['./shopping-order.component.scss']
})
export class ShoppingOrderComponent implements OnInit, OnDestroy {
  referenceExpanded = true;
  conditionExpanded = true;
  deliveryExpanded = true;
  addressExpanded = true;

  // key of localstorage of reference text
  refTextKeyStore: number | string;

  // error message
  errorData = [];

  // summary section
  basketPriceSummary: BasketPriceSummaryModel;

  sucessCoupon: SagMessageData = null;
  shoppingBaketData$: Observable<any>;

  customerMessage = '';

  customerAddress$: Observable<FinalCustomerAddressModel>;
  basketContext: ShoppingBasketContextModel;

  shoppingBasketInfo: ShoppingBasketInfo = null;
  subOrderBasket: SubOrderBasketModel = null;

  currentBasket: ShoppingBasketModel;

  subBasketCustomerRef: string;
  subBasketCustomerMesage: string;

  affiliateCode = environment.affiliate;
  cz = AffiliateEnum.CZ;
  ehcz = AffiliateEnum.EH_CZ;
  ehaxcz = AffiliateEnum.EH_AX_CZ;
  isPDP = AffiliateUtil.isAffiliateApplyPDP(environment.affiliate);

  userDetail: UserDetail;
  isCheckingout = false;
  isDisabledCheckout = false;
  private subs = new SubSink();
  private dmsInfo: DmsInfo;
  private labourTimes: any;
  sb = AffiliateEnum.SB;
  isSB: boolean;
  isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
  isCz = AffiliateUtil.isCz(environment.affiliate);
  isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
  hasItemNotAllowToAddToShoppingCart = false;

  constructor(
    private activatedRoute: ActivatedRoute,
    private location: Location,
    private modalService: BsModalService,
    public shoppingOrderService: ShoppingOrderService,
    public shoppingBasketService: ShoppingBasketService,
    public subShoppingBasketService: SubShoppingBasketService,
    private dmsService: ActiveDmsProcessor,
    public userService: UserService,
    public appStorage: AppStorageService,
    private shoppingBasketAnalyticService: ShoppingBasketAnalyticService,
    private gaService: GoogleAnalyticsService,
    private articleListSearchStorageService: ArticleListSearchStorageService,
    private appCommonService: AppCommonService,
    private broadcaster: BroadcastService,
    private optimizelyService: OptimizelyService
  ) {
    this.isSB = AffiliateUtil.isSb(environment.affiliate);

    if (this.isSB) {
      this.subs.sink = this.shoppingOrderService.appContextService.shoppingBasketContext$
        .subscribe((context: ShoppingBasketContextModel) => {
          this.basketContext = new ShoppingBasketContextModel(context);
        });
    }
  }

  ngOnInit() {
    this.dmsInfo = this.dmsService.getDmsInfo();
    this.dmsService.laboursTimeSubscriber$.subscribe(res => {
      if (res.length > 0) {
        this.labourTimes = res;
      }
    });
    this.userDetail = this.userService.userDetail;
    this.subOrderBasket = this.appStorage.subOrderBasket;
    this.refTextKeyStore = this.activatedRoute.snapshot.queryParams.id || this.userDetail.custNr;

    this.getShoppingBasketData();
    this.getShoppingContext();
    this.subs.sink = this.userService.userPrice$.subscribe(userPrice => {
      this.initialTotalSection(this.currentBasket);
    });
    this.subs.sink = this.broadcaster.on(APP_SHOPPING_CART_UPDATED_EVENT)
      .subscribe((data: ShoppingBasketModel) => {
        this.getShoppingBasketData();
      });
  }

  getShoppingContext() {
    this.basketContext = this.shoppingOrderService.appContextService.shoppingBasketContext;
  }

  getShoppingBasketData() {
    SpinnerService.start('.connect-shopping-order', { class: 'min-height-200-px' });

    this.shoppingBaketData$ = this.shoppingBasketService.loadShoppingBasket().pipe(
      map(basket => {
        if (!basket) {
          SpinnerService.stop('.connect-shopping-order');

          return { data: [], dvse: [] };
        }
        const data = basket.vehicleGroup();
        const dvse = basket.devseGroup();
        this.currentBasket = basket;
        let options;
        const isCz = AffiliateUtil.isCz(environment.affiliate);
        if (isCz) {
          options = {
            defaultArrivalTime: 0
          };
        }
        this.initialTotalSection(basket);
        this.getShoppingBasketRefRes(basket);
        this.handleDVSEArticles(basket);
        this.shoppingBasketInfo = basket.getSummary(options);

        SpinnerService.stop('.connect-shopping-order');
        const articles = (basket && basket.items).map(item => {
          return {
            ...item.articleItem,
            basketItemSourceDesc: item.basketItemSourceDesc,
            basketItemSourceId: item.basketItemSourceId,
          }
        });
        this.gaService.beginCheckout({ articles, summary: this.basketPriceSummary });
        this.hasItemNotAllowToAddToShoppingCart = this.isAffiliateApplyFgasAndDeposit ? OrderingUtil.preventOrderingWhenHasItemNotAllowToAddToShoppingCart(basket) : false;

        return { data, dvse };
      })
    );
  }

  initialTotalSection(basket: ShoppingBasketModel) {
    this.basketPriceSummary = this.shoppingOrderService.initialTotalSection(basket);

    if (this.basketPriceSummary.discount !== 0 && this.basketPriceSummary.existedCouponCode) {
      this.sucessCoupon = {
        type: 'SUCCESS',
        message: 'COUPON.SUCCESS',
        icon: 'fa-check-circle-o'
      };
    } else {
      this.sucessCoupon = null;
    }
  }

  getShoppingBasketRefRes(basket: ShoppingBasketModel) {
    const subOrderBasket = this.subOrderBasket;
    let basketRefRes: ShoppingBasketRefModel = null;

    this.setRefreIntoBasketItem(basket, basketRefRes);

    if (subOrderBasket && subOrderBasket.items) {
      this.subs.sink = this.subShoppingBasketService.getSubBasketRef(subOrderBasket.finalOrder.id)
        .subscribe(data => {
          basketRefRes = data;
          this.subBasketCustomerRef = data.reference;
          this.subBasketCustomerMesage = data.branchRemark;
          this.setRefreIntoBasketItem(basket, basketRefRes);
        });
    }
  }

  handleOrderBehaviour(callback) {
    let result: Observable<any>;
    const spinner = SpinnerService.startApp();
    if (this.userDetail.isFinalUserRole) {
      const orderRequest = this.isSB ? this.getSerbiaOrderRequest() : this.getOrderRequest();
      result = this.placeOrderIfValid(orderRequest);
    } else {
      result = this.checkAvailStatesAndProcessOrder().pipe(switchMap(isPlaceOrder => {
        if (isPlaceOrder) {
          this.errorData = [];
          const orderRequest = this.getFinalCustomerOrderRequest();
          return this.placeOrderIfValid(orderRequest);
        }
        return of({
          message: 'UNKNOWN',
          type: 'WARNING'
        } as SagMessageData);
      }));
    }

    result
      .pipe(tap(() => SpinnerService.stop(spinner)))
      .subscribe((response) => {
        if (this.isSB) {
          this.shoppingBasketService.updateResponseOrders(response && response.message || null);
        }

        if (response.type === 'WARNING') {
          callback(false);
        } else if (response.type === 'SUCCESS') {
          const dataResponse = response.message || [];
          const extras = response.extras || {};
          if (this.dmsInfo) {
            const orderNumber = (dataResponse[0] && dataResponse[0].orderNr) ? dataResponse[0].orderNr : '';
            this.handleDmsExport(orderNumber);
            return;
          }
          this.articleListSearchStorageService.clearAllHistory(this.userDetail.id);
          this.shoppingOrderService.handleOrderedSuccessful(dataResponse, this.refTextKeyStore, extras, this.currentBasket);
        } else {
          const isForeDisabledIfNotTimeOutMessage = TIME_OUT_MESSAGE.indexOf(response.message) === -1;
          callback(isForeDisabledIfNotTimeOutMessage);
        }
      });
  }

  backToViewShoppinBasket() {
    this.location.back();
  }

  onCurrentNetPriceChange() {
    this.userService.toggleNetPriceView();
  }

  updateShoppingBasketContext({ body, reload, done }) {
    this.subs.sink = this.shoppingOrderService.appContextService.updateShoppingBasketContext(body).subscribe(res => {
      if (!!done) {
        done();
      }
    });
  }

  private handleDVSEArticles(basket: ShoppingBasketModel) {
    if (this.userDetail.normalUser) {
      const dvseMsg = [];
      const nonAvailMsg = [];

      for (let item of (basket.items || [])) {
        if (item.itemType === ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE]) {
          dvseMsg.push(basket.getArticlesNoReferencedMessage(item, this.appCommonService.getNotReferencedText()));
        } else {
          if (this.checkConditionGetCusmerMsgWithPromotion(item)) {
            nonAvailMsg.push(basket.getArticlesNoReferencedMessage(item, this.appCommonService.getPromotionText()));
          }
        }
      }

      this.customerMessage = nonAvailMsg.concat(dvseMsg).join('\n');
    }
  }

  private checkConditionGetCusmerMsgWithPromotion(item: ShoppingBasketItemModel) {
    if (this.isPDP && item.articleItem.showPromotionBanner()) {
      return !this.hasAvail(item.articleItem, item.articleItem.availabilities);
    }

    return false;
  }

  private hasAvail(article: ArticleModel, availabilities?: any) {
    if (article) {
      const isArticleCon = AvailabilityUtil.isArticleCon(availabilities);
      if (this.isCz) {
        return AvailabilityUtil.hasAvailCz(availabilities) && !isArticleCon;
      } else {
        return !AvailabilityUtil.articleNoAvail(article) && !isArticleCon;
      }
    }
  }

  private setRefreIntoBasketItem(basket: ShoppingBasketModel, basketRefRes: ShoppingBasketRefModel) {
    if (basket) {
      (basket.items || []).forEach(cartItem => {
        const posRefText = basketRefRes && basketRefRes.items.find(ref => {
          return ref.articleId === cartItem.articleItem.id && (!cartItem.vehicleId || ref.vehicleId === cartItem.vehicleId);
        });
        if (!!posRefText) {
          cartItem.refText = posRefText.reference;
        }
      });
    }
  }

  private processFinalCustomerOrder() {
    const orderRequest = this.isSB ? this.getSerbiaOrderRequest() : this.getOrderRequest();
    return this.shoppingOrderService.createOrderForFinalCustomer(orderRequest)
      .pipe(tap(res => {
        if (res.type === 'SUCCESS') {
          this.shoppingBasketAnalyticService.sendShoppingOrderGaData(this.currentBasket, res.message);
        }
      }));
  }

  private checkAvailStatesAndProcessOrder() {
    const orderArticles = this.getOrderArticles();

    return this.shoppingOrderService.requestAvail(orderArticles, this.currentBasket).pipe(map(avails => {
      if (this.currentBasket.items.filter(item => item.isUpdatedAvail).length > 0) {
        if (this.userDetail.isFinalUserRole) {
          this.shoppingOrderService.updateAvailInOrderList(avails, this.currentBasket);
          return true;
        }
        this.showAvailCheckerModal(avails);
        return false;
      }
      return true;
    }));
  }

  private getOrderArticles() {
    return (this.currentBasket.items || []).map((item) => {
      return item.articleItem;
    });
  }

  private showAvailCheckerModal(avails) {
    this.modalService.show(SagConfirmationBoxComponent, {
      initialState: {
        title: '',
        message: 'SHOPPING_BASKET.UPDATE_AVAIL_MESSAGE',
        showCancelButton: false,
        close: () => {
          this.shoppingOrderService.updateAvailInOrderList(avails, this.currentBasket);
        }
      },
      ignoreBackdropClick: true
    });
  }

  private getOrderRequest() {
    return this.shoppingOrderService.getOrderRequest(this.refTextKeyStore, this.customerMessage, this.currentBasket);
  }

  private getSerbiaOrderRequest() {
    return this.shoppingOrderService.getSerbiaOrderRequest(this.currentBasket);
  }

  private getFinalCustomerOrderRequest() {
    const orderRequest = this.isSB ? this.getSerbiaOrderRequest() : this.getOrderRequest();

    if (this.userDetail.hasWholesalerPermission) {
      if (Object.keys(this.shoppingOrderService.appStorage.subOrderBasket).length > 0) {
        if (this.shoppingOrderService.isSubBasketNotChanged(true)) {
          this.shoppingOrderService.appStorage.subOrderBasket.isOrigin = true;
        } else {
          delete orderRequest.finalCustomerOrderId;
        }
      }
    }

    orderRequest.requestDateTime = DateUtil.getCurrentDateTime();

    return orderRequest;
  }

  private placeOrderIfValid(orderRequest) {
    if (!this.basketContext.billingAddress) {
      return of({
        message: '',
        type: 'ERROR'
      } as SagMessageData);
    }

    return this.sendOrderRequest(orderRequest);
  }

  private sendOrderRequest(orderRequest) {
    let deliveryTypeCode = get(orderRequest, 'orderCondition.deliveryTypeCode');

    const orderType = this.shoppingOrderService.getOrderType(this.currentBasket, this.basketContext);
    return this.shoppingOrderService.createOrder(orderRequest, !!this.dmsInfo).pipe(
      tap(response => {
        if (response.type === 'SUCCESS') {
          const orders = response && response.message || [];
          orders.forEach(order => {
            if (this.isSB) {
              deliveryTypeCode = this.getDeliveryCodeByLocation(order);
            }

            if (order.errorMsg) {
              this.shoppingBasketAnalyticService.sendShoppingOrderEventData(
                this.currentBasket, order.errorMsg, null, deliveryTypeCode, order.orderExecutionType, order.orderType
              );

              if (!this.checkExistErrorMsg(this.shoppingOrderService.transferBasketErrorHandler(order.errorMsg))) {
                this.errorData.push({
                  type: 'ERROR',
                  message: this.shoppingOrderService.transferBasketErrorHandler(order.errorMsg)
                });
              }
            } else {
              this.shoppingBasketAnalyticService.sendShoppingOrderEventData(
                this.currentBasket, null, order, deliveryTypeCode, order.orderExecutionType, order.orderType
              );
            }
            this.shoppingBasketAnalyticService.sendShoppingOrderGaData(this.currentBasket, order);
          });
          this.optimizelyService.addRevenue(orders);
        }
        if (response.type === 'ERROR') {
          this.getErrorMsg(response);

          this.shoppingBasketAnalyticService.sendShoppingOrderEventData(
            this.currentBasket, response.params, null, orderRequest, ORDER_TYPE_ORDER, orderType
          );
        }
      }),
      map(response => {
        if (response.type === 'SUCCESS') {
          const orders = response && response.message || [];
          if (this.errorData.length === orders.length || orders.every(o => o.errorMsg)) {
            response.type = 'ERROR';
          }
        }
        return response;
      }),
      catchError(error => {
        if (this.isSB) {
          this.serbiaSendShoppingOrderEventData(error, null, ORDER_TYPE_ORDER, orderType);
        } else {
          this.shoppingBasketAnalyticService.sendShoppingOrderEventData(
            this.currentBasket, error, null, deliveryTypeCode, ORDER_TYPE_ORDER, orderType
          );
        }
        return throwError(error);
      })
    );
  }

  private getDeliveryCodeByLocation(order) {
    if (this.basketContext && this.basketContext.eshopBasketContextByLocation.length > 0) {
      const contextLocation = this.basketContext.eshopBasketContextByLocation.find(context => order.location && context.location.branchId === order.location.branchId);
      if (contextLocation) {
        return contextLocation.deliveryType.descCode;
      }
    }

    return null;
  }

  private serbiaSendShoppingOrderEventData(error, response, orderType, orderTrans) {
    this.basketContext.eshopBasketContextByLocation.filter(context => {
      this.shoppingBasketAnalyticService.sendShoppingOrderEventData(
        this.currentBasket, error, response, (context.deliveryType && context.deliveryType.descCode || null), orderType, orderTrans
      );
    });
  }

  private getErrorMsg(response) {
    const orders = response && response.length > 0 ? response : [];
    const errors = orders.some(order => order.errorMsg);
    const orderTimeout = orders.filter(order => order.errorMsg && order.errorMsg.key === TIMEOUT_EXCEEDED_ERROR);

    // no split order
    if (orders.length === 1) {
      // only timeout error
      if (orderTimeout && orderTimeout.length === errors.length) {
        if (!this.checkExistErrorMsg('ORDER.ERROR.TRANSFER_ONLY_TIMEOUT_FAILED')) {
          this.errorData.push({
            type: 'ERROR',
            message: 'ORDER.ERROR.TRANSFER_ONLY_TIMEOUT_FAILED'
          });
        }
      } else {
        // other error
        if (!this.checkExistErrorMsg('ORDER.ERROR.TRANSFER_FAILED')) {
          this.errorData.push({
            type: 'ERROR',
            message: 'ORDER.ERROR.TRANSFER_FAILED'
          });
        }
      }
    } else if (orders.length > 1) { //split order
      // only order timeout
      if (orderTimeout && orderTimeout.length === errors.length) {
        if (!this.checkExistErrorMsg('ORDER.ERROR.TRANSFER_ONLY_TIMEOUT_FAILED')) {
          this.errorData.push({
            type: 'ERROR',
            message: 'ORDER.ERROR.TRANSFER_ONLY_TIMEOUT_FAILED'
          });
        }

        return;
      }

      // order timeout and other error
      if (orderTimeout && orderTimeout.length < errors.length) {
        if (!this.checkExistErrorMsg('ORDER.ERROR.TRANSFER_TIMEOUT_AND_OTHER_FAILED')) {
          this.errorData.push({
            type: 'ERROR',
            message: 'ORDER.ERROR.TRANSFER_TIMEOUT_AND_OTHER_FAILED'
          });
        }

        return;
      }

      if (orderTimeout && orderTimeout.length === 0 && errors.length > 0) {
        if (!this.checkExistErrorMsg('ORDER.ERROR.TRANSFER_FAILED')) {
          this.errorData.push({
            type: 'ERROR',
            message: 'ORDER.ERROR.TRANSFER_FAILED'
          });
        }

        return;
      }
    } else {
      if (!this.checkExistErrorMsg(this.shoppingOrderService.transferBasketErrorHandler(response.message))) {
        this.errorData.push({
          type: 'ERROR',
          message: response.message
        });
      }
    }
  }

  private handleDmsExport(orderNr: string) {
    this.shoppingBasketService.clearBasket();
    this.errorData = [];
    const exportOrder: ExportOrder = this.buildExportRequestData(DmsExportCommand.ORDER, orderNr);
    this.dmsService.export(exportOrder).then(errorMessage => {
      this.errorData = this.getDmsErrorData(errorMessage);
    });
  }

  private checkExistErrorMsg(msg) {
    const error = (this.errorData || []).find(error => error.message === msg);
    if (error) {
      return true;
    }

    return false;
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  processDmsOffer(callback) {
    if (!this.currentBasket || !this.basketContext || !this.basketContext.billingAddress) {
      return;
    }

    this.errorData = [];
    const exportOrder: ExportOrder = this.buildExportRequestData(DmsExportCommand.OFFER, '');
    this.dmsService.export(exportOrder).then(errorMessage => {
      this.errorData = this.getDmsErrorData(errorMessage);
      if (callback) {
        callback();
      }
    });
  }

  private getDmsErrorData(errorMessage) {
    if (errorMessage) {
      return [{
        type: 'ERROR',
        message: errorMessage
      }];
    }
    return [];
  }

  private buildExportRequestData(dmsType: DmsExportCommand, orderNumber: string): ExportOrder {
    const exportOrder = this.dmsService.buildExportRequestGeneralInfo(this.dmsInfo, this.userDetail, this.basketContext, this.currentBasket,
      dmsType, this.customerMessage, orderNumber);
    this.currentBasket.items = ShoppingBasketUtil.groupBasketItemsByVehicleId(this.currentBasket.items);
    this.dmsService.buildExportOrders(exportOrder, this.currentBasket, this.labourTimes);
    return exportOrder;
  }

  get allowDmsOffer() {
    return this.dmsInfo && this.basketContext && this.basketContext.billingAddress;
  }
}
