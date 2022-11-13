import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/internal/Observable';
import uuid from 'uuid/v4';
import { timeout } from 'rxjs/internal/operators/timeout';
import { catchError } from 'rxjs/internal/operators/catchError';

import { AppStorageService } from '../services/app-storage.service';
import { environment } from 'src/environments/environment';
import { ErrorCodeEnum } from '../enums/error-code.enum';
import { X_SAG_REQUEST_ID_HEADER_NAME, Constant } from '../conts/app.constant';

import { AppCommonService } from '../services/app-common.service';
import { of } from 'rxjs';
import { ParamUtil } from '../utils/params.utils';
import { AppModalService } from '../services/app-modal.service';

export const affiliateSettingUrl = `/settings/${environment.affiliate}`;
export const baseUrl = `${environment.baseUrl}`;
export const apiCallTimeout = 130000; // 2 minutes and 10 seconds
export const EXCLUDE_URLS: string[] = [
    Constant.AUTH_TOKEN,
    affiliateSettingUrl,
    '/release?ver=',
    '/assets/',
    '/feedback/sales.+/create',
    'oci/export/order',
    '/analytics/settings',
    `/messages/common/`,
    'wss/opening-days/import',
    'wss/margin-by-article-group/import',
    'wss/margin-by-brand/import'
];

@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private appStorage: AppStorageService,
        private commonService: AppCommonService,
        private appModal: AppModalService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const ignoreOfflineError = ParamUtil.toBoolean(req.headers.get('ignoreofflineerror'));
        if (req.headers.has('ignoreofflineerror')) {
            const clonedHeaders = req.headers.delete('ignoreofflineerror');
            req = req.clone({ headers: clonedHeaders });
        }

        const token = req.headers.get('token');
        if (req.headers.has('token')) {
            const clonedHeaders = req.headers.delete('token');
            req = req.clone({ headers: clonedHeaders });
        }

        if (this.isExcludedUrl(req.url)) {
            return next.handle(req)
                .pipe(
                    timeout(apiCallTimeout),
                    catchError(err => this.errorHandler(err, req.url, ignoreOfflineError))
                );
        }
        const appLangCode = this.appStorage.appLangCode;
        const appToken = this.appStorage.appToken || token;
        const headers: any = {
            Accept: 'application/json',
            'X-Client-Version': this.appStorage.appVersion,
            'Content-Type': 'application/json',
            'Access-Control-Max-Age': '3600',
            'Accept-Language': appLangCode || 'de'
        };

        if (appToken) {
            Object.assign(headers, {
                Authorization: `Bearer ${appToken}`,
                [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
            });
        }
        // need search by sale token
        // customers/search
        const saleToken = this.appStorage.saleToken;
        if (req.url.indexOf('/customers/search') !== -1 && saleToken) {
            Object.assign(headers, {
                Authorization: `Bearer ${saleToken}`
            });
        }

        const cloneReq = req.clone({ setHeaders: headers });
        return next.handle(cloneReq).pipe(
            timeout(apiCallTimeout),
            catchError(err => this.errorHandler(err, req.url, ignoreOfflineError))
        );
    }

    private isExcludedUrl(url: string, excludeList = EXCLUDE_URLS): boolean {
        return url && !!excludeList.find(path => url.indexOf(path) !== -1 || new RegExp(path).test(url)) || !this.isConnectRestApi(url);
    }

    private isConnectRestApi(url: string) {
        return url && url.startsWith(baseUrl);
    }

    private errorHandler(errorRes, url?: string, ignoreOfflineError = false) {
        if (!navigator.onLine && !ignoreOfflineError) {
            this.appModal.closeAll();
            this.offlineHandler();
            throw errorRes;
        }

        const errCode = errorRes && errorRes.error && errorRes.error.error_code;
        if (errCode === ErrorCodeEnum.INVALID_VERSION_ERROR_CODE) {
            this.appModal.closeAll();
            this.newVersionHandler();
            return of(null);
        }
        switch (errorRes.status) {
            case ErrorCodeEnum.UNAUTHORIZED:
                this.appModal.closeAll();
                if (this.isExcludedUrl(url, [Constant.AUTH_TOKEN])) {
                    break;
                }
                this.handleAuthError();
                break;
            case ErrorCodeEnum.NOT_FOUND:
            case ErrorCodeEnum.SERVER_ERROR:
            case ErrorCodeEnum.FORBIDDEN:
            case ErrorCodeEnum.BAD_REQUEST:
                break;
            default:
                this.appModal.closeAll();
                break;
        }

        throw errorRes;
    }

    private handleAuthError() {
        this.commonService.authError();
    }

    private newVersionHandler() {
        this.commonService.invalidVersionMessage();
    }

    private offlineHandler() {
        this.commonService.offlineMessage();
    }
}
