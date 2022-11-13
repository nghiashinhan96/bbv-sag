import { Component, OnInit, Input, ViewChild } from '@angular/core';

import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { environment } from 'src/environments/environment';
import { LibUserSetting, AvailabilityUtil, ArticleModel, CZ_AVAIL_STATE, ArticleAvailabilityModel } from 'sag-article-detail';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { AffiliateEnum, AffiliateUtil, SAG_AVAIL_DISPLAY_STATES, MarkedHtmlPipe, SagAvailDisplaySettingModel, SAG_AVAIL_DISPLAY_OPTIONS } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';
import { Router } from '@angular/router';
import { get, sumBy } from 'lodash';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'connect-shopping-order-article-detail',
    templateUrl: './shopping-order-article-detail.component.html',
    styleUrls: ['./shopping-order-article-detail.component.scss']
})
export class ShoppingOrderArticleDetailComponent implements OnInit {
    @Input() cartItem: ShoppingBasketItemModel;
    @Input() set userSetting(val: LibUserSetting) {
        if (val) {
            this.popoverDelay = val.mouseOverFlyoutDelay || 0;
            this.availDisplaySettings = val.availDisplaySettings;
            this.dropShipmentAvailability = val.dropShipmentAvailability;
            this.additionClass = !this.cartItem.vin && this.cartItem.checkDeliverable(val.hasAvailabilityPermission, this.affiliateCode) &&
                !(get(this.cartItem, 'articleItem.availabilities.length') > 1 && !this.dropShipmentAvailability);
            this.grossPrice = this.cartItem.getGrossPrice(this.storage.currentStateVatConfirm);
            this.netPrice = this.cartItem.getNetPrice(this.storage.currentStateVatConfirm);
            this.articlePrice = this.cartItem.getArticlePrice(val.currentStateNetPriceView, this.storage.currentStateVatConfirm);
            this.cartItem.setFCNetPrice(val, this.storage.currentStateVatConfirm)
            if (this.isFinalCustomer) {
                if (val.fcUserCanViewNetPrice) {
                    if (this.storage.currentStateVatConfirm) {
                        this.articlePrice = this.cartItem.totalFinalCustomerNetPriceWithVat;
                    } else {
                        this.articlePrice = this.cartItem.totalFinalCustomerNetPrice;
                    }
                } else {
                    if (this.storage.currentStateVatConfirm) {
                        this.articlePrice = this.cartItem.totalGrossPriceWithVat;
                    } else {
                        this.articlePrice = this.cartItem.totalGrossPrice;
                    }
                }
            }

            this.hasAvailabilityPermission = val.hasAvailabilityPermission;
            this.currentStateNetPriceView = val.currentStateNetPriceView;
            this.setVisibleOfNetPrice();
        }
    }

    @ViewChild('pop', { static: false }) pop: any;

    genArtDescription: string;

    affiliateCode = environment.affiliate;
    additionClass = false;
    grossPrice: number;
    netPrice: number;
    isShownDiscount: boolean;
    articlePrice: number;
    hasAvailabilityPermission: boolean;
    currentStateNetPriceView: boolean;
    priceType: string;
    czAvailState = CZ_AVAIL_STATE;
    cz = AffiliateEnum.CZ;
    ehcz = AffiliateEnum.EH_CZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    isCz = AffiliateUtil.isCz(environment.affiliate);
    isCH: boolean;
    isFinalCustomer = AffiliateUtil.isFinalCustomerAffiliate(environment.affiliate);
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isShowNetPrice = false;
    availDisplayStates = SAG_AVAIL_DISPLAY_STATES;
    isConfirmationPage = false;
    availDisplaySettings: SagAvailDisplaySettingModel[];
    artAvailabilities: ArticleAvailabilityModel[];
    dropShipmentAvailability: boolean;
    popoverDelay: number;
    hasInvalidAvail: boolean;
    availableQuantity: number;
    latestAvailIndex: number;

    sb = AffiliateEnum.SB;
    isSB = AffiliateUtil.isSb(environment.affiliate);

