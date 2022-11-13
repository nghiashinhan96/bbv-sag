import { Injectable, Injector } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Params, Router } from '@angular/router';

import uuid from 'uuid/v4';

import { environment } from 'src/environments/environment';
import { OciInfo } from '../models/oci-info.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { Constant, X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { OciConstant } from '../constants/oci.constants';
import { StringUtil } from 'src/app/dms/utils/string.util';

@Injectable({
    providedIn: 'root'
})
export class OciService {

    constructor(
        private http: HttpClient,
        private appStorage: AppStorageService,
        private injector: Injector
    ) { }

    getOciExportData(hookUrl: string) {
        const hookParam = hookUrl ? `?hook_url=${hookUrl}` : Constant.EMPTY_STRING;
        const url = `oci/export/order${hookParam}`;

        const appLangCode = this.appStorage.appLangCode;
        const appToken = this.appStorage.appToken;
        const headers: any = {
            Accept: 'text/html',
            'Content-Type': 'text/html',
            'Access-Control-Max-Age': '3600',
            'Accept-Language': appLangCode || 'de'
        };
        if (appToken) {
            Object.assign(headers, {
                Authorization: `Bearer ${appToken}`,
                [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
            });
        }
        return this.http.get(`${environment.baseUrl}${url}`, {
            headers,
            responseType: 'text'
        });
    }

    setOciInfo(ociInfo: OciInfo) {
        this.appStorage.ociInfo = ociInfo;
    }

    getOciInfo(): OciInfo {
        return this.appStorage.ociInfo;
    }

    getOciState(): boolean {
        const ociInfo: OciInfo = this.getOciInfo();
        return ociInfo ? ociInfo.isOciFlow : false;
    }

    preUrlHandle(params: Params) {
        this.appStorage.removeAll();

        const userName = params[OciConstant.PARAM_USERNAME];
        const password = params[OciConstant.PARAM_PASSWORD];
        const language = (params[OciConstant.PARAM_LANGUAGE] || '').toLocaleLowerCase();
        const hookUrl = !StringUtil.isBlank(params[OciConstant.PARAM_HOOK_URL])
            ? params[OciConstant.PARAM_HOOK_URL] : params[OciConstant.PARAM_HOOK_URL.toLocaleLowerCase()];

        const ociInfo = new OciInfo({
            userName,
            password,
            language,
            isOciFlow: true,
            target: OciConstant.DEFAULT_TARGET,
            action: hookUrl || OciConstant.DEFAULT_ACTION,
        });
        this.setOciInfo(ociInfo);

        const router = this.injector.get(Router);
        router.navigate([Constant.LOGIN_PAGE], {
            queryParams: {},
            queryParamsHandling: 'merge',
            replaceUrl: true
        });
    }
}
