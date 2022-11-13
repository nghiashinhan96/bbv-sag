import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { TranslateService } from '@ngx-translate/core';
import {
    SAG_AUTH_TOKEN,
    SAG_AUTH_SECURE_CODE,
    SAG_AUTH_HASH_CODE
} from '../constants/sag-auth';
import { SagAuthConfigService } from './sag-auth.config';
import { SAG_COMMON_LANG_CODE } from 'sag-common';

@Injectable()
export class SagAuthStorageService {

    constructor(
        private storage: LocalStorageService,
        private languageService: TranslateService,
        private config: SagAuthConfigService
    ) { }

    set langCode(code) {
        this.storage.store(SAG_COMMON_LANG_CODE, code);
        this.languageService.use(code);
    }

    get langCode() {
        return this.storage.retrieve(SAG_COMMON_LANG_CODE);
    }

    set token(token) {
        const tokenKey = this.config.tokenKey || SAG_AUTH_TOKEN;
        this.storage.store(tokenKey, token);
    }

    get token() {
        const tokenKey = this.config.tokenKey || SAG_AUTH_TOKEN;
        return this.storage.retrieve(tokenKey);
    }

    set hashUsernameCode(value) {
        this.storage.store(SAG_AUTH_HASH_CODE, value);
    }

    get hashUsernameCode() {
        return this.storage.retrieve(SAG_AUTH_HASH_CODE);
    }

    clearHashUsernameCode() {
        this.storage.clear(SAG_AUTH_HASH_CODE);
    }

    set secureCode(value) {
        this.storage.store(SAG_AUTH_SECURE_CODE, value);
    }

    get secureCode() {
        return this.storage.retrieve(SAG_AUTH_SECURE_CODE);
    }

    clearSecureCode() {
        this.storage.clear(SAG_AUTH_SECURE_CODE);
    }
}
