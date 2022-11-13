import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';

import { finalize } from 'rxjs/operators';

import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { UserService } from 'src/app/core/services/user.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ErrorCodeEnum } from 'src/app/core/enums/error-code.enum';
import { ShoppingBasketHistoryService } from 'src/app/core/services/shopping-basket-history.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { SB_LOCATION_TYPES } from 'sag-article-detail';

@Component({
    selector: 'connect-save-basket',
    templateUrl: './save-basket.component.html',
    styleUrls: ['./save-basket.component.scss']
})
export class SaveBasketComponent implements OnInit, OnChanges {
    @Input() disabled: boolean;
    @Input() set context(val: ShoppingBasketContextModel) {
        this.basketContext = val;
    };

    @Output() savedEmitter = new EventEmitter();
    basketHistoryName = '';
    isSaving = false;
    state = null;
    isSB: boolean;
    customerRefText: string = '';
    basketContext: ShoppingBasketContextModel;

    locationTypesForGetCustomerRef = [
        SB_LOCATION_TYPES.PRIMARY,
        SB_LOCATION_TYPES.SECONDARY,
        SB_LOCATION_TYPES.THIRD
    ];

    isEhCh = AffiliateUtil.isEhCh(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);

    constructor(
        private shoppingBasketService: ShoppingBasketService,
        private shoppingBasketHistoryService: ShoppingBasketHistoryService,
        private userService: UserService,
        private appStore: AppStorageService
    ) {
        this.isSB = AffiliateUtil.isSb(environment.affiliate);
    }

    ngOnInit() {
    }

    ngOnChanges(change: SimpleChanges) {
        if(change.context && change.context.currentValue && !change.context.firstChange) {
            this.basketContext = change.context.currentValue;
        }
    }

    saveBasket(event, isClearBasket = false) {
        const customerRefText = this.getCustomerRefText();
        let netPriceViewInContext = this.userService.userPrice.currentStateNetPriceView;
        
        if (this.isEhCh || this.isEhCz) {
            netPriceViewInContext = this.userService.userPrice.fcUserCanViewNetPrice;
        }

        const body = {
            basketName: this.basketHistoryName,
            netPriceViewInContext,
            customerRefText,
        };
        this.isSaving = true;
        SpinnerService.start('connect-save-basket .save-basket-container', {
            class: 'sm',
            containerMinHeight: 0
        });
        this.shoppingBasketHistoryService.saveBasketHistory(body, this.shoppingBasketService.basketType)
            .pipe(
                finalize(() => {
                    this.isSaving = false;
                    SpinnerService.stop('connect-save-basket .save-basket-container');
                })
            )
            .subscribe(res => {
                if (res !== ErrorCodeEnum.UNKNOWN_ERROR) {
                    this.state = 'success';
                    this.basketHistoryName = '';

                    if (isClearBasket) {
                        // clear ref text;
                        this.shoppingBasketService.removeAllBasket()
                            .pipe(
                                finalize(() => {
                                    this.isSaving = false;
                                    SpinnerService.stop('connect-save-basket .save-basket-container');
                                })
                            )
                            .subscribe(returnedBasket => {
                                if (returnedBasket !== ErrorCodeEnum.UNKNOWN_ERROR) {
                                    this.savedEmitter.emit(true);
                                    this.shoppingBasketService.clearBasket();
                                }
                            });
                    } else {
                        this.isSaving = false;
                        SpinnerService.stop('connect-save-basket .save-basket-container');
                    }
                } else {
                    this.state = 'error';
                }
            });
    }

    private getCustomerRefText() {
        let customerRefText = '';

        if(this.isSB) {
            if(this.basketContext.eshopBasketContextByLocation.length > 0) {
                const contextHasReferenceText = this.basketContext.eshopBasketContextByLocation.filter(item => !!item.referenceTextByLocation);
                if(contextHasReferenceText.length > 0) {
                    this.locationTypesForGetCustomerRef.forEach(type => {
                        if(!customerRefText) {
                            const item = contextHasReferenceText.find(con => con.location.locationType === type);
                            if(item) {
                                customerRefText = item.referenceTextByLocation;
                            }
                        }
                    });
                }
            }
        } else {
            const customerNr = this.userService.userDetail.custNr;
            customerRefText = this.appStore.refText[customerNr];
        }

        return customerRefText;
    }

}
