import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { SagCurrencyPipe } from 'sag-currency';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { FeedbackStorageService } from './feedback-storage.service';
import { DateUtil } from 'src/app/core/utils/date.util';
import { FeedbackUtils } from '../utils/feedback.utils';
import { Constant, NOT_AVAILABLE } from 'src/app/core/conts/app.constant';
import { HeaderSearchTypeEnum } from 'src/app/layout/components/header/components/header-search/header-search-type.enum';
import { FeedbackArticleAvailabilityItem } from '../models/feedback-article-availability-item.model';
import { FeedbackArticleAvailability } from '../models/feedback-article-availability.model';
import { FeedbackArticleResult } from '../models/feedback-article-result.model';
import { FeedbackArticleSearch } from '../models/feedback-article-search.model';
import { FeedbackShoppingCartItem } from '../models/feedback-shopping-cart-model';
import { FeedbackTechicalData } from '../models/feedback-techical-data.model';
import { FeedbackVehicleSearch } from '../models/feedback-vehicle-search.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { AVAILABILITY_INFO, AvailabilityUtil } from 'sag-article-detail';
import { AppCommonService } from 'src/app/core/services/app-common.service';

const ARTICLE_RESULT_LIST_MAX_LENGTH = 20;

@Injectable({ providedIn: 'root' })
export class FeedbackRecordingService {
    private userId: number;
    private data: FeedbackTechicalData;

    constructor(
        private translateService: TranslateService,
        private currencyPipe: SagCurrencyPipe,
        private storage: FeedbackStorageService,
        private appContextService: AppContextService,
        private appCommonService: AppCommonService
    ) { }

    reloadData(user: UserDetail) {
        if (!user || !user.id) {
            return;
        }
        this.userId = user.id;
        const data = this.storage.getFeedbackTechnicalData(this.userId);
        this.data = new FeedbackTechicalData(data);
        this.saveDataToStorage();
    }

    getTechnicalData(): FeedbackTechicalData {
        this.data.website = window.location.href;
        return this.data;
    }

    recordSearchFreeTextAction(searchOptions: string[], keyword: string, isArticleSeachMode?) {
        if (!this.data) {
            return;
        }

        if (!searchOptions || !searchOptions.length) {
            return;
        }

        if (isArticleSeachMode) {
            const articleSeachModel = new FeedbackArticleSearch({
                articleName: keyword
            });
            this.recordArticleSearch(articleSeachModel);
            return;
        }
        if (searchOptions.some(opt => opt === HeaderSearchTypeEnum.VEHICLES_MOTOR.toLocaleLowerCase())) {
            const vehicleSearchModel = new FeedbackVehicleSearch({
                type: this.translateService.instant('SEARCH.MOTOCODE_DESC'),
                searchText: keyword
            });
            this.recordVehicleSearch(vehicleSearchModel);
            return;
        }
        if (searchOptions.some(
            opt => opt === HeaderSearchTypeEnum.ARTICLES.toLocaleLowerCase() ||
                opt === HeaderSearchTypeEnum.VEHICLES.toLocaleLowerCase() ||
                opt === HeaderSearchTypeEnum.PRODUCT_CATEGORY.toLocaleLowerCase()
        )
        ) {
            this.recordFreeTextSearchKey(keyword);
        }
    }

    recordOfferNr(offerNr: string) {
        if (!this.data) {
            return;
        }
        this.data.offerNr = offerNr;
        this.saveDataToStorage();
    }

    recordVehicleInfo(vehicleInfo: string) {
        if (!this.data) {
            return;
        }
        this.data.vehicle = vehicleInfo;
        this.saveDataToStorage();
    }

    recordSearchByVinAction(vin: string) {
        if (!this.data) {
            return;
        }
        this.recordVehicleSearch(new FeedbackVehicleSearch({
            type: this.translateService.instant('SEARCH.VIN_20'),
            searchText: vin
        }));
    }

    recordVehicleSearch(vehicleSearch: FeedbackVehicleSearch) {
        if (!this.data) {
            return;
        }
        this.data.vehicleSearch = vehicleSearch;
        this.saveDataToStorage();
    }

    recordArticleResult(articleDoc) {
        if (!this.data || !articleDoc) {
            return;
        }
        if (!this.data.articleResults) {
            this.data.articleResults = [];
        }

        let priceItem;
        if (articleDoc.price && articleDoc.price.price) {
            priceItem = articleDoc.price.price;
        }

        const articleResult = new FeedbackArticleResult({
            articleId: articleDoc.artid,
            articleNr: articleDoc.artnr,
            articleName: articleDoc.artnr_display || articleDoc.artnrDisplay,
            brand: articleDoc.supplier,
            oepPrice: FeedbackUtils.getValue(priceItem ? this.currencyPipe.transform(priceItem.oepPrice) : null),
            uvpePrice: FeedbackUtils.getValue(priceItem ? this.currencyPipe.transform(priceItem.uvpePrice) : null),
            netPrice: FeedbackUtils.getValue(priceItem ? this.currencyPipe.transform(priceItem.netPrice) : null)
        });

        const articleExisted = this.data.articleResults.some(item => item.articleId === articleResult.articleId);

        if (this.data.articleResults.length < ARTICLE_RESULT_LIST_MAX_LENGTH && !articleExisted) {
            this.data.articleResults.push(articleResult);
            this.saveDataToStorage();
        }
    }

