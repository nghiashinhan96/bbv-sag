import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TranslateService } from '@ngx-translate/core';
import uuid from 'uuid/v4';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { FeedbackSavingRequest } from '../models/feedback-saving-request.model';

@Injectable()
export class FeedbackService {
    baseUrl = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private translateService: TranslateService,
        private appStorage: AppStorageService
    ) { }

    getCustomerUserData() {
        return this.http.get(`${this.baseUrl}feedback/customer/user-data`);
    }

    getSaleOnBehalfUserData() {
        return this.http.get(`${this.baseUrl}feedback/sales-onbehalf/user-data`);
    }

    getSaleNotOnBehalfUserData() {
        return this.http.get(`${this.baseUrl}feedback/sales-not-onbehalf/user-data`);
    }

    createCustomerFeedback(model: FeedbackSavingRequest) {
        return this.http.post(`${this.baseUrl}feedback/customer/create`, model);
    }

    createSaleOnBehalfFeedback(model: FormData) {
        const headers = new HttpHeaders(this.getHeaders());

        return this.http.post(`${this.baseUrl}feedback/sales-onbehalf/create`, model, { headers });
    }

    createSaleNotOnBehalfFeedback(model: FormData) {
        const headers = new HttpHeaders(this.getHeaders());
        return this.http.post(`${this.baseUrl}feedback/sales-not-onbehalf/${environment.affiliate}/create`, model, { headers });
    }

    private getHeaders(): any {
        return {
            Accept: 'application/json',
            Authorization: `Bearer ${this.appStorage.appToken}`,
            'Access-Control-Max-Age': 3600,
            'Accept-Language': this.translateService.currentLang,
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
        };
    }
}
