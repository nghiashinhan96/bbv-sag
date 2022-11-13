import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { PopoverDirective } from 'ngx-bootstrap/popover';
import { catchError, finalize, switchMap, takeUntil } from 'rxjs/operators';
import { of, Subject } from 'rxjs';
import { difference, get } from 'lodash';

import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { UserService } from 'src/app/core/services/user.service';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { TIME_OUT_MESSAGE, Constant } from 'src/app/core/conts/app.constant';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { AffiliateUtil, SagMessageData } from 'sag-common';
import { ShoppingCartService } from 'src/app/shopping-basket/services/shopping-cart.service';
import { OrderAnalyticService } from 'src/app/analytic-logging/services/order-analytic.service';
import { environment } from 'src/environments/environment';
import { OciService } from 'src/app/oci/services/oci.service';
import { OciInfo } from 'src/app/oci/models/oci-info.model';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { NumberUtil } from 'src/app/dms/utils/number.util';
import { DmsExportCommand } from 'src/app/dms/enums/dms.enum';
import { ExportOrder } from 'src/app/dms/models/export-order.model';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
import { permissions } from 'src/app/core/utils/permission';
import { HaynesProService, LabourTimeService, HaynesProLicenseSettings } from 'sag-haynespro';
import { HAYNESPRO_LICENSE } from 'src/app/core/enums/haynes.enums';
import { CustomerSearchService } from 'src/app/home/service/customer-search.service';
import { ArticleListSearchStorageService } from 'src/app/article-list/services/article-list-storage.service';
import { OrderingUtil } from 'src/app/core/utils/ordering.util';
import { ShoppingBasketAnalyticService } from 'src/app/analytic-logging/services/shopping-basket-analytic.service';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { TranslateService } from '@ngx-translate/core';
import { ADDRESS_TYPE, DELIVERY_TYPE, ORDER_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { CustomerBranchModel } from 'src/app/core/models/customer-branch.model';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { BroadcastService } from 'sag-common';
import { ArticleBroadcastKey } from 'sag-article-detail';
import { UserAddress } from 'src/app/core/models/user-address.model';
@Component({
    selector: 'connect-header-shopping-basket',
    templateUrl: './header-shopping-basket.component.html',
    styleUrls: ['./header-shopping-basket.component.scss']
})
export class HeaderShoppingBasketComponent implements OnInit, OnDestroy {
    isSB = AffiliateUtil.isSb(environment.affiliate);
    cartQuantity = null;
    totalPrice = 0;
    creditCheckMessage;
    isSalesOnBeHalf = false;
    salesUser = false;
    creditInfo: any;
    isAllItemsAvailable: boolean;
    miniBasket: ShoppingBasketModel;
    isInvalidPersonalNumber: boolean;
    isDemoCustomer: boolean;
    isFinalUserRole: boolean;
    isOciFlow: boolean;
    ociInfo: OciInfo;
    isShown = false;
    refKey: string;
    miniBasketLoading = false;
    isEnabledLastItemAnimation = false;
    refTextKeyStore: number | string;
    isCz = AffiliateUtil.isCz(environment.affiliate);
    dmsInfo: DmsInfo;
    labourTimes = [];
    haynesProLicense: HaynesProLicenseSettings;
    errorMessage: string;

    deliveryType: string;
    defaultDeliveryType: string;
    scrollToDelivery: boolean;

    DELIVERY_TYPE = DELIVERY_TYPE;
    branchesForPickup = [];
    deliveryAddress: any[] = [];
    deliveryTypes: any[] = [];
    hasItemNotAllowToAddToShoppingCart = false;
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    defaultBranch: CustomerBranchModel;
    maxRetry = 3;
    countRetry = 0;

    private destroy$ = new Subject();
    @ViewChild('pop', { static: true }) miniBasketRef: PopoverDirective;
    @ViewChild('miniBasketContainer', { static: false }) miniBasketContainer: ElementRef;

    constructor (
        public shoppingBasketService: ShoppingBasketService,
        public creditLimitService: CreditLimitService,
        private userService: UserService,
        private router: Router,
        private appStorage: AppStorageService,
        private appContextService: AppContextService,
        private shoppingCartService: ShoppingCartService,
        private orderAnalyticService: OrderAnalyticService,
        private ociService: OciService,
        private dmsService: ActiveDmsProcessor,
        private haynesProService: HaynesProService,
        private labourTimeService: LabourTimeService,
        private customerService: CustomerSearchService,
        private articleListSearchStorageService: ArticleListSearchStorageService,
        private shoppingBasketAnalyticService: ShoppingBasketAnalyticService,
        private translateService: TranslateService,
        private broadcaster: BroadcastService
    ) {
    }

    ngOnInit() {
        this.deliveryType = this.userService.userDetail && this.userService.userDetail.customer && this.userService.userDetail.customer.sendMethodCode;
        this.dmsInfo = this.dmsService.getDmsInfo();
        this.shoppingBasketService.toogleMiniBasket$
            .pipe(takeUntil(this.destroy$))
            .subscribe(res => {
                if (this.miniBasketRef) {
                    this.miniBasketRef.hide();
                }
            });
        this.shoppingBasketService.miniBasket$
            .pipe(takeUntil(this.destroy$))
            .subscribe(shoppingBasket => {
                this.isEnabledLastItemAnimation = this.hasNewItem(this.miniBasket, shoppingBasket);
                this.miniBasket = shoppingBasket;
                if (this.miniBasket && (this.miniBasket.items || []).length > 0) {
                    this.miniBasket.items.sort((item1, item2) => {
                        return item1.addedTime > item2.addedTime ? 1 : -1;
                    });
                }
                if (this.miniBasket) {
                    this.miniBasketLoading = false;
                    this.isAllItemsAvailable = this.shoppingBasketService.isAllBasketItemsAvailable;
                    this.totalPrice = this.miniBasket.initTotalInclVat(this.userService.userPrice);
                    this.hasItemNotAllowToAddToShoppingCart = this.isAffiliateApplyFgasAndDeposit ? OrderingUtil.preventOrderingWhenHasItemNotAllowToAddToShoppingCart(shoppingBasket) : false;
                }

                setTimeout(() => {
                    this.scrollToConditionRef();
                }, 200);
            });
        this.shoppingBasketService.basketQuantity$
            .pipe(takeUntil(this.destroy$))
            .subscribe(cartQuantity => {
                this.cartQuantity = cartQuantity;
            });

        this.userService.userDetail$
            .pipe(takeUntil(this.destroy$))
            .subscribe(userDetail => {
                if (userDetail) {
                    this.refTextKeyStore = userDetail.custNr;
                    this.isDemoCustomer = userDetail.settings.demoCustomer;
                    this.isSalesOnBeHalf = userDetail && userDetail.isSalesOnBeHalf;
                    this.salesUser = userDetail && userDetail.salesUser;
                    this.isFinalUserRole = userDetail && userDetail.isFinalUserRole;

                    const refKey = userDetail && userDetail.custNr;
                    if (!userDetail.salesUser && this.refKey && this.refKey !== refKey) {
                        // reload basket;
                        this.loadMiniBasket();
                    }
                    this.refKey = refKey;
                    this.defaultDeliveryType = userDetail.customer && userDetail.customer.sendMethodCode;
                    this.defaultBranch = new CustomerBranchModel(userDetail.customer && userDetail.customer.branch);
                }
            });

        this.creditLimitService.creditCardInfo$
            .pipe(takeUntil(this.destroy$))
            .subscribe(creditInfo => {
                if (this.isSalesOnBeHalf || this.isSB) {
                    this.creditInfo = creditInfo || {};
                } else {
                    this.creditInfo = {};
                }
            });

        this.userService.userPrice$
            .pipe(takeUntil(this.destroy$))
            .subscribe(userPrice => {
                if (!!userPrice && this.miniBasket) {
                    this.totalPrice = this.getTotalPrice(userPrice);
                }
            });

        this.userService.employeeInfo$
            .pipe(takeUntil(this.destroy$))
            .subscribe(info => {
                this.isInvalidPersonalNumber = !info || !info.id;
            });

        this.ociInfo = this.ociService.getOciInfo();
        this.isOciFlow = this.ociInfo ? this.ociInfo.isOciFlow : false;

        this.userService.hasPermissions([permissions.haynespro.name])
            .pipe(takeUntil(this.destroy$))
            .subscribe(hasPerm => {
                this.checkHaynesproLicense(hasPerm);
            });

        this.appContextService.shoppingBasketContext$
            .pipe(takeUntil(this.destroy$))
            .subscribe((context) => {
                this.checkValidBranch().subscribe();
                if (!this.deliveryTypeDisplay) {
                    const salesUser = get(this.userService, 'userDetail.salesUser');
                    const isSalesOnBeHalf = get(this.userService, 'userDetail.isSalesOnBeHalf');
                    if (salesUser && !isSalesOnBeHalf) {
                        return;
                    }
                    setTimeout(() => {
                        this.countRetry++;
                        if (this.countRetry < this.maxRetry) {
                            this.appContextService.getAppContext().subscribe();
                        } else if (this.countRetry === this.maxRetry) {
                            this.userService.initUser().subscribe();
                        }
                    }, 1000);
                }
            });

        if(!this.isSB) {
            this.userService.userPaymentSetting$
                .pipe(takeUntil(this.destroy$))
                .subscribe(paymentSetting => {
                    if(paymentSetting) {
                        this.filterAddress(paymentSetting);
                        this.deliveryTypes = (paymentSetting.deliveryTypes || []).filter(method => method.descCode !== DELIVERY_TYPE.COUNTER);
                    }
                });
        }
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    searchBrand(term: string, item: any) {
        return (item.branchId || '').toLowerCase().indexOf(term.toLowerCase()) !== -1 ||
            (item.branchName || '').toLowerCase().indexOf(term.toLowerCase()) !== -1;
    }

    handleQuickView() {

    }

    scrollToConditionRef() {
        if(this.miniBasket && this.isShown) {
            const miniBasketCondition = document.getElementById('miniBasketCondition');
            if(this.scrollToDelivery && miniBasketCondition) {
                miniBasketCondition.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        }
    }

    onShownPopover() {
        setTimeout(() => {
            this.isShown = true;
        }, 0);

        setTimeout(() => {
            this.scrollToConditionRef();
        }, 200);
    }

    onHiddenPopover() {
        this.isShown = false;
        this.scrollToDelivery = false;
    }

    showMiniBasket(miniBasketRef, isScroll = false) {
        if (miniBasketRef && miniBasketRef.isOpen) {
            miniBasketRef.hide();
            this.scrollToDelivery = false;
            return;
        }

        if (this.shoppingBasketService.isEnabled && miniBasketRef) {
            if (!this.miniBasket) {
                this.loadMiniBasket();
            }

            this.scrollToDelivery = isScroll;
            this.customerService.getCustomerBranches()
                .pipe(takeUntil(this.destroy$))
                .subscribe(value => {
                    if(!value) {
                        return;
                    }

                    this.branchesForPickup = value.sort((b1: CustomerBranchModel, b2: CustomerBranchModel) => b1.branchName < b2.branchName ? -1 : 1)
                });

            miniBasketRef.show();
        }
    }

    gotoOrder(callback) {
        this.checkValidBranch(callback).subscribe(res => {
            if (!res) {
                return;
            }
            
            const shoppingBasketContext = this.appContextService.shoppingBasketContext;
            this.shoppingBasketAnalyticService.sendAddShoppingInfoEventData(this.miniBasket, shoppingBasketContext);
            this.shoppingBasketAnalyticService.sendAddPaymentInfoEventData(this.miniBasket, shoppingBasketContext);

            this.router.navigate(['/','shopping-basket','order']);
        });
    }

    transferBasket(callback) {
        this.checkValidBranch(callback).subscribe(res => {
            if (!res) {
                return;
            }
            const spinner: any = SpinnerService.start('.mini-basket-popover .popover-body');
            const refKey = this.userService.userDetail && this.userService.userDetail.custNr;
            this.shoppingCartService.transferBasket({
                currentBasket: this.miniBasket,
                refTextKeyStore: refKey,
                total: this.totalPrice,
                callback: (result: SagMessageData, orderRequest: any) => {
                    SpinnerService.stop(spinner);
                    if (result.type === 'SUCCESS') {
                        if (this.miniBasketRef) {
                            this.miniBasketRef.hide();
                        }
                        this.articleListSearchStorageService.clearAllHistory(get(this.userService, 'userDetail.id'));
                    } else {
                        const isForeDisabledIfNotTimeOutMessage = TIME_OUT_MESSAGE.indexOf(result.message) === -1;
                        callback(isForeDisabledIfNotTimeOutMessage);
                    }
                    this.orderAnalyticService.sendTransferBasketEventData({
                        shoppingBasket: this.miniBasket,
                        shoppingBasketContext: this.shoppingBasketContext,
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

    get allowOrderReview() {
        if (this.isOciFlow) {
            return false;
        }
        if (this.userService.userDetail.isSalesOnBeHalf) {
            if (this.isCz) {
                return true;
            }
            return (this.miniBasket && this.miniBasket.isAllVailAvail()) &&
                (this.userService.employeeInfo && this.userService.employeeInfo.id) &&
                this.creditLimitService.creditCardInfo.creditValid;
        }
        return true;
    }

    handleCreateOffer(callback) {
        this.checkValidBranch(callback).subscribe(res => {
            if (!res) {
                return;
            }
            const spinner = SpinnerService.start('.mini-basket-popover .popover-body');
            const refKey = this.userService.userDetail && this.userService.userDetail.custNr;
            this.shoppingCartService.transferOffer({
                refTextKeyStore: refKey,
                total: this.totalPrice,
                callback: (result: SagMessageData, orderRequest: any) => {
                    SpinnerService.stop(spinner);
                    if (result.type === 'SUCCESS') {
                        if (this.miniBasketRef) {
                            this.miniBasketRef.hide();
                        }
                        this.articleListSearchStorageService.clearAllHistory(get(this.userService, 'userDetail.id'));
                    } else {
                        const isForeDisabledIfNotTimeOutMessage = TIME_OUT_MESSAGE.indexOf(result.message) === -1;
                        callback(isForeDisabledIfNotTimeOutMessage);
                    }
                    this.orderAnalyticService.sendTransferBasketEventData({
                        shoppingBasket: this.miniBasket,
                        shoppingBasketContext: this.shoppingBasketContext,
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

    exportOci(callback) {
        if (!this.isOciFlow) {
            return;
        }
        this.ociService.getOciExportData(this.ociInfo.action).subscribe(
            response => {
                this.shoppingBasketAnalyticService.sendExportOciEventData(this.miniBasket);
                this.miniBasketRef.hide();
                const htmlSrc = response.toString();
                this.userService.logout();
                setTimeout(() => {
                    document.querySelector('html').innerHTML = htmlSrc;
                }, 100);
            },
            error => {
                if (callback) {
                    callback();
                }
                this.errorMessage = 'MESSAGES.GENERAL_ERROR';
            }
        );
    }

    async processDmsOffer(callback) {
        this.shoppingBasketService.loadShoppingBasket().subscribe(async () => {
            let offer;
            try {
                if (NumberUtil.isNumber(this.dmsInfo.offerId)) {
                    offer = await this.shoppingCartService.addToExistingOfferForDMS(this.dmsInfo.offerId, this.dmsInfo, this.miniBasket);
                    this.getAllVehicleLabourTimes(offer, callback);
                    return;
                }
                if (DmsUtil.isDmsOfferPresent(this.dmsInfo)) {
                    offer = await this.shoppingCartService.addToNewOfferForDMS(this.dmsInfo, this.miniBasket);
                    this.getAllVehicleLabourTimes(offer, callback);
                    return;
                }
            } catch (error) {
                if (error.code === Constant.FORBIDDEN_CODE) {
                    this.dmsService.showDmsOfferPermissionErr();
                }
            }
            this.getAllVehicleLabourTimes(offer, callback);
        });
    }

    async exportOffer(offer, callback) {
        const exportOrder: ExportOrder = this.shoppingCartService.buildDmsExportRequestData(
            this.dmsInfo,
            offer,
            DmsExportCommand.OFFER,
            this.shoppingBasketContext,
            this.miniBasket,
            this.labourTimes
        );
        this.dmsService.export(exportOrder).then((err: string) => {
            this.errorMessage = err;
            if (callback) {
                callback();
            }
        });
    }

    getLabourTime(basket: ShoppingBasketModel) {
        if (!basket) {
            return;
        }
        const licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        if (this.dmsInfo && licenseType === HAYNESPRO_LICENSE.ULTIMATE.toString()) {
            const groupVehiclesCategories = basket.vehicleGroup(false);
            this.labourTimes = this.labourTimeService.loadLabourTimesForAllVehicle(groupVehiclesCategories);
        }
    }

    checkHaynesproLicense(hasPermission) {
        if (!hasPermission) {
            return;
        }
        if (this.dmsInfo) {
            this.haynesProService.getHaynesProLicense().subscribe((res: HaynesProLicenseSettings) => {
                this.haynesProLicense = res;
                this.getLabourTime(this.miniBasket);
            });
        }
    }

    updateBranch(branchId) {
        const collection = (this.branchesForPickup || []).find(type => type.branchId === branchId);
        if (collection) {
            const updatedContext = new ShoppingBasketContextModel(this.shoppingBasketContext);
            updatedContext.pickupBranch = { ...collection };
            this.updateContext(updatedContext);
        }
    }

    updateDeliveryAddress(deliveryAddressId) {
        const collection = (this.deliveryAddress || []).find(type => type.id === deliveryAddressId);
        if (collection) {
            const updatedContext = new ShoppingBasketContextModel(this.shoppingBasketContext);
            updatedContext.deliveryAddress = { ...collection };
            this.updateContext(updatedContext);
        }
    }

    onChangeDelivery(descCode: string) {
        const delivery = (this.deliveryTypes || []).find(type => type.descCode === descCode);
        if (delivery) {
            const updatedContext = new ShoppingBasketContextModel(this.shoppingBasketContext);
            updatedContext.orderType = ORDER_TYPE.ORDER;
            updatedContext.deliveryType = { ...delivery };

            if(delivery.descCode === DELIVERY_TYPE.TOUR && this.defaultBranch && this.defaultBranch.branchId) {
                updatedContext.pickupBranch = this.defaultBranch;
            }

            this.updateContext(updatedContext);
        }
    }

    get shoppingBasketContext() {
        return this.appContextService.shoppingBasketContext;
    }

    updateContext(body) {
        this.appContextService.updateShoppingBasketContext(body)
            .pipe(takeUntil(this.destroy$))
            .subscribe(res => {
                this.broadcaster.broadcast(ArticleBroadcastKey.MINI_BASKET_CONDITION_EVENT, this.shoppingBasketContext);
            });
    }

    get currentDeliveryType() {
        if(this.isSB || (this.salesUser && !this.isSalesOnBeHalf)) {
            return null;
        }

        return this.shoppingBasketContext && this.shoppingBasketContext.deliveryType && this.shoppingBasketContext.deliveryType.descCode || null;
    }

    get deliveryTypeDisplay() {
        if(this.isSB || (this.salesUser && !this.isSalesOnBeHalf || !this.currentDeliveryType)) {
            return '';
        }

        if(this.defaultDeliveryType === this.currentDeliveryType) {
            return this.translateService.instant(`ARTICLE.${this.currentDeliveryType}`);
        } else {
            return `> ${this.translateService.instant(`ARTICLE.${this.currentDeliveryType}`)} <`;
        }
    }

    private getTotalPrice(currentStateNetPriceView) {
        return this.miniBasket.initTotalInclVat(currentStateNetPriceView);
    }

    private hasNewItem(oldBasket: ShoppingBasketModel, newBasket: ShoppingBasketModel) {
        if (!oldBasket || !newBasket) {
            return false;
        }
        const oldCartKeys = (oldBasket.items || []).map(item => item.cartKey);
        const newCartKeys = (newBasket.items || []).map(item => item.cartKey);
        return difference(newCartKeys, oldCartKeys).length > 0;
    }

    private loadMiniBasket() {
        this.miniBasketLoading = true;
        this.shoppingBasketService.loadMiniBasket();
    }

    private checkValidBranch(callback?: any) {
        if (this.shoppingCartService.byPassHiddenBranchCheck()) {
            this.errorMessage = '';
            return of(true);
        }

        return this.customerService.getCustomerBranches()
            .pipe(
                takeUntil(this.destroy$),
                catchError(() => of([])),
                switchMap(res => {
                    const context = this.shoppingBasketContext;
                    const branchId = context && context.pickupBranch && context.pickupBranch.branchId || '';
                    const isHiddenBrand = branchId && !(res || []).some(item => item.branchId === branchId);

                    if (isHiddenBrand) {
                        this.errorMessage = 'SHOPPING_BASKET.BRANCH_REQUIRED';
                        if (this.miniBasketContainer && this.miniBasketContainer.nativeElement) {
                            this.miniBasketContainer.nativeElement.scrollTop = 0;
                        }
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

    private filterAddress(settings: PaymentSetting) {
        const addresses = (settings.addresses || []).map(item => new UserAddress(item));
        this.deliveryAddress = addresses
            .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DELIVERY]
                || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);

        // remove default if more than one address type
        if (this.deliveryAddress.length > 1) {
            this.deliveryAddress = this.deliveryAddress.filter(item => {
                return item.addressType !== ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT];
            });
        }
    }

    private getAllVehicleLabourTimes(offer, callback) {
        if (!this.miniBasket) {
            this.exportOffer(offer, callback);
            return;
        }
        const licenseType = this.haynesProLicense && this.haynesProLicense.licenseType;
        if (this.dmsInfo && licenseType === HAYNESPRO_LICENSE.ULTIMATE.toString()) {
            const groupVehiclesCategories = this.miniBasket.vehicleGroup(false);
            this.labourTimeService.getAllVehicleLabourTimes(groupVehiclesCategories).then(values => {
                if (values && values.length > 0) {
                    const newArr = [];
                    values.forEach(item => {
                        if (item.vehicleId) {
                            newArr.push(item);
                        }
                    });
                    this.labourTimes = newArr;
                } 
                this.exportOffer(offer, callback);
            });
            return;
        }
        this.exportOffer(offer, callback);
    }
}
