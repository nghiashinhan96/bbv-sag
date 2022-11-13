import { Injectable, Injector } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { DmsInfo } from '../models/dms-info.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { ExportOrder } from '../models/export-order.model';
import { ExportOrderDetail } from '../models/export-order-detail.model';
import { DmsUtil } from '../utils/dms.util';
import { DmsError } from '../models/dms-error.model';
import { Validator } from 'src/app/core/utils/validator';
import { DmsExportCommand } from '../enums/dms.enum';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ReturnBasket } from '../models/return-basket.model';
import { StringUtil } from 'src/app/dms/utils/string.util';
import { DateUtil } from 'src/app/core/utils/date.util';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { NumberUtil } from 'src/app/dms/utils/number.util';
import { CollectionUtil } from 'src/app/dms/utils/collection.util';
import { ShoppingBasketUtil } from 'src/app/shopping-basket/utils/shopping-basket.util';
import { SpinnerService } from '../../core/utils/spinner';
import { DmsConstant } from '../constants/dms.constant';
import { DmsBaseService } from './dms-base.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { SagMessageData } from 'sag-common';
import { LocalStorageService } from 'ngx-webstorage';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { UserService } from 'src/app/core/services/user.service';
import { LabourTimeHelper } from 'sag-haynespro';
import { CurrencyUtil } from 'sag-currency';
import { SysNotificationComponent } from 'src/app/core/components/sys-notification/sys-notification.component';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { AVAILABILITY_INFO } from 'sag-article-detail';
import { AVAILABILITY_STATE } from 'src/app/analytic-logging/const/availability-state.constant';

const OFFER_NUMBER_MAX_LENGTH = 10;

@Injectable({
    providedIn: 'root'
})
export class DmsService extends DmsBaseService {

    private emitterLaboursTime = new BehaviorSubject<any>([]);
    laboursTimeSubscriber$ = this.emitterLaboursTime.asObservable();

    private offerErrModalRef: BsModalRef;

    constructor(
        protected translateService: TranslateService,
        protected http: HttpClient,
        protected dmsStorageService: DmsBaseService,
        protected appStorage: AppStorageService,
        protected storage: LocalStorageService,
        protected basketService: ShoppingBasketService,
        protected userService: UserService,
        protected googleAnalyticsService: GoogleAnalyticsService,
        protected injector: Injector
    ) {
        super(storage, http, appStorage);
        this.userService.userDetail$.subscribe(userDetail => {
            const dmsInfo = this.getDmsInfo();
            if (userDetail && userDetail.id && dmsInfo && dmsInfo.reference && !Object.keys(this.appStorage.refText).length) {
                this.appStorage.refText = { [this.userService.userDetail.custNr]: dmsInfo.reference };
            }
        });
    }

    get modalService() {
        return this.injector.get(BsModalService);
    }

    setLaboursTime(values) {
        this.emitterLaboursTime.next(values);
    }

    processSearch(dmsInfo: DmsInfo, hasDmsPermission: boolean) {

        if (!dmsInfo) {
            return;
        }
        if (DmsUtil.isValidCloudDms(dmsInfo.token, dmsInfo.hookUrl) && !hasDmsPermission) {
            console.log('User does not have dms permission');
            this.userService.logout();
            return;
        }
        if (NumberUtil.isNumber(dmsInfo.offerId)) {
            // #3022: When this value is set then the search entry fields are ignored
            this.addOfferItemsToCart(dmsInfo.offerId);
            return;
        }
        const router = this.injector.get(Router);
        if (Number(dmsInfo.searchFlag) === 0 && Number(dmsInfo.orderFlag) === 1) {
            router.navigateByUrl(Constant.SHOPPING_LIST_PAGE);
        } else if (dmsInfo.vin || dmsInfo.typenschein || dmsInfo.vehicleText) {
            router.navigateByUrl(Constant.HOME_PAGE);
        } else if (dmsInfo.articleNumbers && dmsInfo.articleNumbers.length > 0) {
            this.addArticleNumbersToCart(dmsInfo.articleNumbers, dmsInfo.articleQuantities);
        } else {
            router.navigateByUrl(Constant.HOME_PAGE);
        }
    }