    recordAvailability(articleId, availabilities, isUpdateAvaiForArticleItemAndCartItem?) {
        if (!this.data) {
            return;
        }
        this.updateAvailability(this.getAvailability(articleId, availabilities), isUpdateAvaiForArticleItemAndCartItem);
    }

    recordShoppingCartItems(items: ShoppingBasketItemModel[], isAvaiReady = true) {
        if (!this.data) {
            return;
        }
        const cartItems = [];
        items.forEach(item => {
            if (item.articleItem) {
                cartItems.push(this.prepareCartItemModel(item, item.articleItem, isAvaiReady));
            }
            if (item.attachedCartItems) {
                item.attachedCartItems.forEach(attachedCartItem => {
                    if (attachedCartItem.articleItem) {
                        cartItems.push(this.prepareCartItemModel(item, attachedCartItem.articleItem));
                    }
                });
            }
        });
        this.data.cartItems = cartItems;
        this.saveDataToStorage();
    }

    clearArticleResults() {
        if (!this.data) {
            return;
        }
        this.data.articleResults = [];
    }

    resetModel() {
        this.data = new FeedbackTechicalData();
        this.storage.setFeedbackTechnicalData(this.userId, null);
    }

    recordArticleSearch(articleSearch: FeedbackArticleSearch) {
        if (!this.data) {
            return;
        }
        this.data.articleSearch = articleSearch;
        this.saveDataToStorage();
    }

    private recordFreeTextSearchKey(freeTextSearchKey: string) {
        if (!this.data) {
            return;
        }
        this.data.freeTextSearchKey = freeTextSearchKey;
        this.saveDataToStorage();
    }

    private getAvailability(articleId, availabilities: Array<any>): FeedbackArticleAvailability {
        let sortedAvailabilities = availabilities || [];
        if (sortedAvailabilities.length && sortedAvailabilities.length > 1) {
            sortedAvailabilities = AvailabilityUtil.sortAvailWithLatestTime(availabilities);
        }
        return new FeedbackArticleAvailability({
            articleId,
            avaiItems: sortedAvailabilities.map(item => this.extractAvai(item))
        });
    }

    private updateAvailability(availability: FeedbackArticleAvailability, isUpdateAvaiForArticleItemAndCartItem?) {
        if (this.data.articleResults && isUpdateAvaiForArticleItemAndCartItem) {
            this.data.articleResults.forEach(item => {
                if (item.articleId === availability.articleId) {
                    item.availability = availability;
                }
            });
        }
        if (this.data.cartItems) {
            this.data.cartItems.forEach(item => {
                if (item.articleId === availability.articleId) {
                    item.availability = availability;
                }
            });
        }
        this.saveDataToStorage();
    }

    private extractAvai(availability): FeedbackArticleAvailabilityItem {
        return new FeedbackArticleAvailabilityItem({
            quantity: availability.quantity,
            tourName: this.getAvaiTourName(availability),
            arrivalTime: this.getAvaiArrivalTime(availability)
        });
    }

    private getAvaiTourName(availability): string {
        switch (availability.sendMethodCode) {
            case AVAILABILITY_INFO[AVAILABILITY_INFO.PICKUP]:
                const ctx = this.appContextService.shoppingBasketContext;
                return ctx && ctx.pickupBranch && ctx.pickupBranch.branchName || '';
            case AVAILABILITY_INFO[AVAILABILITY_INFO.TOUR]:
                if (availability.availState !== Constant.AVAIL_STATE_INORDER_24_HOURS) {
                    return availability.tourName;
                }
                return '';
            default:
                return '';
        }
    }

    private getAvaiArrivalTime(availability): string {
        if (!(availability.sofort && availability.sendMethodCode === AVAILABILITY_INFO[AVAILABILITY_INFO.PICKUP])) {
            if (availability.availState === Constant.AVAIL_STATE_INORDER_24_HOURS) {
                return this.appCommonService.getDeliveryTextApplyAffiliateSetting();
            }
            return (availability.formattedCETArrivalDate && availability.formattedCETArrivalTime)
                ? (availability.formattedCETArrivalDate + Constant.SPACE + availability.formattedCETArrivalTime)
                : NOT_AVAILABLE;
        }
        if (availability.sofort && availability.sendMethodCode === AVAILABILITY_INFO[AVAILABILITY_INFO.PICKUP]) {
            return this.translateService.instant('ARTICLE.DELIVERY_IMMEDIATE');
        }
        return '';
    }

    private prepareCartItemModel(item, article, isAvaiReady = true): FeedbackShoppingCartItem {
        const cartItem = new FeedbackShoppingCartItem({
            vehicleId: item.vehicleId,
            vehicleDesciption: item.vehicleInfo,
            quantity: item.quantity,
            addedTime: DateUtil.formatDateInDateTime(item.addedTime),
            articleId: article.id_pim,
            articleNr: article.artnr,
            brand: article.supplier,

        });

        // if availability presents then we save it either
        if (isAvaiReady && article.availabilities) {
            const availability = article.availabilities;
            cartItem.availability = this.getAvailability(article.artnr, availability);
        }
        if (article.price && article.price.price) {
            const priceItem = article.price.price;
            if (priceItem.netPrice) {
                cartItem.netPrice = this.currencyPipe.transform(priceItem.netPrice);
            }
            if (priceItem.grossPrice) {
                cartItem.grossPrice = this.currencyPipe.transform(priceItem.grossPrice);
            }
        }
        return cartItem;
    }

    private saveDataToStorage() {
        this.data.userId = this.userId;
        setTimeout(() => {
            this.storage.setFeedbackTechnicalData(this.userId, this.data);
        });
    }
}
