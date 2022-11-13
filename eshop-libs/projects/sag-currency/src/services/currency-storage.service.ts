import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { SAG_COMMON_LOCALE, SAG_COMMON_CUSTOMER_CURRENCY } from 'sag-common';

@Injectable()
export class SagCurrencyStorageService {

  constructor(private storage: LocalStorageService) { }

  set locale(locale: string) {
    this.storage.store(SAG_COMMON_LOCALE, locale);
  }

  get locale() {
    return this.storage.retrieve(SAG_COMMON_LOCALE);
  }

  get customerCurrency() {
    return this.storage.retrieve(SAG_COMMON_CUSTOMER_CURRENCY);
  }
}