    buildExportOrders(exportOrder: ExportOrder, shoppingCart: ShoppingBasketModel, labourTimes?) {
        let currentVehicleId = null;
        for (let i = 0; i < shoppingCart.items.length; i++) {
            const item = shoppingCart.items[i];
            if (!currentVehicleId) {
                currentVehicleId = item.vehicleId;
            }
            // check when move to new vehicleId, add labourTime below basket-items with that vehicle
            if (currentVehicleId !== item.vehicleId && labourTimes) {
                const labourRecords = this.buildLabourTimeWithVehicleId(currentVehicleId, labourTimes);
                exportOrder.orders = exportOrder.orders.concat(labourRecords);
                currentVehicleId = item.vehicleId;
            }
            exportOrder.orders.push(this.buildExportOrderDetail(item, exportOrder.version));
            if (item.attachedCartItems) {
                item.attachedCartItems.forEach(attachedCartItem => {
                    exportOrder.orders.push(this.buildExportOrderDetail(attachedCartItem, exportOrder.version));
                });
            }

            // if the last item, also add labour-time to that vehicle
            if (labourTimes && (i === shoppingCart.items.length - 1)) {
                const labourRecords = this.buildLabourTimeWithVehicleId(currentVehicleId, labourTimes);
                exportOrder.orders = exportOrder.orders.concat(labourRecords);
            }
        }
    }

    buildLabourTimeWithVehicleId(vehicleId, labourTimesArr) {
        if (CollectionUtil.isEmpty(labourTimesArr)) {
            return [];
        }
        const arrResult = labourTimesArr.filter(item => item.vehicleId === vehicleId)[0];
        const exportLaboursTime = [];
        if (CollectionUtil.isEmpty(arrResult)) {
            return [];
        }

        for (const item of arrResult) {
            const labourRateVatWithTime = CurrencyUtil.roundHalfEvenTo2digits(
                LabourTimeHelper.getLabourRateWithTime(item.time, item.labourRateWithVat));
            const labourRateWithTime = CurrencyUtil.roundHalfEvenTo2digits(LabourTimeHelper.getLabourRateWithTime(item.time, item.labourRate));
            const labourRate = CurrencyUtil.roundHalfEvenTo2digits(item.labourRate);
            const awNumber = DmsConstant.LABOUR_TIME_PREFIX + (item.awNumber ? item.awNumber : '');
            exportLaboursTime.push({
                articleNumber: awNumber,
                totalGrossPriceIncl: StringUtil.toString(labourRateVatWithTime),
                totalGrossPrice: StringUtil.toString(labourRateWithTime),
                grossPrice: StringUtil.toString(labourRate),
                description: item.name,
                quantity: item.time,
                totalNetPriceInclVat: '',
                totalNetPrice: '',
                netPrice: '',
                totalUvpeIncl: '',
                totalUvpe: '',
                uvpe: '',
            } as ExportOrderDetail
            );
        }
        return exportLaboursTime;
    }

