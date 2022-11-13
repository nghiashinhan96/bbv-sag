import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { APP_SUB_ORDER_BASKET, APP_VAT_CONFIRM } from '../consts/article-detail.const';

@Injectable()
export class SagArticleDetailStorageService {
    constructor(
        private storage: LocalStorageService,
    ) { }

    get subOrderBasket() {
        return this.storage.retrieve(APP_SUB_ORDER_BASKET) || {};
    }

    get currentStateVatConfirm() {
        return this.storage.retrieve(APP_VAT_CONFIRM) || false;
    }
}