    constructor(
        public storage: AppStorageService,
        public userService: UserService,
        private markedHtmlPipe: MarkedHtmlPipe,
        private router: Router,
        private translateService: TranslateService,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
        this.priceType = this.getPriceType(this.cartItem.articleItem);
        this.isConfirmationPage = this.router.url.indexOf('shopping-basket/order') > -1;

        this.artAvailabilities = AvailabilityUtil.sortAvailWithLatestTime(get(this.cartItem, 'articleItem.availabilities'));
        this.hasInvalidAvail = this.artAvailabilities.some(avail => avail.backOrder);
        this.availableQuantity = sumBy(this.artAvailabilities, a => a.backOrder ? 0 : a.quantity);
        this.latestAvailIndex = (this.artAvailabilities || []).findIndex(a => !a.backOrder);
    }

    get hasAdditionClass() {
        return this.additionClass && !(this.artAvailabilities && this.artAvailabilities.length > 1 && this.hasInvalidAvail);
    }

    getSpecialNotes(availability) {
        if (this.userService && this.userService.userPrice) {
            return AvailabilityUtil.initSpecialNotesWithDeliveryTour(this.userService.userPrice.availDisplaySettings, availability, environment.affiliate, this.isConfirmationPage);
        }
        return '';
    }

    getAvailDisplayText(availability) {
        if (this.isConfirmationPage) {
            return `
            ${availability.formattedCETArrivalDate}
            ${availability.formattedCETArrivalTime}`;
        }

        const text = AvailabilityUtil.getAvailTextDisplayWhenHaveTime(this.userService.userPrice.availDisplaySettings, availability, this.storage.appLangCode, environment.affiliate);
        if (text) {
            return this.markedHtmlPipe.transform(text);
        }

        return '';
    }

    getAvailDisplayTextCaseSofort() {
        const availSetting = AvailabilityUtil.getAvailSettingByAvailState(this.availDisplaySettings, this.availDisplayStates.PARTIALLY_AVAILABLE);
        if (availSetting && availSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT) {
            const text = AvailabilityUtil.getAvailContentByLanguageCode(availSetting.listAvailText, this.appStorage.appLangCode);
            if (text) {
                return this.markedHtmlPipe.transform(text);
            }

            return '';
        }

        return this.translateService.instant('ARTICLE.DELIVERY_IMMEDIATE');
    }

    getAvailColor(availability, state?: string) {
        if (AffiliateUtil.isAffiliateCZ9(environment.affiliate)) {
            return '';
        }

        if (availability.conExternalSource && this.isCH) {
            state = this.availDisplayStates.NOT_AVAILABLE;
        }

        if (!state) {
            state = AvailabilityUtil.getAvailStateWhenHaveTime(availability);
        }

        return AvailabilityUtil.getAvailColorByAvailState(this.userService.userPrice.availDisplaySettings, environment.affiliate, state);
    }

    getAvailConfirmTextColor() {
        return AvailabilityUtil.getAvailConfirmTextColor(this.userService.userPrice.availDisplaySettings);
    }

    get isArticle24h() {
        if (this.isEhCz) {
            return AvailabilityUtil.isArticle24hCz(this.cartItem.articleItem && this.cartItem.articleItem.availabilities || []);
        }
        return AvailabilityUtil.isArticle24h(this.cartItem.articleItem && this.cartItem.articleItem.availabilities || []);
    }

    private getPriceType(article: ArticleModel) {
        if (article.displayedPrice) {
            return `${article.displayedPrice.type} ${article.displayedPrice.brand}`;
        }
        return 'UVPE';
    }

    setVisibleOfNetPrice() {
        const userSettings = this.userService && this.userService.userPrice;
        if (userSettings) {
            this.isShowNetPrice = userSettings.currentStateNetPriceView;
            if (this.isFinalCustomer) {
                this.isShowNetPrice = userSettings.netPriceView && userSettings.fcUserCanViewNetPrice;
            }
        }
    }
}
