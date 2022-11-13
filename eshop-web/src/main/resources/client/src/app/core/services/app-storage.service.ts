import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import {
    APP_TOKEN,
    APP_SALE_TOKEN,
    APP_VERSION,
    APP_DEFAULT_SETTING,
    APP_VEHICLE_FILTERING,
    APP_CUSTOMERS,
    Constant,
    APP_SELECTED_VEHICLE,
    APP_REF_TEXT,
    APP_SALE_INFO,
    APP_ARTICLE_MODE,
    APP_FASTSCAN_ART,
    APP_SALE_USER_NAME,
    APP_ORDERED, APP_VAT_CONFIRM,
    APP_THU_LE_MESSAGE,
    APP_SHOP_CUSTOMER,
    SELECTED_FAVORITE_ITEM,
    SELECTED_FAVORITE_LEAF,
    APP_ANALYTIC_EVENT_BASKET_ITEM_SOURCE,
    APP_DIGI_INVOICE_HASH_CODE,
    APP_ADVANCE_VEHICLE_SEARCH_MAKE,
    APP_ADVANCE_VEHICLE_SEARCH_MODEL
} from '../conts/app.constant';
import { TranslateService } from '@ngx-translate/core';

import { CustomerModel } from '../models/customer.model';
import { BehaviorSubject } from 'rxjs';
import { APP_SUB_ORDER_BASKET } from '../conts/app.constant';
import { SubOrderBasketModel } from '../models/sub-order-basket.model';
import { OfferDetail } from 'src/app/offers/models/offer-detail.model';
import { OciConstant } from 'src/app/oci/constants/oci.constants';
import { LIB_USER_SETTING } from 'sag-article-list';
import { SAG_COMMON_LANG_CODE, SAG_COMMON_LOCALE, SAG_COMMON_CUSTOMER_CURRENCY } from 'sag-common';
import { environment } from 'src/environments/environment';
import { WspTreeModel } from 'src/app/wsp/models/wsp-tree.model';
@Injectable({
    providedIn: 'root'
})
export class AppStorageService {

    private customersSub$;
    uniTrees: WspTreeModel[];

    constructor(
        private storage: LocalStorageService,
        private languageService: TranslateService
    ) { }

    set appLangCode(code) {
        this.storage.store(SAG_COMMON_LANG_CODE, code);
        this.languageService.use(code);
    }

    get appLangCode() {
        return this.storage.retrieve(SAG_COMMON_LANG_CODE);
    }

    set appToken(token) {
        this.storage.store(APP_TOKEN, token);
    }

    get appToken() {
        return this.storage.retrieve(APP_TOKEN);
    }

    set saleToken(token) {
        this.storage.store(APP_SALE_TOKEN, token);
    }

    get saleToken() {
        return this.storage.retrieve(APP_SALE_TOKEN);
    }

    set appVersion(ver: string) {
        this.storage.store(APP_VERSION, ver);
    }

    get appVersion() {
        return this.storage.retrieve(APP_VERSION);
    }
    // for the legacy app storage
    get appOldVersion() {
        const ver2Key = 'connect8.app.version';
        const ver2 = this.storage.retrieve(ver2Key);
        localStorage.removeItem(ver2Key);
        const ver3Key = `connect8.app.${environment.affiliate}.version`;
        const ver3 = localStorage.getItem(ver3Key);
        localStorage.removeItem(ver3Key);
        return ver2 || ver3;
    }

    set defaultSetting(setting) {
        this.storage.store(APP_DEFAULT_SETTING, setting);
    }

    get defaultSetting() {
        return this.storage.retrieve(APP_DEFAULT_SETTING);
    }

    set articleMode(mode: string) {
        this.storage.store(APP_ARTICLE_MODE, mode);
    }

    get articleMode() {
        return this.storage.retrieve(APP_ARTICLE_MODE);
    }

    set vehicleFilter(filter: any) {
        this.storage.store(APP_VEHICLE_FILTERING, filter);
    }

    get vehicleFilter() {
        return this.storage.retrieve(APP_VEHICLE_FILTERING);
    }

    set saleUserName(userName: string) {
        this.storage.store(APP_SALE_USER_NAME, userName);
    }

    get saleUserName() {
        return this.storage.retrieve(APP_SALE_USER_NAME);
    }

    observeCustomers() {
        if (!this.customersSub$) {
            this.customersSub$ = new BehaviorSubject<any>(this.customers);
        }
        return this.customersSub$.asObservable();
    }

