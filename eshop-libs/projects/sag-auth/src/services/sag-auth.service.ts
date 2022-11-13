import { PotentialCustomerModel } from './../models/potenial-customer.model';
import { Injectable } from '@angular/core';
import { SagAuthConfigService } from './sag-auth.config';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/internal/operators/catchError';
import {
    SAG_AUTH_INVALID_VERSION_ERROR_CODE,
    SAG_AUTH_INVALID_TOKEN_ERROR_CODE,
    SAG_AUTH_API_TIMEOUT,
    SAG_AUTH_GENERAL_ERROR,
    X_SAG_REQUEST_ID_HEADER_NAME
} from '../constants/sag-auth';
import { throwError } from 'rxjs/internal/observable/throwError';
import { Observable } from 'rxjs/internal/Observable';
import uuid from 'uuid/v4';
import { AuthModel } from '../models/auth.model';
import { TranslateService } from '@ngx-translate/core';
import { SagAuthStorageService } from './sag-auth-storage.service';
import * as jwt_decode_ from 'jwt-decode';
const jwtDecode = jwt_decode_;
@Injectable()
export class SagAuthService {
    private baseUrl: string;
    private tokenUrl: string;
    private isFinalUser: boolean;
    private timeout: number;
    private langCode: string;
    constructor(
        private config: SagAuthConfigService,
        private http: HttpClient,
        private translate: TranslateService,
        private authStorage: SagAuthStorageService
    ) {
        this.baseUrl = config.baseUrl;
        this.isFinalUser = config.isFinalUser;
        this.timeout = config.timeout || 130000;
        this.langCode = translate.currentLang || 'de';
        this.tokenUrl = config.tokenUrl;
    }

    signIn(data: AuthModel): Observable<any> {
        const url = `${this.tokenUrl}oauth/token`;
        const headers = {
            'Accept-Language': this.langCode,
            Authorization: `Basic ZXNob3Atd2ViOmVzaG9wLXdlYi15enRBaEdwRlc=`,
            'Content-Type': 'application/x-www-form-urlencoded',
            [X_SAG_REQUEST_ID_HEADER_NAME]: uuid(),
        };
        const formData = data.formData;
        return this.http.post(url, formData, { headers, observe: 'body' });
    }

    resetPassword(affiliate: string, langCode: string, newPassword: string, code: string, hash: string) {
        const url = `${this.baseUrl}user/forgot-password/reset-password`;
        const body = {
            affiliate,
            langCode,
            password: newPassword,
            token: code,
            hashUsernameCode: hash,
            isFinalUser: this.isFinalUser
        };
        return this.http.post(url, body, { responseType: 'text' });
    }

    checkRegistrationInfo(values) {
        const url = `${this.baseUrl}customers/info`;
        return this.http.post(url, values);
    }

    register(values) {
        const url = `${this.baseUrl}customers/register`;
        return this.http.post(url, values);
    }

    sendCode(affiliate: any, username: any, langCode: any, redirectUrl: string) {
        const url = `${this.baseUrl}user/send-code`;
        const body = { username, affiliateId: affiliate, langCode, redirectUrl };
        return this.http.post(url, body, { responseType: 'text' }).pipe(
            catchError(error => this.handleError(error))
        );
    }

    checkCode(code: any, hash: string) {
        const url = `${this.baseUrl}user/forgot-password/code`;
        const body = { token: code, hashUsernameCode: hash };
        return this.http.post(url, body, { responseType: 'text' });
    }

    signOut() {
        const url = `${this.baseUrl}context/cache/clear`;
        return this.http.delete(url);
    }

    isTokenExpired(token?: string): boolean {
        if (!token) {
            token = this.authStorage.token;
        }
        if (!token) {
            return true;
        }
        const date = this.getTokenExpirationDate(token);
        if (!date) {
            return false;
        }
        return !(date.valueOf() > new Date().valueOf());
    }

    registerPotentialCustomer(body: PotentialCustomerModel) {
        let url = `${this.baseUrl}customers/register/potential-customer`;
        let headers = {
            'Content-Type': 'application/json'
        };
        return this.http.post(url, body, { headers, responseType: 'text' });
    }

    private getTokenExpirationDate(token: string): Date {
        const decoded = jwtDecode(token);

        if (!decoded.exp) {
            return null;
        }
        const date = new Date(0);
        date.setUTCSeconds(decoded.exp);
        return date;
    }

    private handleError(error: any) {
        let info: any;
        if (typeof error.error === 'string') {
            try {
                info = JSON.parse(error.error);
            } catch (e) { }
        }
        if (info) {
            // Checking invalid version
            if (info.error_code === SAG_AUTH_INVALID_VERSION_ERROR_CODE) {
                // this.hasInvalidReleaseVersion.next(true);
                return throwError(error);
            } else {
                // this.hasInvalidReleaseVersion.next(false);
            }
            // Checking invalid token
            if (info.error === SAG_AUTH_INVALID_TOKEN_ERROR_CODE) {
                // this.hasInvalidToken.next(true);
                return throwError(error);
            } else {
                // this.hasInvalidToken.next(false);
            }
        } else if (error.message === SAG_AUTH_API_TIMEOUT) {
            info = error;
        } else {
            info = SAG_AUTH_GENERAL_ERROR;
        }
        return throwError(info);
    }
}
