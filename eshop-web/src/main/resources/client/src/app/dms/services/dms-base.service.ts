import { DmsInfo } from '../models/dms-info.model';
import { Injectable } from '@angular/core';
import { DmsConstant } from '../constants/dms.constant';
import { LocalStorageService } from 'ngx-webstorage';
import { DmsError } from '../models/dms-error.model';
import { ExportOrder } from '../models/export-order.model';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { AppStorageService } from 'src/app/core/services/app-storage.service';

@Injectable({
    providedIn: 'root'
})
export class DmsBaseService {

    private baseUrl = environment.baseUrl;

    constructor(protected storage: LocalStorageService,
        protected http: HttpClient,
        protected appStorage: AppStorageService) { }

    updateDmsInfo(dmsInfo: DmsInfo) {
        this.storage.store(DmsConstant.DMS_INFO, dmsInfo);
    }

    getDmsInfo(): DmsInfo {
        return this.storage.retrieve(DmsConstant.DMS_INFO);
    }

    removeDmsInfo() {
        this.storage.clear(DmsConstant.DMS_INFO);
    }

    updateDmsError(dmsError: DmsError) {
        this.storage.store(DmsConstant.DMS_ERROR_KEY, dmsError);
    }

    getDmsError(): DmsError {
        return this.storage.retrieve(DmsConstant.DMS_ERROR_KEY);
    }

    removeDmsError() {
        this.storage.clear(DmsConstant.DMS_ERROR_KEY);
    }

    prepareExportData(data: ExportOrder) {
        const url = `${this.baseUrl}dms/export`;
        return this.http.post(url, data);
    }

    download() {
        const url = `${this.baseUrl}dms/download?access_token=${this.appStorage.appToken}`;
        this.appStorage.removeAll();
        window.location.replace(url);
        return;
    }

    addArticlesToShoppingCart(articleNrs: string[], articleQuantities: number[]) {
        const url = `${this.baseUrl}dms/cart/articles/add?articleNrs=${articleNrs}&articleQuantities=${articleQuantities}`;
        return this.http.post(url, null);
    }

    addOfferToShoppingCart(offerId: string) {
        const url = `${this.baseUrl}dms/cart/offer/${offerId}/add`;
        return this.http.post(url, null);
    }
}
