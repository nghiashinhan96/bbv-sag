import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { CUSTOM_PRICE_SHOWN } from '../constants/custom-pricing.const';

@Injectable()
export class SagCustomPricingStorageService {

    constructor(private storage: LocalStorageService) { }

    get isCustomPriceShown(): boolean {
        return this.storage.retrieve(CUSTOM_PRICE_SHOWN);
    }

    set isCustomPriceShown(status: boolean) {
        this.storage.store(CUSTOM_PRICE_SHOWN, status);
    }

    get isCustomPriceShown$() {
        return this.storage.observe(CUSTOM_PRICE_SHOWN);
    }
}