    resetOpenInfoBox(customerId?: string, state = false) {
        if(!!customerId) {
            (this.customers || []).forEach(cus => {
                if(cus.nr.toString() === customerId) {
                    cus.isOpenInfoBox = state;
                } else {
                    cus.isOpenInfoBox = false;
                }
            });
        } else {
            (this.customers || []).forEach(cus => cus.isOpenInfoBox = false);
        }

        this.customersSub$.next(this.customers);
    }

    openInfoBox(customer: CustomerModel) {
        customer.isOpenInfoBox = true;
        return customer;
    }

    addCustomer(customer: CustomerModel) {
        this.resetOpenInfoBox();
        const currentCustomers = [...this.customers];
        customer = this.openInfoBox(customer);
        currentCustomers.push(customer);
        this.customersSub$.next(currentCustomers);
        this.customers = [...currentCustomers];
        return this.customers;
    }

    removeCustomer(customerNr) {
        const filtered = this.customers.filter(c => c.nr !== customerNr);
        this.customersSub$.next(filtered);
        this.customers = [...filtered];
        return this.customers;
    }

    isExistedCustomer(customerNr) {
        const filtered = this.customers.filter(c => c.nr === (customerNr || '').toString());
        return filtered.length > 0;
    }

    set customers(customers: CustomerModel[]) {
        this.storage.store(APP_CUSTOMERS, customers);
    }

    get customers() {
        return this.storage.retrieve(APP_CUSTOMERS) || [];
    }

    set currencyLocale(locale: string) {
        this.storage.store(SAG_COMMON_LOCALE, locale);
    }

    get currencyLocale() {
        return this.storage.retrieve(SAG_COMMON_LOCALE);
    }

    get tyreSearchHistoryMode() {
        return this.storage.retrieve(Constant.TYRE_SEARCH_HISTORY_MODE) || {};
    }

    set tyreSearchHistoryMode({ key, value }) {
        const mode = this.tyreSearchHistoryMode;
        mode[key] = value;
        this.storage.store(Constant.TYRE_SEARCH_HISTORY_MODE, mode);
    }

    get filterHistory() {
        return this.storage.retrieve(Constant.TYRE_SEARCH_HISTORY) || {};
    }

    set filterHistory({ key, value }) {
        let data = this.filterHistory;
        if (typeof data !== 'object') {
            data = {};
        }
        data[key] = value;
        this.storage.store(Constant.TYRE_SEARCH_HISTORY, data);
    }

    get batteryFilterHistory() {
        const value = this.storage.retrieve(Constant.BATTERY_SEARCH_HISTORY);
        return value ? { ...value } : null;
    }

    set batteryFilterHistory(value) {
        this.storage.store(Constant.BATTERY_SEARCH_HISTORY, value);
    }

    get oilFilterHistory() {
        const value = this.storage.retrieve(Constant.OIL_SEARCH_HISTORY);
        return value ? { ...value } : null;
    }

    set oilFilterHistory(value) {
        this.storage.store(Constant.OIL_SEARCH_HISTORY, value);
    }

    get bulbFilterHistory() {
        const value = this.storage.retrieve(Constant.BULBS_SEARCH_HISTORY);
        return value ? { ...value } : null;
    }

    set bulbFilterHistory(value) {
        this.storage.store(Constant.BULBS_SEARCH_HISTORY, value);
    }

    clearFilterHistory(key) {
        const data = this.filterHistory;
        delete data[key];
        this.storage.store(Constant.TYRE_SEARCH_HISTORY, data);
    }

    get userPrice() {
        return this.storage.retrieve(LIB_USER_SETTING);
    }

    set userPrice(settings) {
        this.storage.store(LIB_USER_SETTING, settings);
    }

    set selectedVehicle(vehicle: any) {
        this.storage.store(APP_SELECTED_VEHICLE, vehicle);
    }

    get selectedVehicle() {
        return this.storage.retrieve(APP_SELECTED_VEHICLE) || null;
    }

    get selectedOffer(): OfferDetail {
        return this.storage.retrieve(Constant.SELECTED_OFFER) || null;
    }

    set selectedOffer(value: OfferDetail) {
        this.storage.store(Constant.SELECTED_OFFER, value);
    }

    observeGoodReceiver() {
        return this.storage.observe(Constant.GOOD_RECEIVER);
    }

    get goodReceiver() {
        return this.storage.retrieve(Constant.GOOD_RECEIVER);
    }

    set goodReceiver(value) {
        this.storage.store(Constant.GOOD_RECEIVER, value);
    }

    get refText() {
        return this.storage.retrieve(APP_REF_TEXT) || {};
    }

    set refText(data) {
        const previousData = this.refText;
        this.storage.store(APP_REF_TEXT, Object.assign({}, previousData, data));
    }

    get jsonEventList() {
        return this.storage.retrieve(Constant.JSON_EVENT_LIST) || {};
    }

