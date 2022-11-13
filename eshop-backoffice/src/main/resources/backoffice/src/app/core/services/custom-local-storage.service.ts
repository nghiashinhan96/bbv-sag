import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { TranslateService } from '@ngx-translate/core';

import { TOKEN_NAME, LANG_NAME, DEFAULT_LANG, APP_VERSION } from '../conts/app.constant';
@Injectable({
    providedIn: 'root',
})
export class AppStorageService {
    constructor(
        private storage: LocalStorageService,
        private translateService: TranslateService
    ) { }

    set appToken(token) {
        this.storage.store(TOKEN_NAME, token);
    }

    get appToken() {
        return this.storage.retrieve(TOKEN_NAME);
    }

    set appLangCode(code) {
        this.storage.store(LANG_NAME, code);
        this.translateService.use(code);
    }

    get appLangCode() {
        return this.storage.retrieve(LANG_NAME);
    }

    resetLocalStorage() {
        const lang = this.storage.retrieve(LANG_NAME) || DEFAULT_LANG;
        this.storage.clear();
        this.storage.store(LANG_NAME, lang);
    }

    removeAll() {
        const appVersion = this.appVersion;
        this.storage.clear();
        this.appVersion = appVersion;
    }

    set appVersion(ver: string) {
        this.storage.store(APP_VERSION, ver);
    }

    get appVersion() {
        return this.storage.retrieve(APP_VERSION);
    }
}
