import { Injectable } from '@angular/core';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { UserService } from 'src/app/core/services/user.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SagMessageData } from 'sag-common';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { map } from 'rxjs/internal/operators/map';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs/internal/observable/of';
import { ShoppingBasketTransferModel } from '../models/shopping-basket-transfer.model';
import { Router } from '@angular/router';
import { OfferDetail } from 'src/app/offers/models/offer-detail.model';
import { catchError } from 'rxjs/internal/operators/catchError';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { OffersService } from 'src/app/offers/services/offers.services';
import { TranslateService } from '@ngx-translate/core';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { OfferUpdateRequest } from 'src/app/offers/models/offer-detail-request.model';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { DmsExportCommand } from 'src/app/dms/enums/dms.enum';
import { ExportOrder } from 'src/app/dms/models/export-order.model';
import { DmsUtil } from 'src/app/dms/utils/dms.util';
import { NumberUtil } from 'src/app/dms/utils/number.util';
import { ShoppingBasketContextModel } from '../models/shopping-basket-context.model';
import { DELIVERY_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class ShoppingCartService {

    constructor(
        private translateService: TranslateService,
        private shoppingBasketService: ShoppingBasketService,
        private appContextService: AppContextService,
        private userService: UserService,
        private appStorage: AppStorageService,
        private creditLimitService: CreditLimitService,
        private router: Router,
        private offersService: OffersService,
        private dmsService: ActiveDmsProcessor
    ) { }

    transferOffer({ refTextKeyStore, total, callback }:
        {
            refTextKeyStore: number | string,
            total: number,
            callback: (result, request) => void
        }
    ) {
        const orderRequest = this.getOrderRequest(refTextKeyStore, total);
        this.shoppingBasketService.createOfferToAx(orderRequest).subscribe((result: SagMessageData) => {
            this.transferBasketAndOfferResponeHandler(result, refTextKeyStore, callback, orderRequest);
        });
    }

    transferBasket({ refTextKeyStore, total, callback }:
        {
            currentBasket: ShoppingBasketModel,
            refTextKeyStore: number | string,
            total: number,
            callback: (result, request) => void
        }
    ) {
        const requestBody = this.getOrderRequest(refTextKeyStore, total);
        this.shoppingBasketService.transferBasket(requestBody)
            .subscribe((result: SagMessageData) => {
                this.transferBasketAndOfferResponeHandler(result, refTextKeyStore, callback, requestBody);
            });
    }

    addOffer({ currentBasket, refTextKeyStore, selectedOffer, callback }:
        {
            currentBasket: ShoppingBasketModel,
            refTextKeyStore: number | string,
            selectedOffer: OfferDetail,
            callback: (result: {
                status: 'success' | 'error',
                updatedOffer?: OfferDetail
            }) => void
        }
    ) {
        let offerRequest$: Observable<OfferDetail>;
        if (selectedOffer) {
            offerRequest$ = this.offersService.getOfferById(selectedOffer && selectedOffer.id);
        } else {
            offerRequest$ = this.offersService.createOffer().pipe(map((res: any) => res && res.offer || null));
        }
        offerRequest$.pipe(catchError(err => of(null)))
            .subscribe(result => {
                if (result) {
                    const offer = new OfferDetail(result);
                    const currentPositions = currentBasket.getOfferPostions(this.translateService);
                    offer.offerPositions = [...offer.offerPositions, ...currentPositions];
                    // update
                    this.updateOffer(offer).subscribe(updatedOffer => {
                        if (updatedOffer) {
                            this.appStorage.selectedOffer = updatedOffer;
                            this.shoppingBasketService.removeAllBasket().subscribe(res => { });
                            this.appStorage.cleanRefText(refTextKeyStore);
                            this.router.navigate(['/offers', 'edit', updatedOffer.id], { queryParams: { addFromBasket: true } });
                            callback({
                                status: 'success',
                                updatedOffer
                            });
                        } else {
                            callback({ status: 'error' });
                        }
                    }, () => callback({ status: 'error' }));
                } else {
                    callback({ status: 'error' });
                }
            });
    }

    async addToExistingOfferForDMS(offerId, dmsInfo: DmsInfo, currentBasket: ShoppingBasketModel) {
        const offer = await this.offersService.asyncGetOfferById(offerId);
        if (!offer) {
            return;
        }
        this.dmsService.updateDmsOfferNumber(dmsInfo, offer);
        // Clear all article related to the shopping basket in current offer first
        offer.offerPositions = this.offersService.removeBasketItemInOffer(offer.offerPositions);
        // Articles are updated to the Offer <offerNr> in Connect and the Shopping Basket cleared
        this.offersService.addItemsToOffer(offer, currentBasket);
        // Labour time is not supported in AX
        this.doUpdateOfferInfo(offer);
        return offer;
    }

    async addToNewOfferForDMS(dmsInfo: DmsInfo, currentBasket: ShoppingBasketModel) {
        const offer = await this.offersService.asyncCreateNewOffer();
        this.dmsService.updateDmsOfferNumber(dmsInfo, offer);
        this.offersService.addItemsToOffer(offer, currentBasket);
        // Labour time is not supported in AX when add from shopping cart to offer
        return this.doUpdateOfferInfo(offer);
    }

    private async doUpdateOfferInfo(offer: OfferDetail) {
        // Calculate the offer
        const updatedOffer = await this.offersService.asyncUpdateOffer(
            OfferUpdateRequest.convertToRequestUpdateOffer(offer), true);

        // Update the offer
        const result = await this.offersService.asyncUpdateOffer(
            OfferUpdateRequest.convertToRequestUpdateOffer(updatedOffer), false);
        if (!result) {
            return offer;
        }
        this.appStorage.selectedOffer = offer;
        return result;
    }

    buildDmsExportRequestData(
        dmsInfo: DmsInfo,
        offer: OfferDetail,
        dmsRequestType: DmsExportCommand,
        context: ShoppingBasketContextModel,
        currentBasket: ShoppingBasketModel,
        labourTimes: any[]
    ): ExportOrder {
        let orderNumber = DmsUtil.isDmsOfferPresent(dmsInfo) && offer ? offer.id : '';
        // #3269 Ensure the offer can be modified/added
        orderNumber = NumberUtil.isNumber(dmsInfo.offerId) ? dmsInfo.offerId : orderNumber;
        const exportOrder = this.dmsService.buildExportRequestGeneralInfo(dmsInfo, this.userService.userDetail,
            context,
            currentBasket,
            dmsRequestType, '', orderNumber.toString());
        this.dmsService.buildExportOrders(exportOrder, currentBasket, labourTimes);
        return exportOrder;
    }

    byPassHiddenBranchCheck() {
        const context = this.appContextService.shoppingBasketContext;
        const isFinalUserRole = this.userService.userDetail && this.userService.userDetail.isFinalUserRole;
        return !environment.enableHiddenBranch || isFinalUserRole || context && context.deliveryType && context.deliveryType.descCode !== DELIVERY_TYPE.PICKUP
    }

    private updateOffer(offer: OfferDetail) {
        // calculate
        return this.offersService.updateOffer(offer.updateRequestModel, true).pipe(
            switchMap(calculated => {
                const calculatedOfer = calculated && calculated.offer || null;
                if (calculatedOfer) {
                    return this.offersService.updateOffer(new OfferDetail(calculatedOfer).updateRequestModel, false).pipe(
                        map((updated: any) => updated && updated.offer || null)
                    );
                }
                return of(null);
            }),
            catchError(err => of(null))
        );
    }

    private getOrderRequest(refTextKeyStore: number | string, total: number) {
        const refTextObj = this.appStorage.refText;
        const custRefTxt = refTextObj && refTextObj[refTextKeyStore] || '';

        let orderFrom;
        const dmsInfo = this.dmsService.getDmsInfo();
        if (dmsInfo) {
            orderFrom = DmsUtil.getDMSSalesOrgin();
        }

        return new ShoppingBasketTransferModel({
            context: this.appContextService.shoppingBasketContext,
            custRefTxt,
            msg: '',
            total,
            user: this.userService.userDetail,
            finalCustomer: this.appStorage.goodReceiver,
            saleInfo: this.userService.employeeInfo,
            orderFrom
        }).orderRequest;
    }

    private transferBasketAndOfferResponeHandler(result: SagMessageData, refTextKeyStore: number | string, callback, request) {
        if (result.type === 'SUCCESS') {
            this.appStorage.cleanRefText(refTextKeyStore);
            this.shoppingBasketService.loadMiniBasket();
            this.creditLimitService.resetCreditCardInfo(this.userService.userDetail);
            this.appContextService.initAppContext();
            if (result.message && result.message.axOrderURL) {
                window.open(result.message.axOrderURL, '_blank');
            }
            this.router.navigate(['/home']);
        }
        callback(result, request);
    }
}