    set jsonEventList(value) {
        this.storage.store(Constant.JSON_EVENT_LIST, value);
    }

    get hiddenSystemMessages() {
        return this.storage.retrieve(Constant.SECTION_HIDDEN_MESSAGES_KEY);
    }

    set hiddenSystemMessages(value) {
        let messages;
        if (Array.isArray(value)) {
            messages = value;
        } else {
            messages = this.hiddenSystemMessages || [];
            messages.push(value);
        }
        this.storage.store(Constant.SECTION_HIDDEN_MESSAGES_KEY, messages);
    }

    observeRefText() {
        return this.storage.observe(APP_REF_TEXT);
    }

    cleanRefText(keyStore: any) {
        const refTextObj = this.refText;
        delete refTextObj[keyStore];
        this.refText = refTextObj;
    }

    removeAll() {
        const jsonEventList = this.jsonEventList;
        const preservedCurrencyLocale = this.currencyLocale;
        const preservedAppLangCode = this.appLangCode;
        const preservedAffSetting = this.defaultSetting;
        const appVersion = this.appVersion;
        const currentStateSingleSelectMode = this.currentStateSingleSelectMode;

        const prefix = `connect8.${environment.affiliate}.`;
        Object.keys(localStorage).forEach(k => {
            if (k.indexOf(prefix) !== -1) {
                const key = k.replace(prefix, '');
                this.storage.clear(key);
            }
        });

        this.currencyLocale = preservedCurrencyLocale;
        this.appLangCode = preservedAppLangCode;
        this.appVersion = appVersion;
        this.jsonEventList = jsonEventList;
        this.defaultSetting = preservedAffSetting;
        this.currentStateSingleSelectMode = currentStateSingleSelectMode;
        this.uniTrees = null;
        // in logout case
        if (this.customersSub$) {
            this.customersSub$.next([]);
        }
    }

    set saleInfo(data) {
        this.storage.store(APP_SALE_INFO, data);
    }

    get saleInfo() {
        return this.storage.retrieve(APP_SALE_INFO) || {};
    }

    // for temp data if switch from fastscan
    get fastScanArt$() {
        return this.storage.observe(APP_FASTSCAN_ART);
    }
    set fastscanArt(art) {
        this.storage.store(APP_FASTSCAN_ART, art);
    }

    get fastscanArt() {
        return this.storage.retrieve(APP_FASTSCAN_ART);
    }

    get subOrderBasket(): SubOrderBasketModel {
        return this.storage.retrieve(APP_SUB_ORDER_BASKET) || {};
    }

    set subOrderBasket(data: SubOrderBasketModel) {
        this.storage.store(APP_SUB_ORDER_BASKET, data);
    }

    // USER: forgot password
    set hashUsernameCode(value) {
        this.storage.store(Constant.KEY_RESETPASSWORD_HASH_USERNAME_CODE, value);
    }

    get hashUsernameCode() {
        return this.storage.retrieve(Constant.KEY_RESETPASSWORD_HASH_USERNAME_CODE);
    }

    set secureCode(value) {
        this.storage.store(Constant.KEY_SECURE_CODE, value);
    }

    get secureCode() {
        return this.storage.retrieve(Constant.KEY_SECURE_CODE);
    }

    clearHashUsernameCode() {
        this.storage.clear(Constant.KEY_RESETPASSWORD_HASH_USERNAME_CODE);
    }

    clearSecureCode() {
        this.storage.clear(Constant.KEY_SECURE_CODE);
    }

    // Default URL
    set defaultUrl(value: string) {
        this.storage.store(Constant.DEFAULT_URL_KEY, value);
    }

    get defaultUrl() {
        return this.storage.retrieve(Constant.DEFAULT_URL_KEY);
    }

    get ordered$() {
        return this.storage.observe(APP_ORDERED);
    }

    get ordered() {
        return this.storage.retrieve(APP_ORDERED) || [];
    }

    set ordered(ordereds) {
        this.storage.store(APP_ORDERED, ordereds);
    }

    getFeedbackTechnicalData(userId) {
        const key = `${Constant.FEEDBACK_TECHNICAL_DATA_KEY}_${userId}`;
        return this.storage.retrieve(key);
    }

    setFeedbackTechnicalData(userId, value) {
        const key = `${Constant.FEEDBACK_TECHNICAL_DATA_KEY}_${userId}`;
        this.storage.store(key, value);
    }

    // Classic category
    set classicViewMode(value: boolean) {
        this.storage.store(Constant.CLASSIC_CATE_VIEW_KEY, value);
    }