    buildExportOrderDetail(item: ShoppingBasketItemModel, version: string): ExportOrderDetail {
        let articleNumber = item.articleItem && item.articleItem.article && item.articleItem.article.number
            ? item.articleItem.article.number : '';
        let artAdditionalDescription = item.articleItem && item.articleItem.productAddon || '';
        let description: string;
        if (item.depot) {
            if (item.pfand) {
                description = this.translateService.instant('SHOPPING_BASKET.PFAND_TEXT');
            } else {
                description = this.translateService.instant('SHOPPING_BASKET.DEPOT_TEXT');
            }
        } else if (item.recycle) {
            description = this.translateService.instant('SHOPPING_BASKET.RECYCLING_TEXT');
        } else if (item.voc) {
            description = this.translateService.instant('SHOPPING_BASKET.VOC_TEXT');
        } else if (item.vrg) {
            description = this.translateService.instant('SHOPPING_BASKET.VRG_TEXT');
        } else {
            let descArr = [];
            const desc = item.articleItem && item.articleItem.genArtTxts ? item.articleItem.genArtTxts[0].gatxtdech : '';
            if (desc) {
                descArr.push(desc);
            }
            if (artAdditionalDescription) {
                descArr.push(artAdditionalDescription);
            }
            description = descArr.join(', ');
            articleNumber = item.articleItem && item.articleItem.artnrDisplay || '';  // artnr_display
        }

        let totalNetPriceInclVat = '';
        let totalNetPrice = '';
        let netPrice = '';
        let totalUvpeIncl = ''; // Column 10. total of Suggested Retail Price include vat
        let totalUvpe = ''; // Column 11. total of UVPE price
        let uvpe = ''; // Column 12. Single UVPE price
        let brand = item.articleItem && item.articleItem.productBrand || '';

        let deliveryStatus = this.googleAnalyticsService.getAvailabilityState(item.articleItem);

        const displayAvail = item.articleItem.getDisplayAvail(true);

        let fromBranchTime = displayAvail && displayAvail.arrivalTime;
        switch (deliveryStatus) {
            case AVAILABILITY_STATE.NOT_AVAILABLE:
            case AVAILABILITY_STATE.NOT_ORDERABLE:
            case AVAILABILITY_STATE.PARTIALLY_AVAILABLE:
                fromBranchTime = 'n/a';
                break;
        }

        let deliveryNote = 'Pick-Up';
        if (displayAvail && displayAvail.sendMethodCode === AVAILABILITY_INFO.TOUR) {
            deliveryNote = displayAvail.tourName;
        }

        if (version === DmsConstant.VERSION_3) {
            totalNetPriceInclVat = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalNetPriceWithVat));
            totalNetPrice = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalNetPrice));
            netPrice = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.netPrice));
            totalUvpeIncl = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalGrossPriceWithVat)); // #2679
            totalUvpe = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalGrossPrice)); // #2679
            uvpe = StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.grossPrice)); // #2679
        }

        return {
            articleNumber,
            totalGrossPriceIncl: StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalGrossPriceWithVat)),
            totalGrossPrice: StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.totalGrossPrice)),
            grossPrice: StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(item.grossPrice)),
            description,
            quantity: item.quantity,
            totalNetPriceInclVat,
            totalNetPrice,
            netPrice,
            totalUvpeIncl,
            totalUvpe,
            articleId: parseInt(item.articleItem.artid, 10),
            uvpe,
            brand,
            artAdditionalDescription,
            fromBranchTime,
            deliveryStatus,
            deliveryNote
        } as ExportOrderDetail;
    }

    addArticleNumbersToCart(articleNrs: string[], articleQuantities: number[]) {
        articleNrs.forEach((artNr, index, theArray) => {
            theArray[index] = Validator.removeWhiteSpace(Validator.removeNonAlphaCharacter(artNr)).toLowerCase();
        });
        SpinnerService.start();
        this.addArticlesToShoppingCart(articleNrs, articleQuantities).subscribe(
            response => {
                SpinnerService.stop();
                this.handleUpdateShoppingCart(response);
            },
            error => {
                SpinnerService.stop();
                this.dmsStorageService.updateDmsError(DmsError.builder().articlesNotFound(articleNrs).build());
                this.gotoShoppingPage();
            }
        );
    }

    addOfferItemsToCart(offerId) {
        SpinnerService.start();
        this.addOfferToShoppingCart(offerId).subscribe(
            () => {
                SpinnerService.stop();
                this.gotoShoppingPage();
            },
            err => {
                const { error } = err;
                SpinnerService.stop();
                if (error.code === Constant.FORBIDDEN_CODE) {
                    this.showDmsOfferPermissionErr();
                } else {
                    this.dmsStorageService.updateDmsError(DmsError.builder().offerIdFailed(offerId).build());
                    this.gotoShoppingPage();
                }
            }
        );
    }

    buildExportRequestGeneralInfo(dmsInfo: DmsInfo, user: UserDetail, basketContext, basketItems,
        exportCommand: DmsExportCommand, customerMessage: string, orderNumber: string): ExportOrder {
        const dmsCommand = exportCommand === DmsExportCommand.ORDER ? DmsConstant.CMD_ORDER : DmsConstant.CMD_OFFER;
        const requestType = this.translateService.instant('DMS.TYPE.' + exportCommand.toString());
        const companyName = StringUtil.defaultIfBlank(basketContext.billingAddress.companyName);
        let street = basketContext.billingAddress.street;
        street = StringUtil.removeNewLine(street);
        let postCode = basketContext.billingAddress.postCode;
        postCode = StringUtil.removeNewLine(postCode);
        let city = basketContext.billingAddress.city;
        city = StringUtil.removeNewLine(city);
        const exportOrder = {
            basePath: dmsInfo.basePath,
            fileName: dmsInfo.fileName,
            orders: [],
            customerNr: user.custNr,
            requestType,
            orderNumber,
            orderDate: DateUtil.getCurrentDateTime(),
            totalPriceInclVat: StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(ShoppingBasketUtil.refreshTotalGrossInclVat(basketItems))),
            totalPrice: StringUtil.toString(CurrencyUtil.roundHalfEvenTo2digits(basketItems.grossTotalExclVat)),
            deliveryType: this.translateService.instant('CONDITION.DELIVERY_TYPE.' + basketContext.deliveryType.descCode),
            paymentMethod: this.translateService.instant('CONDITION.PAYMENT_METHOD.' + basketContext.paymentMethod.descCode),
            companyName,
            street,
            postCode,
            city,
            note: customerMessage,
            version: dmsInfo.version,
            hookUrl: dmsInfo.hookUrl,
            dmsCommand
        } as ExportOrder;

        return exportOrder;
    }

    postReturnBasket(returnBasket: ReturnBasket, hookUrl: string) {
        return this.postToThirdParty(hookUrl, returnBasket);
    }

    postToThirdParty(url: string, body: any) {
        const returnBasketHeaders = {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            Authorization: `Bearer ${this.appStorage.appToken}`,
            IgnoreOfflineError: 'true'
        };
        return this.http.post(url,
            body,
            {
                headers: new HttpHeaders(returnBasketHeaders)
            });
    }

    updateDmsOfferNumber(dmsInfo: DmsInfo, selectedOffer) {
        if (dmsInfo && StringUtil.isNotBlank(dmsInfo.reference)) {
            selectedOffer.offerNr = dmsInfo.reference.substring(0,
                Math.min(OFFER_NUMBER_MAX_LENGTH, dmsInfo.reference.length));
            dmsInfo.reference = '';
            this.dmsStorageService.updateDmsInfo(dmsInfo);
        }
    }

    showDmsOfferPermissionErr() {
        const modalService = this.injector.get(BsModalService);

        this.offerErrModalRef = modalService.show(SysNotificationComponent, {
            class: 'modal-sm sys-notification',
            ignoreBackdropClick: true,
            initialState: {
                message: 'DMS.OFFER_NOT_SUPPORTED',
                showCancelButton: false,
                okButton: 'OK'
            }
        });

        this.offerErrModalRef.content.close = () => {
            this.navigateToHomePage();
            this.offerErrModalRef.hide();
        };
    }

    private handleUpdateShoppingCart(response) {
        this.dmsStorageService.updateDmsError(DmsError.builder().articlesNotFound(response.notValidArticleNumbers).build());
        this.gotoShoppingPage();
    }

    private gotoShoppingPage() {
        const router = this.injector.get(Router);
        history.pushState({ urlPath: Constant.HOME_PAGE }, '', Constant.HOME_PAGE);
        router.navigateByUrl('shopping-basket/cart');
    }

    navigateToHomePage() {
        const router = this.injector.get(Router);
        router.navigateByUrl(Constant.HOME_PAGE);
    }

    getDmsErrorMessage(): SagMessageData {
        const dmsError = this.getDmsError();
        if (!dmsError) {
            return null;
        }
        this.removeDmsError();
        if (!CollectionUtil.isEmpty(dmsError.articlesNotFound)) {
            return {
                type: 'ERROR',
                message:  'DMS.ERROR.NOT_FOUND_ARTICLE_NUMBERS'
            };
        }
        if (dmsError.offerIdFailed) {
            this.removeDmsError();
            return {
                type: 'ERROR',
                message:  'DMS.ERROR.FAILED_TO_ADD_OFFER',
                params: { param: dmsError.offerIdFailed }
            } as SagMessageData;
        }

        return null;
    }

}
