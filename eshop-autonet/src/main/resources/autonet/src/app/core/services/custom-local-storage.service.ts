import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import {
    APP_LANG_CODE,
    APP_TOKEN,
    APP_VERSION,
    APP_DEFAULT_SETTING,
    APP_AUTONET,
    APP_VEHICLE_FILTERING,
    APP_VEHICLE_ID,
    DEFAULT_LOCALE,
    APP_ARTICLE_MODE,
    APP_LAST_KEYWORD,
    APP_ADVANCE_VEHICLE_SEARCH_MAKE,
    APP_ADVANCE_VEHICLE_SEARCH_MODEL
} from '../conts/app.constant';
import { AutonetAuthModel } from '../models/autonet-auth.model';
import { LanguageEnum } from '../enums/language.enum';
import { LIB_IS_SIMPLE_MODE, LIB_USER_SETTING } from 'sag-article-list';
import { SAG_COMMON_LOCALE } from 'sag-common';

@Injectable({
    providedIn: 'root'
})
export class AppStorageService {
    private pAutonet: AutonetAuthModel;

    constructor(private storage: LocalStorageService) { }

    set appLangCode(code) {
        this.storage.store(APP_LANG_CODE, code);
    }

    get appLangCode() {
        return this.storage.retrieve(APP_LANG_CODE);
    }

    set appToken(token) {
        this.storage.store(APP_TOKEN, token);
    }

    get appToken() {
        return this.storage.retrieve(APP_TOKEN);
    }

    set appVersion(ver: string) {
        this.storage.store(APP_VERSION, ver);
    }

    get appVersion() {
        return this.storage.retrieve(APP_VERSION);
    }

    set defaultSetting(setting) {
        this.storage.store(APP_DEFAULT_SETTING, setting);
    }

    get defaultSetting() {
        return this.storage.retrieve(APP_DEFAULT_SETTING);
    }

    set isSimpleMode(status: boolean) {
        this.storage.store(LIB_IS_SIMPLE_MODE, status);
    }

    get isSimpleMode() {
        return this.storage.retrieve(LIB_IS_SIMPLE_MODE);
    }

    set currencyLocale(locale: string) {
        this.storage.store(SAG_COMMON_LOCALE, locale);
    }

    get country() {
        const locale = this.storage.retrieve(SAG_COMMON_LOCALE) || DEFAULT_LOCALE;
        return locale.split('_')[0];
    }

    get autonet() {
        return this.storage.retrieve(APP_AUTONET) as AutonetAuthModel;
    }

    set autonet(data: any) {
        this.pAutonet = new AutonetAuthModel(data);
        this.appToken = this.pAutonet.token;
        let langId = Number(this.pAutonet.lid);
        if (langId === LanguageEnum.bg || langId === LanguageEnum.sl) {
            langId = LanguageEnum.en;
            this.pAutonet.lid = langId;
        }
        this.appLangCode = LanguageEnum[langId];
        this.storage.store(APP_AUTONET, this.pAutonet);
    }

    set vehicleFilter(filter: any) {
        this.storage.store(APP_VEHICLE_FILTERING, filter);
    }

    get vehicleFilter() {
        return this.storage.retrieve(APP_VEHICLE_FILTERING);
    }

    set libUserSetting(filter: any) {
        this.storage.store(LIB_USER_SETTING, filter);
    }

    get libUserSetting() {
        return this.storage.retrieve(LIB_USER_SETTING);
    }

    set vehicleId(filter: any) {
        this.storage.store(APP_VEHICLE_ID, filter);
    }

    get vehicleId() {
        return this.storage.retrieve(APP_VEHICLE_ID);
    }

    set articleMode(mode: string) {
        this.storage.store(APP_ARTICLE_MODE, mode);
    }

    get articleMode() {
        return this.storage.retrieve(APP_ARTICLE_MODE);
    }

    set lastKeyword(keyword: string) {
        this.storage.store(APP_LAST_KEYWORD, keyword);
    }

    get lastKeyword() {
        return this.storage.retrieve(APP_LAST_KEYWORD);
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

    removeAll() {
        const prefix = `autonet.`;
        Object.keys(localStorage).forEach(k => {
            if (k.indexOf(prefix) !== -1) {
                const key = k.replace(prefix, '');
                this.storage.clear(key);
            }
        });
    }
}
