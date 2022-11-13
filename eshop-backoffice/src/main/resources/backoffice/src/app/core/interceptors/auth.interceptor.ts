import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent
} from '@angular/common/http';
import { Injectable } from '@angular/core';

import uuid from 'uuid/v4';
import { Observable } from 'rxjs/internal/Observable';
import { timeout, catchError } from 'rxjs/internal/operators';

import { AppStorageService } from '../services/custom-local-storage.service';
import { SpinnerService } from '../utils/spinner';
import { X_SAG_REQUEST_ID_HEADER_NAME } from '../conts/app.constant';
import { ErrorCodeEnum } from '../enums/error-code.enum';
import { AppCommonService } from '../services/app-common.service';

export const apiCallTimeout = 130000; // 2 minutes and 10 seconds
export const EXCLUDE_URLS: string[] = [
    '/oauth/token',
    '/release?ver=',
    '/assets/',
    '/admin/upload/',
    'admin/opening-days/import',
    'admin/external-vendor/import',
    'admin/delivery-profile/import'
];
export const AUTH_URL = '/oauth/token';
@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {

    constructor(
        private commonService: AppCommonService,
        private appStorage: AppStorageService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.isExcludedUrl(req.url)) {
            return next.handle(req).pipe(
                timeout(apiCallTimeout),
                catchError(
                    err => {
                        this.errorHandler(err, req.url);
                        throw err;
                    }
                )
            );
        }

        const appLangCode = this.appStorage.appLangCode;
        const appToken = this.appStorage.appToken || '';
        const headers: any = {
            Accept: 'application/json',
            'Content-Type': 'application/json',
            'Access-Control-Max-Age': '3600',
            'Accept-Language': appLangCode || 'de',
            Authorization: `Bearer ${appToken}`,
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
        };

        const cloneReq = req.clone({ setHeaders: headers });
        return next.handle(cloneReq).pipe(
            timeout(apiCallTimeout),
            catchError(
                err => {
                    this.errorHandler(err, req.url);
                    throw err;
                }
            )
        );
    }

    private isExcludedUrl(url: string): boolean {
        return url && !!EXCLUDE_URLS.find(path => url.indexOf(path) !== -1 || new RegExp(path).test(url));
    }

    private isAuthUrl(url: string): boolean {
        return url.indexOf(AUTH_URL) !== -1;
    }

    private errorHandler(errorRes, url?) {
        SpinnerService.stop();
        if (!navigator.onLine) {
            this.offlineHandler();
            return;
        }
        switch (errorRes.status) {
            case ErrorCodeEnum.UNAUTHORIZED:
                if (!this.isAuthUrl(url)) {
                    this.handleAuthError();
                }
                break;
            case ErrorCodeEnum.NOT_FOUND:
            case ErrorCodeEnum.SERVER_ERROR:
                break;
            default:
                const errCode = errorRes && errorRes.error && errorRes.error.error_code || ErrorCodeEnum.UNKNOWN_ERROR;
                switch (errCode) {
                    case ErrorCodeEnum.INVALID_VERSION_ERROR_CODE.toString():
                        this.newVersionHandler();
                        break;
                    default:
                        break;
                }
                break;
        }
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
