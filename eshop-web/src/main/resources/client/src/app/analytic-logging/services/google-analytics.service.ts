import { Injectable, Injector } from '@angular/core';
import { Router } from '@angular/router';
import { get } from 'lodash';
import { AvailabilityUtil } from 'sag-article-detail';
import { AffiliateUtil, SagAvailDisplaySettingModel } from 'sag-common';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { GA_USER_TYPE } from '../enums/ga-search-category.enums';
import { GaProductField } from '../models/ga-product-field.model';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { AVAILABILITY_STATE } from '../const/availability-state.constant';
import { DELIVERY_TYPE, PAYMENT_INFO_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { PAYMENT_METHOD } from '../../core/enums/shopping-basket.enum';
import { ARTICLE_LIST_TYPE, SEARCH_MODE } from 'sag-article-list';
import { LIST_NAME_TYPE } from '../enums/event-type.enums';
import { GASignUpModel } from '../models/ga.model';
import { cloneDeep } from 'lodash'

declare var ConnectGoogleTagManager: any;

@Injectable({ providedIn: 'root' })
export class GoogleAnalyticsService {
    private routerInstance: Router;
    private currencyIsoCode: string;
    private gaListName: string = '';
    private bastketSourceItem = null;
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isSb = AffiliateUtil.isSb(environment.affiliate);
    isAffiliateApplyFgasAndDeposit = AffiliateUtil.isAffiliateApplyFgasAndDeposit(environment.affiliate);
    availDisplaySettings: SagAvailDisplaySettingModel[];

    constructor(
        private injector: Injector,
        private appStorage: AppStorageService
    ) { }

    get router() {
        if (!this.routerInstance) {
            this.routerInstance = this.injector.get(Router);
        }
        return this.routerInstance;
    }

    getGAListName() {
        return this.gaListName;
    }

    setGAListName(value) {
        const name = this.getListName(value);
        this.gaListName = name;
    }

   setBastketSourceItem(value) {
        this.bastketSourceItem = cloneDeep(value);
    }

    public initiateGoogleAnalytics(settings) {
        if (!settings || !settings.google_tag_manager_setting) {
            return;
        }
        try {
            const gtm = JSON.parse(settings.google_tag_manager_setting || '');
            const connect = (gtm && gtm.application || []).find(s => s.name === 'Connect');
            this.currencyIsoCode = settings && settings.currency;
            if (connect) {
                ConnectGoogleTagManager.setData(connect);
                ConnectGoogleTagManager.setCurrencyCode(this.currencyIsoCode);
                ConnectGoogleTagManager.initiate();
            }
        } catch (error) { }
    }

    public setUserData(loggedUser) {
        try {
            this.currencyIsoCode = loggedUser.customer && loggedUser.customer.currency;
            ConnectGoogleTagManager.setUserData({
                userType: loggedUser.salesUser || loggedUser.isSalesOnBeHalf ? GA_USER_TYPE.SALES : GA_USER_TYPE.CUSTOMER,
                customerNr: loggedUser.custNr,
                userId: loggedUser.id
            });
            ConnectGoogleTagManager.setCurrencyCode(this.currencyIsoCode);
        } catch (error) { }
    }

    public clear() {
        try {
            if (ConnectGoogleTagManager) {
                ConnectGoogleTagManager.clear();
            }
        } catch (error) { }
    }

    public sendPageView() {
        // set page to Google Analytics
        // GoogleAnalytics.sendPageView(this.router.url);
    }

    public search(category, searchTerm, source_id, source_desc) {
        try {
            ConnectGoogleTagManager.ec.search({
                category,
                searchTerm,
                source_id,
                source_desc
            });
        } catch (error) { }
    }

    public sendNoResultFound(searchTerm) {
        // GoogleAnalytics.sendQueryParameters(this.router.url, this.buildSearchParamIfPresent('no_result', searchTerm));
    }

    public login(user: UserDetail) {
        try {
            ConnectGoogleTagManager.ec.login({
                userType: user.salesUser || user.isSalesOnBeHalf ? GA_USER_TYPE.SALES : GA_USER_TYPE.CUSTOMER
            });
        } catch (error) { }
    }

    public addToWishlist(data: any, modalName = '') {
        try {
            if (data && data.addItem && data.articleItem) {
                const article = this.createProductFieldObject(data.articleItem, 0, true, modalName);
                ConnectGoogleTagManager.ec.addToWishlist({
                    summary: {
                        total: this.round2Digits(get(data, 'articleItem.price.price.totalNetPrice'))
                    },
                    article
                });
            }
        } catch (error) { }
    }

    removeFromBasket(miniBasket: any, cartKeys) {
        if (!miniBasket || !miniBasket.items || !miniBasket.items.length) {
            return;
        }
        const removedArts = miniBasket.items
            .map((basketItem, index) => ({ ...basketItem, index }))
            .filter(basketItem => cartKeys.indexOf(basketItem.cartKey) !== -1)
            .map(basketItem =>
            ({
                ...basketItem.articleItem,
                index: basketItem.index,
                basketItemSourceId: basketItem.basketItemSourceId,
                basketItemSourceDesc: basketItem.basketItemSourceDesc
            }));

        const googleAnalyticsArgs = this.prepareImpressionFieldObjectArgs(removedArts);
        const value = (googleAnalyticsArgs || []).reduce((acc, cur) => {
            return acc + cur.price * cur.quantity;
        }, 0)
        this.clearShoppingBasket(googleAnalyticsArgs, this.round2Digits(value));
    }

    public updateBasketProduct(googleAnalyticsArgs: any, modalName = '') {
        try {
            const value = this.round2Digits(googleAnalyticsArgs.quantity * get(googleAnalyticsArgs, 'product.price'));
            if (this.gaListName && googleAnalyticsArgs && googleAnalyticsArgs.product) {
                googleAnalyticsArgs.product.item_list_name = this.gaListName;
                googleAnalyticsArgs.product.item_sub_list_name = modalName;
            }
            ConnectGoogleTagManager.ec.updateBasketProduct(googleAnalyticsArgs, value);
        } catch (error) { }
    }

    public clearShoppingBasket(googleAnalyticsArgs, value) {
        try {
            ConnectGoogleTagManager.ec.removeProductFromBasket(googleAnalyticsArgs, value);
        } catch (error) { }
    }

    public viewProductList(articles = [], modalName = '') {
        try {
            articles.forEach(article => {
                article.item_list_name = this.gaListName;
                article.item_sub_list_name = modalName;
            });
            ConnectGoogleTagManager.ec.viewProductList(articles);
        } catch (error) { }
    }

    public viewProductDetails(article: any, modalName = '') {
        try {
            if (!article || !article.isShownSpecDetail) {
                return;
            }
            const googleAnalyticsArgs = this.createProductFieldObject(article, 0, true, modalName);
            const value = this.round2Digits(googleAnalyticsArgs.quantity * googleAnalyticsArgs.price);
            ConnectGoogleTagManager.ec.selectProduct(googleAnalyticsArgs);
            ConnectGoogleTagManager.ec.viewProductDetails(googleAnalyticsArgs, value);
        } catch (error) { }
    }

    public viewShoppingCart(data, isFastScan = false) {
        try {
            const articles = this.prepareImpressionFieldObjectArgs(data.articles, true);
            if (data && data.summary) {
                data.summary.subTotalExlVat = this.round2Digits(data.summary.subTotalExlVat);
            }
            if (this.gaListName && articles) {
                articles.forEach(item => {
                    item.item_list_name = isFastScan ? LIST_NAME_TYPE.ARTICLE_SEARCH_RESULTS : this.gaListName;
                });
            }
            ConnectGoogleTagManager.ec.viewShoppingCart({ ...data, articles });
        } catch (error) { }
    }

    public beginCheckout(data) {
        try {
            const articles = this.prepareImpressionFieldObjectArgs(data.articles);
            if (data && data.summary) {
                data.summary.subTotalExlVat = this.round2Digits(data.summary.subTotalExlVat);
            }
            ConnectGoogleTagManager.ec.beginCheckout({ ...data, articles });
        } catch (error) { }
    }

    public purchase(data: any) {
        try {
            if (data && data.summary) {
                data.summary.value = this.round2Digits(data.summary.value);
                data.summary.tax = this.round2Digits(data.summary.tax);
            }
            ConnectGoogleTagManager.ec.purchase(data);
        } catch (error) { }
    }


    public findGaNetPrice(price: any): number {
        if (!price || !price.price || !price.price.netPrice) {
            return 0;
        }
        return this.round2Digits(price.price.netPrice);
    }

    public addShippingInfo(data: any, shoppingBasketContext) {
        try {
            if (data && shoppingBasketContext && shoppingBasketContext.deliveryType) {
                const deliveryType = shoppingBasketContext.deliveryType.descCode || '';
                data.shipping_tier = this.isSb ? this.getShippingInfoWTSB(shoppingBasketContext) : this.getShippingInfo(deliveryType);
                data.value = this.round2Digits(data.value);
            }
            ConnectGoogleTagManager.ec.addShippingInfo(data);
        } catch (error) { }
    }

    public addPaymentInfo(data: any, shoppingBasketContext) {
        try {
            if (data && shoppingBasketContext && shoppingBasketContext.paymentMethod) {
                data.value = this.round2Digits(data.value);
                data.payment_type = this.getPaymentInfo(shoppingBasketContext.paymentMethod.descCode);
            }
            ConnectGoogleTagManager.ec.addPaymentInfo(data);
        } catch (error) { }
    }

    private findPartDesc(article: any) {
        if (!article || !article.genArtTxtEng || !article.genArtTxtEng.gatxtdech) {
            return 'Other';
        }
        return article.genArtTxtEng.gatxtdech;
    }

    public signUp(data: GASignUpModel) {
        try {
            ConnectGoogleTagManager.ec.signUp(data);
        } catch (error) { }
    }

    createProductFieldObject(article: any, index = 0, isAddListName = false, modalName?: string) {
        if (!article) {
            return null;
        }
        const availabilityState = this.getAvailabilityState(article);
        const availabilityPartner = this.getAvailabilityPartner(article);
        const basketItemSource = this.appStorage.basketItemSource;

        const result = {
            item_id: article.pimId || article.id_pim,
            item_article_nr: article.artnrDisplay || article.artnr_display,
            item_name: this.findPartDesc(article),
            item_brand: article.product_brand || article.productBrand,
            index: index || article.index || 0,
            currency: this.currencyIsoCode,
            price: this.findGaNetPrice(article.price),
            quantity: article.amountNumber || article.salesQuantity,
            item_category: article.icat || '',
            item_category2: article.icat2 || '',
            item_category3: article.icat3 || '',
            item_category4: article.icat4 || '',
            item_category5: article.icat5 || '',
            item_availability: availabilityState,
            item_availability_partner: availabilityPartner,
            source_id: this.bastketSourceItem && this.bastketSourceItem.basketItemSourceId || article.basketItemSourceId || basketItemSource.basketItemSourceId || '',
            source_description: this.bastketSourceItem && this.bastketSourceItem.basketItemSourceDesc || article.basketItemSourceDesc || basketItemSource.basketItemSourceDesc || '',
        } as GaProductField;
        if (isAddListName) {
            result.item_list_name = this.gaListName || '';
            result.item_sub_list_name = modalName || '';
        }
        return result;
    }

    public prepareProductFieldObjectArgs(changedArt: any, newQuantity: number) {
        const productFieldObject = this.createProductFieldObject(changedArt);
        if (!changedArt.oldQuantity) {
            productFieldObject.quantity = 0;
        }
        return {
            product: productFieldObject,
            quantity: newQuantity
        };
    }

    public prepareImpressionFieldObjectArgs(articleDocs: any, isAddListName = false) {
        if (!articleDocs) {
            return [];
        }

        const impressions = [];
        articleDocs.forEach((article, index) => {
            const impressionFieldObject = this.createProductFieldObject(article, index, isAddListName);
            impressions.push(impressionFieldObject);
        });

        return impressions;
    }

    private round2Digits(number) {
        if (!number) {
            return 0;
        }
        return Math.round(number * 100) / 100;
    }

    public getAvailabilityState(article) {
        const availabilities = article.availabilities;
        if (!availabilities || !availabilities.length) {
            return '';
        }
        if (this.isSb) {
            return '';
        }
        const allowedAddToShoppingCart = article.allowedAddToShoppingCart;
        if (this.isAffiliateApplyFgasAndDeposit && allowedAddToShoppingCart === false) {
            return AVAILABILITY_STATE.NOT_ORDERABLE;
        }
        const { isPartiallyAvail, hasDropShipmentFakeAvail } = this.determinePartiallyAvail(availabilities);
        if (isPartiallyAvail) {
            return AVAILABILITY_STATE.PARTIALLY_AVAILABLE;
        }
        const isArticle24h = this.isEhCz ? AvailabilityUtil.isArticle24hCz(availabilities) : AvailabilityUtil.isArticle24h(availabilities);
        if (isArticle24h || hasDropShipmentFakeAvail) {
            return AVAILABILITY_STATE.NOT_AVAILABLE;
        }
        const availability = availabilities[0];
        if (availability.sofort) {
            return AVAILABILITY_STATE.IMMEDIATE;
        }
        const isSameDay = AvailabilityUtil.isAvailSameDay(availability);
        const isInStockLabelShown = this.determineInStockForTour(availabilities, article.stock);
        // sameDay state + inStock label is shown => immediate
        if (isSameDay && isInStockLabelShown) {
            return AVAILABILITY_STATE.IMMEDIATE;
        }
        if (isSameDay) {
            return AVAILABILITY_STATE.SAME_DAY;
        }
        const isNextDay = AvailabilityUtil.isAvailNextDay(availability);
        if (isNextDay) {
            return AVAILABILITY_STATE.NEXT_DAY;
        }
        return '';
    }

    private getAvailabilityPartner(article) {
        const defaultValue = 'none';
        const availabilities = article.availabilities;
        if (!availabilities || !availabilities.length) {
            return defaultValue;
        }
        if (this.isSb) {
            return defaultValue;
        }
        const venAvail = availabilities.find(item => item.venExternalSource && item.externalSource);
        if (!venAvail) {
            return defaultValue;
        }
        return `${venAvail.externalVendorId}#${venAvail.externalVendorName}`;
    }

    private determineInStockForTour(availabilities, stock) {
        const availDisplaySettings = this.availDisplaySettings;
        const inStockText = AvailabilityUtil.initInStockNoteForTour(availDisplaySettings, availabilities, stock, environment.affiliate, this.appStorage.appLangCode);
        const isInStockLabelShown = !!inStockText;
        return isInStockLabelShown;
    }

    private determinePartiallyAvail(availabilities) {
        const hasInvalidAvail = availabilities.some(avail => avail.backOrder);
        const hasDropShipmentFakeAvail = AvailabilityUtil.isArticleCon(availabilities);
        const isPartiallyAvail = availabilities.length > 1 && (hasInvalidAvail || hasDropShipmentFakeAvail);
        return { isPartiallyAvail, hasDropShipmentFakeAvail };
    }

    private getShippingInfo(deliveryType: DELIVERY_TYPE | string) {
        switch (deliveryType) {
            case DELIVERY_TYPE.TOUR:
                return 'Tour';
            case DELIVERY_TYPE.COURIER:
                return 'Courier';
            case DELIVERY_TYPE.PICKUP:
                return 'Pickup';
            default:
                return '';
        }
    }

    private getPaymentInfo(paymentType: PAYMENT_METHOD) {
        switch (paymentType) {
            case PAYMENT_METHOD.CREDIT:
                return PAYMENT_INFO_TYPE.CREDIT;
            case PAYMENT_METHOD.WHOLESALE:
                return PAYMENT_INFO_TYPE.WHOLESALE;
            case PAYMENT_METHOD.CASH:
            case PAYMENT_METHOD.CARD:
                return PAYMENT_INFO_TYPE.CASH;
            case PAYMENT_METHOD.DIRECT_INVOICE:
                return PAYMENT_INFO_TYPE.DIRECT_INVOICE;
            case PAYMENT_METHOD.EUR_PAYMENT:
                return PAYMENT_INFO_TYPE.EUR_PAYMENT;
            case PAYMENT_METHOD.BANK_TRANSFER:
                return PAYMENT_INFO_TYPE.BANK_TRANSFER;
            default:
                return '';
        }
    }

    private getShippingInfoWTSB(shoppingBasketContext: ShoppingBasketContextModel) {
        const { eshopBasketContextByLocation } = shoppingBasketContext;
        if (!eshopBasketContextByLocation) {
            return '';
        }
        const deliveryTypes = eshopBasketContextByLocation.map(basket => basket && basket.deliveryType ? basket.deliveryType.descCode : '');
        const uniqueTypes = [...new Set(deliveryTypes)];
        return uniqueTypes.map((item) => this.getShippingInfo(item)).join('|');
    }

    private getListName(type: string) {
        switch (type) {
            case ARTICLE_LIST_TYPE.IN_CONTEXT:
            case ARTICLE_LIST_TYPE.NOT_IN_CONTEXT:
            case SEARCH_MODE.ARTICLE_NUMBER:
            case SEARCH_MODE.FREE_TEXT:
            case SEARCH_MODE.ID_SAGSYS:
            case SEARCH_MODE.ONLY_ARTICLE_NUMBER_AND_SUPPLIER:
                return LIST_NAME_TYPE.ARTICLE_SEARCH_RESULTS;
            case ARTICLE_LIST_TYPE.MOTOR:
            case SEARCH_MODE.VEHICLE_CODE:
            case SEARCH_MODE.VEHICLE_DESC:
            case SEARCH_MODE.VIN_CODE:
            case SEARCH_MODE.MAKE_MODEL_TYPE:
                return LIST_NAME_TYPE.VEHICLE_SEARCH_RESULTS;
            case ARTICLE_LIST_TYPE.TYPRE:
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
            case SEARCH_MODE.TYRES_SEARCH:
                return LIST_NAME_TYPE.TYRE_SEARCH_RESULTS;
            case SEARCH_MODE.WSP_SEARCH:
            case ARTICLE_LIST_TYPE.WSP:
                return LIST_NAME_TYPE.UNIPARTS_CATALOG_RESULTS;
            case SEARCH_MODE.SHOPPING_LIST_RESULTS:
                return LIST_NAME_TYPE.SHOPPING_LIST_RESULTS;
            case ARTICLE_LIST_TYPE.BATTERY:
            case SEARCH_MODE.BATTERY_SEARCH:
                return LIST_NAME_TYPE.BATTERY_SEARCH_RESULTS;
            case ARTICLE_LIST_TYPE.BULB:
            case SEARCH_MODE.BULB_SEARCH:
                return LIST_NAME_TYPE.BULB_SEARCH_RESULTS;
            case ARTICLE_LIST_TYPE.OIL:
            case SEARCH_MODE.OIL_SEARCH:
                return LIST_NAME_TYPE.OIL_SEARCH_RESULTS;
            case ARTICLE_LIST_TYPE.SHOPPING_BASKET:
            case SEARCH_MODE.SHOPPING_BASKET:
                return LIST_NAME_TYPE.CART_ITEM;
            default:
                return '';
        }

    }
}