    get classicViewMode() {
        return this.storage.retrieve(Constant.CLASSIC_CATE_VIEW_KEY);
    }

    // Single select mode
    set currentStateSingleSelectMode(value: any) {
        this.storage.store(Constant.SINGLE_SELECT_MODE_KEY, value);
    }

    get currentStateSingleSelectMode() {
        return this.storage.retrieve(Constant.SINGLE_SELECT_MODE_KEY) || {};
    }

    setCurrentStateSingleSelectMode(userId: number, value: boolean) {
        if (!userId) {
            return;
        }
        const data = this.currentStateSingleSelectMode;
        data[userId] = value;
        this.currentStateSingleSelectMode = data;
    }

    getCurrentStateSingleSelectMode(userId: number) {
        if (!userId) {
            return;
        }
        const data = this.currentStateSingleSelectMode;
        return data[userId];
    }

    set ociInfo(ociInfo) {
        this.storage.store(OciConstant.OCI_INFO, ociInfo);
    }

    get ociInfo() {
        return this.storage.retrieve(OciConstant.OCI_INFO);
    }

    clearOciInfo() {
        this.storage.clear(OciConstant.OCI_INFO);
    }

    set shopType(value) {
        this.storage.store(Constant.SHOP_TYPE, value);
    }

    get currentStateVatConfirm() {
        return this.storage.retrieve(APP_VAT_CONFIRM) || false;
    }

    set currentStateVatConfirm(value) {
        this.storage.store(APP_VAT_CONFIRM, !!value);
    }

    get thuleMessage() {
        return this.storage.retrieve(APP_THU_LE_MESSAGE) || '';
    }

    set thuleMessage(message) {
        this.storage.store(APP_THU_LE_MESSAGE, message);
    }

    clearThuleMessage() {
        this.storage.clear(APP_THU_LE_MESSAGE);
    }

    set shopCustomer({ key, value }) {
        const data = this.shopCustomer;
        const newData = Object.assign({}, data, { [key]: value });
        this.storage.store(APP_SHOP_CUSTOMER, newData);
    }

    get shopCustomer() {
        return this.storage.retrieve(APP_SHOP_CUSTOMER) || {};
    }

    isShipCustomer(customerNumber: string) {
        const data = this.shopCustomer;
        return data[customerNumber];
    }

    set selectedFavoriteItem(item) {
        this.storage.store(SELECTED_FAVORITE_ITEM, item);
    }

    get selectedFavoriteItem() {
        return this.storage.retrieve(SELECTED_FAVORITE_ITEM);
    }

    set selectedFavoriteLeaf(item) {
        this.storage.store(SELECTED_FAVORITE_LEAF, item);
    }

    get selectedFavoriteLeaf() {
        return this.storage.retrieve(SELECTED_FAVORITE_LEAF);
    }

    set customerCurrency(currency: string) {
        this.storage.store(SAG_COMMON_CUSTOMER_CURRENCY, currency);
    }

    get customerCurrency() {
        return this.storage.retrieve(SAG_COMMON_CUSTOMER_CURRENCY);
    }

    set digiInvoiceHashCode(hashCode: string) {
        this.storage.store(APP_DIGI_INVOICE_HASH_CODE, hashCode);
    }

    get digiInvoiceHashCode() {
        return this.storage.retrieve(APP_DIGI_INVOICE_HASH_CODE);
    }

    clearDigiInvoiceHashCode() {
        this.storage.clear(APP_DIGI_INVOICE_HASH_CODE);
    }

    clearCustomerCurrency() {
        return this.storage.clear(SAG_COMMON_CUSTOMER_CURRENCY);
    }

    get basketItemSource() {
        return this.storage.retrieve(APP_ANALYTIC_EVENT_BASKET_ITEM_SOURCE) || {};
    }

    set basketItemSource(value) {
        this.storage.store(APP_ANALYTIC_EVENT_BASKET_ITEM_SOURCE, value);
    }

    clearBasketItemSource() {
        return this.storage.clear(APP_ANALYTIC_EVENT_BASKET_ITEM_SOURCE);
    }

    set advanceVehicleSearchMake(make: any) {
        this.storage.store(APP_ADVANCE_VEHICLE_SEARCH_MAKE, make);
    }

    get advanceVehicleSearchMake() {
        return this.storage.retrieve(APP_ADVANCE_VEHICLE_SEARCH_MAKE);
    }

    set advanceVehicleSearchModel(model: any) {
        this.storage.store(APP_ADVANCE_VEHICLE_SEARCH_MODEL, model);
    }

    get advanceVehicleSearchModel() {
        return this.storage.retrieve(APP_ADVANCE_VEHICLE_SEARCH_MODEL);
    }
}
