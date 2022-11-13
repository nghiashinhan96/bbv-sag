import { Injectable } from '@angular/core';

import { TranslateService } from '@ngx-translate/core';
import uuid from 'uuid/v4';
import { HttpClient } from '@angular/common/http';

import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { AppStorageService } from './custom-local-storage.service';
import { ApiUtil } from '../utils/api.util';

@Injectable({
    providedIn: 'root',
})
export class UploadService {
    constructor(
        private http: HttpClient,
        private storage: AppStorageService,
        private translateService: TranslateService
    ) { }

    executeUpload(path: string, body: FormData) {
        const url = ApiUtil.getUrl(path);
        const headers = {
            Authorization: `Bearer ${this.storage.appToken}`,
            'Accept-Language': this.translateService.currentLang,
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
        };
        const options = { headers };
        return this.http.post(url, body, options);
    }
}
