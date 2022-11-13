import {
    HttpInterceptor,
    HttpRequest,
    HttpHandler,
    HttpEvent
} from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { X_SAG_REQUEST_ID_HEADER_NAME } from '../conts/app.constant';
import uuid from 'uuid/v4';
import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { timeout } from 'rxjs/internal/operators/timeout';
import { catchError } from 'rxjs/internal/operators/catchError';
import { AppStorageService } from '../services/custom-local-storage.service';


export const affiliateSettingUrl = `/settings/${environment.affiliate}`;
export const apiCallTimeout = 130000; // 2 minutes and 10 seconds
export const EXCLUDE_URLS: string[] = [
    '/oauth/token',
    affiliateSettingUrl,
    '/release?ver=',
    '/assets/',
];
@Injectable({
    providedIn: 'root'
})
export class AuthInterceptor implements HttpInterceptor {
    constructor(
        private router: Router,
        private appStorage: AppStorageService
    ) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let cloneReq: any;
        if (this.isRequiredAuthUrl(req.url)) {
            const appLangCode = this.appStorage.appLangCode;
            const appToken = this.appStorage.appToken;
            const headers: any = {
                Accept: 'application/json',
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
            cloneReq = req.clone({ setHeaders: headers });
        }

        return next.handle(cloneReq || req).pipe(
            timeout(apiCallTimeout),
            catchError(
                err => {
                    if (err.status === 401) {
                        this.handleAuthError();
                    }
                    throw err;
                }
            )
        );
    }

    private isRequiredAuthUrl(url: string): boolean {
        return !EXCLUDE_URLS.find(path => url.indexOf(path) !== -1);
    }

    private handleAuthError() {
        this.appStorage.appToken = null;
        this.appStorage.autonet = null;
        this.router.navigateByUrl('/unauthorized');
    }
}
