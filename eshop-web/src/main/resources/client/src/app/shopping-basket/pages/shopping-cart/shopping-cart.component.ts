import { TIME_OUT_MESSAGE, Constant, APP_SHOPPING_CART_UPDATED_EVENT } from 'src/app/core/conts/app.constant';
import { Component, OnInit, OnDestroy, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import {
    HaynesProService,
    LabourTimeService,
    HaynesLinkHandleService,
    SagHaynesProReturnModalComponent,
    HaynesProLicenseSettings
} from 'sag-haynespro';

import { catchError, finalize, map, switchMap, takeUntil } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';
import { Observable, Subject } from 'rxjs';
import { BsModalService } from 'ngx-bootstrap/modal';
import { get, cloneDeep } from 'lodash';

import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { UserService } from 'src/app/core/services/user.service';
import { ShoppingBasketContextModel } from '../../models/shopping-basket-context.model';
import { DELIVERY_TYPE, ORDER_TYPE } from '../../../core/enums/shopping-basket.enum';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { CustomerSearchService } from 'src/app/home/service/customer-search.service';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { ArticleListSearchStorageService } from 'src/app/article-list/services/article-list-storage.service';
import { ShoppingBasketAnalyticService } from 'src/app/analytic-logging/services/shopping-basket-analytic.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { OciService } from 'src/app/oci/services/oci.service';

import { ShoppingBasketModel, ShoppingBasketInfo } from 'src/app/core/models/shopping-basket.model';
import { OfferDetail } from 'src/app/offers/models/offer-detail.model';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { ExportOrder } from 'src/app/dms/models/export-order.model';
import { OciInfo } from 'src/app/oci/models/oci-info.model';
import { SubOrderBasketModel } from 'src/app/core/models/sub-order-basket.model';

import { ShoppingExportModalComponent } from 'src/app/shared/connect-common/components/shopping-export-modal/shopping-export-modal.component';
import { ArticleListSearchModalComponent } from '../../components/article-list-search-modal/article-list-search-modal.component';

import { DmsExportCommand } from 'src/app/dms/enums/dms.enum';
import { SHOPPING_BASKET_ENUM } from 'src/app/core/enums/shopping-basket.enum';
import { CollectionUtil } from 'src/app/dms/utils/collection.util';
import { NumberUtil } from 'src/app/dms/utils/number.util';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
import { HAYNESPRO_LICENSE } from 'src/app/core/enums/haynes.enums';
import { permissions } from 'src/app/core/utils/permission';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { BroadcastService, SagMessageData, SagConfirmationBoxComponent, AffiliateUtil, AffiliateEnum } from 'sag-common';
import { ArticleBasketModel, ArticleBroadcastKey, CZ_AVAIL_STATE, ArticleModel } from 'sag-article-detail';
import { ARTICLE_LIST_TYPE, ARTICLE_TYPE, SEARCH_MODE } from 'sag-article-list';
import { SagCustomPricingService } from 'sag-custom-pricing';
import { BasketPriceSummaryModel } from '../../models/basket-price-summary-model';
import { ShoppingOrderService } from '../../services/shopping-order.service';
import { OrderAnalyticService } from 'src/app/analytic-logging/services/order-analytic.service';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { environment } from 'src/environments/environment';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { CommonModalService } from 'src/app/shared/connect-common/services/common-modal.service';
import { OrderingUtil } from 'src/app/core/utils/ordering.util';
import { ArticleReplaceModalComponent } from 'src/app/shared/connect-common/components/article-replace-modal/article-replace-modal.component';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { SubSink } from 'subsink';
import { SUB_LIST_NAME_TYPE } from 'src/app/analytic-logging/enums/event-type.enums';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';

@Component({
    selector: 'connect-shopping-cart',
    templateUrl: './shopping-cart.component.html',
    styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit, OnDestroy {
    SHOPPING_BASKET = ARTICLE_LIST_TYPE.SHOPPING_BASKET;
    SUB_BASKET = SHOPPING_BASKET_ENUM.FINAL;

    shoppingBaketData$: Observable<any>;
    couponForm: FormGroup;
    errorCoupon: SagMessageData = null;
    sucessCoupon: SagMessageData = null;
    showVatIcon: boolean;

    referenceExpanded = true;
    conditionExpanded = true;

    paymentSetting;

    selectedOffer: OfferDetail = null;
    // button permission
    isNotAllowToOrder: boolean;
    shoppingBasketInfo: ShoppingBasketInfo = null;
    creditInfo;

    // show spinner while removing coupon
    isRemovingCoupon = false;
    // key of localstorage of reference text
    refTextKeyStore: number | string;
    // all branches
    branches$: Observable<any[]>;
    // error of:
    saleUserError: SagMessageData;
    kslMessage: SagMessageData;
    basketMessage: SagMessageData;
    thuleMessage: SagMessageData;

    currentBasket: ShoppingBasketModel;
    private destroy$ = new Subject();
    private shouldGetLatestDataFromCartCacheSub$ = new Subject();

    subOrderBasket: SubOrderBasketModel = null;
    dmsInfo: DmsInfo;
    dmsErrorMessage: SagMessageData;
    errorMessage: string;
    private currentShoppingBasketContext: ShoppingBasketContextModel;
    labourTimes = [];

    private isSearchArticleList = false;

    isOciFlow: boolean;
    ociInfo: OciInfo;

    haynesProLicense: HaynesProLicenseSettings;
    basketPriceSummary: BasketPriceSummaryModel;

    currentStateVatConfirm: boolean;
    private spinner: any;

    private condition: any;

    czAvailState = CZ_AVAIL_STATE;
    czPriceType = CZ_PRICE_TYPE;
    isCz = AffiliateUtil.isCz(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isFC = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
    cz = AffiliateEnum.CZ;
    sb = AffiliateEnum.SB;
    ehcz = AffiliateEnum.EH_CZ;
    isSB: boolean;

    noQuantityAvailable: boolean;

    requestNoAvailInPrimaryLocationMessage = {
        message: 'SHOPPING_BASKET.REQUEST_QUANTITY_NOT_AVAIL_AT_PRIMARY_LOCATION',
        type: 'WARNING'
    };
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);

    isPDP: boolean;
    vatTypeDisplayConvert: any;
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    hasItemNotAllowToAddToShoppingCart = false;

    private subs = new SubSink();

    couponModuleEnabled = false;
    isHideCouponInput: boolean = true;
    isSubFinalShoppingCart: boolean = false;
    isReceiverSelected: boolean = false;

    @ViewChild('specialInfoRef', { static: false }) specialInfoTemplateRef: TemplateRef<any>;
    @ViewChild('memosRef', { static: false }) memosTemplateRef: TemplateRef<any>;
    @ViewChild('availRef', { static: false }) availTemplateRef: TemplateRef<any>;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChild('priceRef', { static: false }) priceTemplateRef: TemplateRef<any>;
    @ViewChild('totalPriceRef', { static: false }) totalPriceTemplateRef: TemplateRef<any>;

    constructor(
        public shoppingBasketService: ShoppingBasketService,
        public appContextService: AppContextService,
        public userService: UserService,
        public creditLimitService: CreditLimitService,
        public appStorage: AppStorageService,
        public customerService: CustomerSearchService,
        private fb: FormBuilder,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private modalService: BsModalService,
        private location: Location,
        private broadcaster: BroadcastService,
        private dmsService: ActiveDmsProcessor,
        private articleListSearchStorageService: ArticleListSearchStorageService,
        private articleBasketService: ArticleShoppingBasketService,
        private shoppingBasketAnalyticService: ShoppingBasketAnalyticService,
        private ociService: OciService,
        private haynesProService: HaynesProService,
        private labourTimeService: LabourTimeService,
        public haynesLinkHandleService: HaynesLinkHandleService,
        public shoppingOrderService: ShoppingOrderService,
        private shoppingCartService: ShoppingCartService,
        private customPricingService: SagCustomPricingService,
        private orderAnalyticService: OrderAnalyticService,
        private appModal: AppModalService,
        private commonModalService: CommonModalService,
        private gaService: GoogleAnalyticsService
    ) {
        this.isSB = AffiliateUtil.isSb(environment.affiliate);
    }

    onArticleNumberClick(data: ArticleModel) {
        this.prepareBastketSourceItem(data);
        this.commonModalService.showReplaceModal(data);
    }

    onShowAccessories(data: ArticleModel) {
        this.prepareBastketSourceItem(data);
        this.commonModalService.showAccessoriesModal(data);
    }

    onShowPartsList(data: ArticleModel) {
        this.prepareBastketSourceItem(data);
        this.commonModalService.showPartsListModal(data);
    }

    onShowCrossReference(data: ArticleModel) {
        this.prepareBastketSourceItem(data);
        this.commonModalService.showCrossReferenceModal(data);
    }

    startSpinner() {
        this.spinner = SpinnerService.start('.connect-shopping-cart', { class: 'fixed-top' });
    }

    stopSpinner() {
        SpinnerService.stop(this.spinner);
        this.spinner = null;
    }

    ngOnInit() {
        this.isPDP = AffiliateUtil.isAffiliateApplyPDP(environment.affiliate);
        this.vatTypeDisplayConvert = this.getVATSetting();

        this.isSubFinalShoppingCart = this.shoppingBasketService.basketType === this.SUB_BASKET;
        this.isReceiverSelected = !!this.appStorage.goodReceiver;

        this.couponModuleEnabled = get(this.userService, 'userDetail.settings.couponModuleEnabled', false);
        this.isHideCouponInput = this.isSubFinalShoppingCart || this.isReceiverSelected || this.isFC;

        this.subs.sink = this.appStorage.observeGoodReceiver().subscribe(res => {
            let isReceiverExist: boolean = !!res;
            this.isHideCouponInput = isReceiverExist;

            if (isReceiverExist) {
                this.removeCoupon();
            }
        });

        this.checkThuleMessage();
        this.dmsInfo = this.dmsService.getDmsInfo();
        this.dmsErrorMessage = this.dmsService.getDmsErrorMessage();
        // to binding reftext
        // id = sub basket id if any otherwise using customer number
        this.selectedOffer = this.appStorage.selectedOffer;
        this.subOrderBasket = this.appStorage.subOrderBasket;
        this.currentStateVatConfirm = this.appStorage.currentStateVatConfirm;

        this.refTextKeyStore = this.activatedRoute.snapshot.queryParams.id || this.userService.userDetail.custNr;
        if (!this.userService.employeeInfo || !this.userService.employeeInfo.id) {
            this.saleUserError = {
                message: ''
            } as SagMessageData;
        }

        this.appContextService.shoppingBasketContext$
            .pipe(takeUntil(this.destroy$))
            .subscribe((context: ShoppingBasketContextModel) => {
                this.currentShoppingBasketContext = context;

                if (!this.isSB) {
                    this.isNotAllowToOrder =
                        context &&
                        context.deliveryType &&
                        context.deliveryType.descCode === DELIVERY_TYPE.PICKUP &&
                        !context.pickupBranch;
                }
            });

        this.appStorage.fastScanArt$
            .pipe(takeUntil(this.destroy$))
            .subscribe(fastscanArt => {
                if (fastscanArt) {
                    this.startSpinner();
                    this.shoppingBasketService.loadCachedBasket().subscribe(basket => {
                        this.processFastscan(fastscanArt, basket);
                    });
                }
            });

        this.spinner = SpinnerService.startApp();
        this.shoppingBaketData$ = this.shoppingBasketService.loadCachedBasket().pipe(
            map(basket => {
                const data = basket.vehicleGroup();
                const dvse = basket.devseGroup();
                const fastscanArt = this.appStorage.fastscanArt;

                if (fastscanArt) {
                    // process fastscan
                    this.processFastscan(fastscanArt, basket);
                } else {
                    this.updateCurrentBasket(basket);
                    this.reloadCurrentShoppingBasket(false, null, true);
                }
                if (fastscanArt && data.length === 0) {
                    return null;
                }
                return { data, dvse };
            })
        );
        this.couponForm = this.fb.group({
            couponCode: ['', Validators.required]
        });

        this.userService.getPaymentSetting().subscribe(res => {
            this.paymentSetting = res;
        });

        this.branches$ = this.customerService.getCustomerBranches();

        this.broadcaster.on(ArticleBroadcastKey.SHOPPING_BASKET_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ArticleBasketModel) => {
                switch (data.action) {
                    case 'LOADED':
                        if (data.article.replacementForArtId || data.isAccessoryItem || data.isPartsItem) {
                            this.articleBasketService.isItemAddedToCart(data);
                        }
                        break;
                    case 'ADD':
                        if (data.uuid) {
                            SpinnerService.start(`#part-detail-${data.uuid}`,
                                { containerMinHeight: 0 }
                            );
                        }
                        if (!this.isSearchArticleList) {
                            if (data.cartKey) {
                                this.articleBasketService.updateBasketItem(data, (cartItem, response) => {
                                    this.shouldGetLatestDataFromCartCacheSub$.next();

                                    const gaCallback = (articles) => {
                                        const miniBasket = cloneDeep(this.shoppingBasketService.miniBasket);
                                        const articleAdded = (articles.basket_item_added || []).find(article => article.basket_item_article_id === data.pimId);
                                        const miniBasketAddedItem = (miniBasket.items || []).find(article => article.cartKey === data.cartKey);
                                        miniBasketAddedItem.articleItem.basketItemSourceId = articleAdded.basket_item_source_id;
                                        miniBasketAddedItem.articleItem.basketItemSourceDesc = articleAdded.basket_item_source_desc;
                                        this.shoppingBasketAnalyticService.sendAddArticleGaData(miniBasket, data);
                                    }
                                    this.shoppingBasketAnalyticService.sendShoppingBasketEvent(cartItem, response.items, undefined, gaCallback);
                                });

                            } else {
                                this.articleBasketService.addItemToCart(data, (basket) => {
                                    this.updateCurrentBasket(basket);
                                });
                            }
                        }
                        break;
                    case 'CUSTOM_PRICE_CHANGE':
                        this.currentBasket = new ShoppingBasketModel(data.basket);
                        this.shoppingBasketService.updateOtherProcess(data.basket);
                        this.updateCurrentBasket(this.currentBasket);
                        break;
                }
            });

        this.shoppingBasketService.currentSubBasket$.pipe(takeUntil(this.destroy$)).subscribe(data => {
            if (!data) {
                return;
            }
            if (!this.isSearchArticleList) {
                return;
            }
            this.updateCurrentBasket(data);
            this.stopSpinner();
        });

        this.userService.hasPermissions([permissions.haynespro.name]).subscribe(hasPerm => {
            this.checkHaynesproLicense(hasPerm);
        });

        this.userService.userPrice$.pipe(takeUntil(this.destroy$)).subscribe(userPrice => {
            this.showVatIcon = userPrice.vatConfirm;
            this.initialTotalSection(this.currentBasket);
        });

        this.ociInfo = this.ociService.getOciInfo();
        this.isOciFlow = this.ociInfo ? this.ociInfo.isOciFlow : false;

        this.broadcaster.on(APP_SHOPPING_CART_UPDATED_EVENT)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: ShoppingBasketModel) => {
                this.updateCurrentBasket(data);
                this.reloadCurrentShoppingBasket();
            });

        this.broadcaster.on(ArticleBroadcastKey.TOGGLE_SPECIAL_DETAIL)
            .pipe(takeUntil(this.destroy$))
            .subscribe((data: any) => {
                this.gaService.viewProductDetails(data.article, data.rootModalName);
            });

        this.broadcaster.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((code: string) => {
                this.router.navigate(['article', 'result'], {
                    queryParams: { type: SEARCH_MODE.ARTICLE_NUMBER, articleNr: code }
                });
            });

        this.broadcaster.on(ArticleBroadcastKey.VEHICLE_ID_CHANGE)
            .pipe(takeUntil(this.destroy$))
            .subscribe((vehicleId: string) => {
                if (vehicleId) {
                    this.router.navigate(['vehicle', vehicleId]);
                }
            });

        this.creditLimitService.creditCardInfo$
            .pipe(takeUntil(this.destroy$))
            .subscribe(creditInfo => {
                if (this.isSB) {
                    this.creditInfo = creditInfo || {};
                    this.isNotAllowToOrder = this.checkNotAllowToOrderSb(this.currentBasket);
                } else {
                    this.creditInfo = {};
                }
            });

        this.shouldGetLatestDataFromCartCacheSub$
            .pipe(
                takeUntil(this.destroy$),
                switchMap(() => {
                    if (this.isSubFinalShoppingCart) {
                        this.startSpinner();
                    }
                    return this.shoppingBasketService.loadCachedBasket(true).pipe(
                        finalize(() => {
                            if (this.isSubFinalShoppingCart) {
                                this.stopSpinner();
                            }
                        })
                    )
                })
            )
            .subscribe((shoppingBasketData: ShoppingBasketModel) => {
                this.updateCurrentBasket(shoppingBasketData);
            });
    }

    ngOnDestroy(): void {
        this.gaService.setBastketSourceItem(null);
        this.appStorage.fastscanArt = null;
        this.destroy$.next(true);
        this.destroy$.complete();
        if (this.userService.userDetail) {
            this.articleListSearchStorageService.setSubBasketHistory(this.userService.userDetail.id, []);
        }
        this.subs.unsubscribe();
    }

    getVATSetting() {
        if (this.userService && this.userService.userPrice) {
            return this.userService.userPrice.vatTypeDisplayConvert;
        }

        return null;
    }

    reloadCurrentShoppingBasket(isShownNonVehicleGroupOnly = false, callback?: () => void, shouldSendGaEvent?: boolean, isFastScan = false) {
        this.shoppingBasketService.loadShoppingBasket().subscribe(res => {
            if (res) {
                this.updateCurrentBasket(res, isShownNonVehicleGroupOnly);
                if (shouldSendGaEvent) {
                    const basket = res;
                    const articles = (basket && basket.items || []).map(item => {
                        return {
                            ...item.articleItem,
                            basketItemSourceDesc: item.basketItemSourceDesc,
                            basketItemSourceId: item.basketItemSourceId,
                        }
                    });
                    this.gaService.viewShoppingCart({ articles, summary: this.basketPriceSummary }, isFastScan);
                }
            }
            if (callback) {
                callback();
            }
            this.stopSpinner();
        });
    }

    processFastscan(art, basket: ShoppingBasketModel) {
        const addedItemRequest = {
            article: art,
            quantity: art.amountNumber,
            basketItemSourceId: this.appStorage.basketItemSource.basketItemSourceId,
            basketItemSourceDesc: this.appStorage.basketItemSource.basketItemSourceDesc
        };
        if (basket.items.length > 0) {
            const existedArt = basket.items.find(cartItem =>
                !cartItem.vehicleId && cartItem.articleItem && cartItem.articleItem.pimId === art.id_pim);
            if (!!existedArt) {
                addedItemRequest.quantity = art.qtyMultiple + existedArt.quantity;
            }
        }
        addedItemRequest.article.amountNumber = addedItemRequest.quantity;
        this.kslMessage = null;
        this.shoppingBasketService.addItemToCart(addedItemRequest).subscribe((res: ShoppingBasketModel) => {
            this.changeToCounterBasketMode().then((isKSLForShopCust) => {
                this.reloadCurrentShoppingBasket(true, () => {
                    if (isKSLForShopCust && this.currentBasket.items.length > 0 && !this.shoppingBasketInfo.isAllSofort) {
                        this.kslMessage = {
                            message: 'SHOPPING_BASKET.COUNTER_MESSAGE',
                            type: 'WARNING'
                        };
                    }
                    this.creditLimitService.updateCreditCardInfo();
                }, true, true);
            });
            this.appStorage.fastscanArt = null;
            const items = [];
            (res && res.items || []).forEach(item => {
                if (item.articleItem.artid === art.artid && !item.vehicleId) {
                    items.push(item);
                    item.quantity = art.qtyMultiple;
                }
            });
            res.items = items;
            this.shoppingBasketAnalyticService.sendFastscanEventData(art.id_pim, res.items);
        });
    }

    updateShoppingBasketContext({ body, reload, done }) {
        if (reload) {
            this.startSpinner();
        } else {
            if (!this.spinner) {
                this.spinner = SpinnerService.start('connect-shopping-cart .shopping-basket-condition');
            }
        }
        this.appContextService.updateShoppingBasketContext(body)
            .pipe(takeUntil(this.destroy$))
            .subscribe(res => {
                if (reload) {
                    // need to reload if the delivery method has changed
                    this.reloadCurrentShoppingBasket();
                    this.checkValidBranch().pipe(takeUntil(this.destroy$)).subscribe();
                } else {
                    this.creditLimitService.updateCreditCardInfo();
                    this.stopSpinner();
                }
                if (!!done) {
                    done();
                }
            });
    }

    toggleNetPriceSetting() {
        this.userService.toggleNetPriceView();
    }

    toggleVAT() {
        this.currentStateVatConfirm = !this.currentStateVatConfirm;
        this.appStorage.currentStateVatConfirm = this.currentStateVatConfirm;
        this.initialTotalSection(this.currentBasket);
    }

    checkHaynesproLicense(hasPermission) {
        if (!hasPermission) {
            return;
        }
        if (this.dmsInfo) {
            this.haynesProService.getHaynesProLicense().subscribe((res: HaynesProLicenseSettings) => {
                this.haynesProLicense = res;
            });
        }
    }

    private updateCurrentBasket(basket: ShoppingBasketModel, isShownNonVehicleGroupOnly = false) {
        let options;
        if (this.isCz) {
            options = {
                defaultArrivalTime: 0
            };
        }
        this.currentBasket = basket;
        this.getHaynesLinkAndLabourTime(basket);
        this.shoppingBasketInfo = basket.getSummary(options);
        this.initialTotalSection(basket);
        this.shoppingBaketData$ = of({
            data: basket.vehicleGroup(isShownNonVehicleGroupOnly),
            dvse: basket.devseGroup()
        });

        if (this.isSB) {
            this.noQuantityAvailable = this.checkNoQuantityAvailableInPrimaryLocation(basket);
            this.isNotAllowToOrder = this.checkNotAllowToOrderSb(basket);
        }
        this.hasItemNotAllowToAddToShoppingCart = this.isAffiliateApplyFgasAndDeposit ? OrderingUtil.preventOrderingWhenHasItemNotAllowToAddToShoppingCart(basket) : false;
    }

    getHaynesLinkAndLabourTime(basket: ShoppingBasketModel) {
        // create list labourTime if dms and ultimate license
        const licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        if (this.dmsInfo && licenseType === HAYNESPRO_LICENSE.ULTIMATE.toString()) {
            const groupVehiclesCategories = basket.vehicleGroup(false);
            this.haynesLinkHandleService.getLinkHaynesPro(this.userService.userDetail, groupVehiclesCategories);
            this.labourTimes = this.labourTimeService.loadLabourTimesForAllVehicle(groupVehiclesCategories);
        }
    }

    emitHandleLoginHaynesPro($event) {
        const { vehicleId, index } = $event;
        this.haynesLinkHandleService.loginHaynesPro(this.userService.userDetail, vehicleId, index);
    }

    openHaynesProModal($event) {
        const { vehicleId, index } = $event;
        this.modalService.show(SagHaynesProReturnModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                index,
                vehicleId,
                callback: () => {
                    this.retrieveDataFromHaynesPro(index);
                }
            }
        });
    }

    retrieveDataFromHaynesPro(index) {
        const selectedVehicleId = this.haynesLinkHandleService.linkLoginHaynesPro[index].vehicleId;
        if (!selectedVehicleId) {
            return;
        }
        this.labourTimeService.loadLabourTimeForVehicle(selectedVehicleId).then(labourTimesByVehicle => {
            const tmpLabourTimes = this.labourTimes.filter(l => l.vehicleId !== selectedVehicleId);
            if (labourTimesByVehicle && labourTimesByVehicle.vehicleId) {
                tmpLabourTimes.push(labourTimesByVehicle);
            }
            this.labourTimes = tmpLabourTimes;
        });
    }

    get hasHaynesProFeatures() {
        const licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        return this.dmsInfo && licenseType  === 'ULTIMATE';
    }

    addCoupon(done: () => void) {
        if (this.couponForm.invalid) {
            return;
        }
        const code = this.couponForm.value.couponCode;
        this.errorCoupon = null;
        this.couponForm.disable();
        this.shoppingBasketService.addCoupon(code).pipe(
            map(basket => {
                this.updateCurrentBasket(basket);
                this.shoppingBasketAnalyticService.sendAddCouponEventData(basket);
                return true;
            }),
            catchError(errorRes => {
                this.handleCouponError(errorRes.error || {});
                return of(null);
            })
        ).subscribe(() => {
            this.couponForm.enable();
            done();
        });
    }

    removeCoupon() {
        this.errorCoupon = null;
        this.isRemovingCoupon = true;
        this.couponForm.disable();
        this.shoppingBasketService.removeCoupon().pipe(
            map(basket => {
                this.updateCurrentBasket(basket);
                return true;
            }),
            catchError(errorRes => {
                this.handleCouponError(errorRes.error || {});
                return of(null);
            })
        ).subscribe(() => {
            this.isRemovingCoupon = false;
            this.couponForm.enable();
        });
    }

    addOffer(callback, selectedOffer?: OfferDetail) {
        this.shoppingCartService.addOffer({
            currentBasket: this.currentBasket,
            refTextKeyStore: this.refTextKeyStore,
            selectedOffer,
            callback: (result) => {
                if (result.status === 'success') {
                    this.selectedOffer = result.updatedOffer;
                }
                callback();
            }
        });
    }

    transferBasket(callback) {
        const spinner = SpinnerService.startApp();
        this.checkValidBranch().pipe(takeUntil(this.destroy$)).subscribe(valid => {
            if (!valid) {
                callback();
                SpinnerService.stop(spinner);
                return;
            }

            this.shoppingCartService.transferBasket({
                currentBasket: this.currentBasket,
                refTextKeyStore: this.refTextKeyStore,
                total: this.basketPriceSummary.total,
                callback: (result: SagMessageData, orderRequest: any) => {
                    if (result.type === 'ERROR') {
                        this.basketMessage = result;
                        const isForeDisabledIfNotTimeOutMessage = TIME_OUT_MESSAGE.indexOf(result.message) === -1;
                        callback(isForeDisabledIfNotTimeOutMessage);
                    } else {
                        this.articleListSearchStorageService.clearAllHistory(get(this.userService, 'userDetail.id'));
                    }
                    SpinnerService.stop(spinner);
                    this.orderAnalyticService.sendTransferBasketEventData({
                        shoppingBasket: this.currentBasket,
                        shoppingBasketContext: this.appContextService.shoppingBasketContext,
                        orderRequest,
                        orderType: 'BASKET',
                        orderTransactionType: get(result, 'message.orderType'),
                        response: result.message,
                        responseError: result.extras
                    });
                }
            });
        });
    }

    transferOffer(callback) {
        const spinner = SpinnerService.startApp();
        this.checkValidBranch().pipe(takeUntil(this.destroy$)).subscribe(valid => {
            if (!valid) {
                callback();
                SpinnerService.stop(spinner);
                return;
            }

            this.shoppingCartService.transferOffer({
                refTextKeyStore: this.refTextKeyStore,
                total: this.basketPriceSummary.total,
                callback: (result: SagMessageData, orderRequest: any) => {
                    if (result.type === 'ERROR') {
                        this.basketMessage = result;
                        const isForeDisabledIfNotTimeOutMessage = TIME_OUT_MESSAGE.indexOf(result.message) === -1;
                        callback(isForeDisabledIfNotTimeOutMessage);
                    } else {
                        this.articleListSearchStorageService.clearAllHistory(get(this.userService, 'userDetail.id'));
                    }
                    SpinnerService.stop(spinner);
                    this.orderAnalyticService.sendTransferBasketEventData({
                        shoppingBasket: this.currentBasket,
                        shoppingBasketContext: this.appContextService.shoppingBasketContext,
                        orderRequest,
                        orderType: '',
                        orderTransactionType: get(result, 'message.orderType'),
                        response: result.message,
                        responseError: result.extras
                    });
                }
            });
        });
    }

    viewConfirmOrder(callback?: any) {
        this.checkValidBranch(callback).pipe(takeUntil(this.destroy$)).subscribe(valid => {
            if (!valid) {
                return;
            }
            if (this.labourTimes && this.labourTimes.length > 0) {
                this.dmsService.setLaboursTime(this.labourTimes);
            }

            const shoppingBasketContext = this.appContextService.shoppingBasketContext;
            this.shoppingBasketAnalyticService.sendConfirmOrderEventData(this.currentBasket);
            this.shoppingBasketAnalyticService.sendAddShoppingInfoEventData(this.currentBasket, shoppingBasketContext);
            this.shoppingBasketAnalyticService.sendAddPaymentInfoEventData(this.currentBasket, shoppingBasketContext);

            this.router.navigate(['../', 'order'], {
                queryParamsHandling: 'merge',
                relativeTo: this.activatedRoute
            });
        });
    }

    addMoreArticle() {
        if (!this.subOrderBasket || !this.subOrderBasket.finalOrder) {
            return;
        }
        this.isSearchArticleList = true;
        this.modalService.show(ArticleListSearchModalComponent, {
            ignoreBackdropClick: true,
            class: 'article-list-search-modal',
            backdrop: 'static',
            initialState: {
                close: () => {
                    this.isSearchArticleList = false;
                }
            }
        });
    }

    export(callback) {
        this.appModal.modals = this.modalService.show(ShoppingExportModalComponent, {
            ignoreBackdropClick: true,
            class: 'shopping-export-modal',
            initialState: {
                shoppingBasket: this.currentBasket
            }
        });
        callback();
    }

    removeArticle(cartKeys: string[]) {
        if (this.shoppingBasketService.basketType === this.SUB_BASKET && cartKeys.length === this.currentBasket.items.length) {
            const deleteModalRef = this.modalService.show(SagConfirmationBoxComponent, {
                ignoreBackdropClick: true,
                initialState: {
                    title: 'COMMON_LABEL.CONFIRMATION',
                    message: 'ORDER_DASHBOARD.CONFIRMATION.CONFIRM_DELETE_ORDER',
                    okButton: 'COMMON_LABEL.CONFIRM_BTN',
                    cancelButton: 'COMMON_LABEL.CLOSE',
                    confirm: () => {
                        deleteModalRef.hide();
                        this.doRemoveArticle(cartKeys, true);
                    }
                }
            });
        } else {
            this.doRemoveArticle(cartKeys, cartKeys.length === this.currentBasket.items.length);
        }
    }

    conditionChanged(condition) {
        this.condition = condition;
    }

    private doRemoveArticle(cartKeys: string[], isAll = false) {
        this.startSpinner();
        let reloadAvail = true;
        this.shoppingBasketService.removeBasketItem({ cartKeys, reloadAvail }).subscribe((basket: ShoppingBasketModel) => {
            this.shouldGetLatestDataFromCartCacheSub$.next();
            if (this.isSB && isAll) {
                this.shoppingOrderService.appContextService.initAppContext().subscribe(res => { });
            }
            SpinnerService.stop(this.spinner);
        });
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

    backToPreviousPage(callback) {
        this.location.back();
        callback();
    }

    exportOci() {
        if (!this.isOciFlow) {
            return;
        }

        this.ociService.getOciExportData(this.ociInfo.action).subscribe(
            response => {
                this.shoppingBasketAnalyticService.sendExportOciEventData(this.currentBasket);
                const htmlSrc = response.toString();
                this.userService.logout();
                setTimeout(() => {
                    document.querySelector('html').innerHTML = htmlSrc;
                }, 100);
            },
            error => {
                this.errorMessage = 'MESSAGES.GENERAL_ERROR';
            }
        );
    }

    get allowOrderReview() {
        if (this.isOciFlow) {
            return false;
        }
        if (this.userService.userDetail.isSalesOnBeHalf) {
            if (this.isCz || this.isSB) {
                return true;
            }
            return !!this.shoppingBasketInfo &&
                this.shoppingBasketInfo.isAllVailAvail &&
                !!this.userService.employeeInfo &&
                !!this.userService.employeeInfo.id &&
                !!this.creditLimitService.creditCardInfo &&
                !!this.creditLimitService.creditCardInfo.creditValid;
        }
        return true;
    }

    private handleCouponError(error) {
        const errorCode = error.code || error.error_code || error.error;
        if (errorCode === 404) {
            this.errorCoupon = {
                type: 'ERROR',
                message: 'COUPON.ERROR.NOT_FOUND',
                icon: 'fa-close'
            };
        } else {
            this.errorCoupon = {
                type: 'ERROR',
                message: error.message,
                params: { value: error.additonalinfo },
                icon: 'fa-close'
            };
        }
    }

    private changeToCounterBasketMode() {
        return new Promise(resolve => {
            const basketContext = this.appContextService.shoppingBasketContext;
            const isShopCustomer = this.userService.userDetail.isShopCustomer;
            const isKSLForShopCust = isShopCustomer && basketContext.showKSLMode;
            if (isKSLForShopCust) {
                if (basketContext.orderType !== ORDER_TYPE.COUNTER) {
                    const updated = new ShoppingBasketContextModel(basketContext);
                    updated.orderType = ORDER_TYPE[ORDER_TYPE.COUNTER];
                    this.updateShoppingBasketContext({
                        body: updated, reload: false, done: () => {
                            resolve(isKSLForShopCust);
                        }
                    });
                } else {
                    resolve(isKSLForShopCust);
                }
            } else {
                resolve(isKSLForShopCust);
            }
        });
    }

    get allowDmsOffer(): boolean {
        return this.dmsInfo && this.currentBasket && !CollectionUtil.isEmpty(this.currentBasket.items);
    }

    async processDmsOffer(callback) {
        try {
            if (NumberUtil.isNumber(this.dmsInfo.offerId)) {
                this.selectedOffer = await this.shoppingCartService.addToExistingOfferForDMS(this.dmsInfo.offerId, this.dmsInfo, this.currentBasket);
                this.exportOffer(callback);
                return;
            }
            if (DmsUtil.isDmsOfferPresent(this.dmsInfo)) {
                this.selectedOffer = await this.shoppingCartService.addToNewOfferForDMS(this.dmsInfo, this.currentBasket);
                this.exportOffer(callback);
                return;
            }
        } catch (error) {
            if (error.code === Constant.FORBIDDEN_CODE) {
                this.dmsService.showDmsOfferPermissionErr();
            }
        }
        this.exportOffer(callback);
    }

    async exportOffer(callback) {
        const exportOrder: ExportOrder = this.shoppingCartService.buildDmsExportRequestData(
            this.dmsInfo,
            this.selectedOffer,
            DmsExportCommand.OFFER,
            this.currentShoppingBasketContext,
            this.currentBasket,
            this.labourTimes
        );
        this.dmsService.export(exportOrder).then((err: string) => {
            this.errorMessage = err;
            if (callback) {
                callback();
            }
        });
    }

    async onCustomPriceChange({ price, vehicleId }, vehicleGroups: any[]) {
        const group = vehicleGroups.find(g => g.vehicleId === vehicleId);
        if (!group) {
            return;
        }
        const spinner = SpinnerService.start(`connect-shopping-cart #${group.vehicleId}`, {
            containerMinHeight: 100
        });
        const cartItems = group.value.reduce((acc, cur) => [...acc, ...cur], []);
        const vehicle = group.vehicle || {};
        const articles = cartItems.map(item => item.articleItem);

        const allSelectedPrices = await this.customPricingService.updateCustomPriceByBrand(price, articles, {
            brand: vehicle.vehicle_brand,
            brandId: vehicle.id_make
        });
        // update displayed price for event data
        const customPriceUpdatingBody = [];
        cartItems.forEach(item => {
            const customPrice = allSelectedPrices.find(p => p.articleId === item.articleItem.pimId);
            item.articleItem.displayedPrice = customPrice && customPrice.displayedPrice;
            customPriceUpdatingBody.push({
                cartKey: item.cartKey,
                displayedPrice: item.articleItem.displayedPrice
            });
        });
        try {
            const res = await this.customPricingService.updateCustomPriceInBasket(customPriceUpdatingBody).toPromise();
            this.updateCurrentBasket(new ShoppingBasketModel(res));
            const updatedCartItems = res.items.filter(item => {
                return group.vehicle.vehicleInfo === item.vehicleInfo &&
                    item.itemType !== ARTICLE_TYPE[ARTICLE_TYPE.DVSE_NON_REF_ARTICLE];
            });
            this.shoppingBasketAnalyticService.sendCustomPriceShoppingBasketEvent(updatedCartItems);
        } catch (ex) {
            console.error('Update custom price in basket fail: %s', ex);
        }
        SpinnerService.stop(spinner);
    }

    private prepareBastketSourceItem(data: ArticleModel) {
        const articleId = data.article.artid;
        const basketItem = (this.currentBasket.items || []).find(item => item.articleItem.artid === articleId && !item.vehicleId);
        if (basketItem) {
            this.gaService.setBastketSourceItem({ basketItemSourceId: basketItem.basketItemSourceId, basketItemSourceDesc: basketItem.basketItemSourceDesc });
        }
    }

    private checkThuleMessage() {
        const notFoundPartNumbers = this.appStorage.thuleMessage;
        if (!!notFoundPartNumbers && notFoundPartNumbers.length > 0) {
            this.thuleMessage = {
                message: 'SHOPPING_BASKET.THULE_NOT_FOUND_PARTS',
                type: 'WARNING',
                params: {
                    ids: (notFoundPartNumbers || []).join(', ')
                }
            };
        } else {
            this.thuleMessage = null;
        }
        this.appStorage.clearThuleMessage();
    }

    private checkValidBranch(callback?: any) {
        if (this.shoppingCartService.byPassHiddenBranchCheck()) {
            this.errorMessage = '';
            return of(true);
        }

        const branchId = get(this.condition, 'branchId', '');
        let obs;
        if (!branchId) {
            obs = of([]);
        } else {
            obs = this.customerService.getCustomerBranches();
        }

        return obs
            .pipe(
                catchError(() => of([])),
                switchMap((res: any[]) => {

                    const isHiddenBrand = !(res || []).some(item => item.branchId === branchId);

                    if (isHiddenBrand) {
                        this.errorMessage = 'SHOPPING_BASKET.BRANCH_REQUIRED';
                        window.scrollTo(0, 0);
                        return of(false);
                    }

                    this.errorMessage = '';
                    return of(true);
                }),
                finalize(() => {
                    if (callback) {
                        callback();
                    }
                })
            );
    }

    private checkNoQuantityAvailableInPrimaryLocation(basket: ShoppingBasketModel) {
        return (basket && basket.items || []).some(item => {
            if (item.articleItem && item.articleItem.availabilities && !item.vin) {
                return item.articleItem.availabilities.some(avail => avail.location && !avail.location.allInPrioLocations);
            }
        });
    }

    private checkNotAllowToOrderSb(basket: ShoppingBasketModel) {
        return this.checkNoQuantityAvailableInPrimaryLocation(basket) || (this.creditInfo && this.creditInfo.creditValid === false);
    }
}
