import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import uuid from 'uuid/v4';

import { OpeningDayRequestModel, SearchCriteriaModel } from '../models/opening-day.model';
import { CountryModel } from '../models/country.model';
import { AffiliateModel } from '../models/affiliate.model';
import { WorkingDayCodeModel } from '../models/working-day-code.model';
import { BranchModel } from '../models/branch.model';
import {
    CREATE_OPENING_DAY,
    UPDATE_OPENING_DAY,
    GET_OPENING_DAY_LIST,
    GET_OPENING_DAY_ITEM,
    REMOVE_OPENING_ITEM,
    GET_ALL_COUNTRIES,
    GET_ALL_AFFILIATES,
    GET_WORKING_DAY_CODE,
    GET_BRANCHES,
    UPLOAD_FILE
} from './constant';
import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { WSS_COUNTRY_SHORT_CODE } from '../enums/opening-day-calendar.enum';


@Injectable()
export class OpeningDayCalendarService {
    private readonly AFFILIATES_COUNTRY = this.getCountryShortCodeBaseOnAffiliate();
    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private http: HttpClient,
        private translateService: TranslateService,
        private appStorage: AppStorageService,) { }

    createOpeningDay(data: OpeningDayRequestModel) {
        const url = `${this.BASE_URL}${CREATE_OPENING_DAY}`;
        return this.http.post(url, data);
    }

    updateOpeningDay(data: OpeningDayRequestModel) {
        const url = `${this.BASE_URL}${UPDATE_OPENING_DAY}`;
        return this.http.put(url, data);
    }

    getOpeningDayList(criteria: SearchCriteriaModel, pagenation?): Observable<any> {
        let url = pagenation ? `${GET_OPENING_DAY_LIST}?page=${pagenation.page}&size=${pagenation.size}` : GET_OPENING_DAY_LIST;
        url = `${this.BASE_URL}${url}`;
        return this.http.post(url, criteria);
    }

    getOpeningDay(id: string): Observable<any> {
        const url = `${this.BASE_URL}${GET_OPENING_DAY_ITEM + id}`;
        return this.http.get(url);
    }

    removeOpeningDay(id: number): Observable<any> {
        const url = `${this.BASE_URL}${REMOVE_OPENING_ITEM + id}`;
        return this.http.delete(url);
    }

    getAllCountries(): Observable<any> {
        const url = `${this.BASE_URL}${GET_ALL_COUNTRIES}${this.AFFILIATES_COUNTRY}`;
        return this.http.get(url).pipe(
            map((countries: CountryModel[]) => {
                return countries.map(country => {
                    return {
                        value: country.id.toString(),
                        label: this.translateService.instant(`OPENING_DAY.OPENING_DAY_FORM.LAND_SHORT_CODE.${country.shortCode ? country.shortCode.toUpperCase() : ''}`)
                    };
                });
            })
        );
    }

    getWorkingDayCode() {
        const url = `${this.BASE_URL}${GET_WORKING_DAY_CODE}`;
        return this.http.get(url).pipe(
            map((codes: WorkingDayCodeModel[]) => {
                return codes.map(code => {
                    return {
                        value: code.id.toString(),
                        label: this.translateService.instant(`OPENING_DAY.${code.code}`),
                        originalCode: code.code
                    };
                });
            })
        );
    }

    getBranches() {
        const url = `${this.BASE_URL}${GET_BRANCHES}`;
        return this.http.get(url).pipe(
            map((branches: BranchModel[]) => {
                return (branches || []).map(branch => {                    
                    return {
                        value: branch.branchNr.toString(),
                        label: `${branch.branchNr} - ${branch.branchCode}`,
                        branch: branch.branchNr.toString(),
                        code: branch.branchCode,
                        id: branch.id
                    };
                });
            }),
            catchError(() => {
                return of([]);
            })
        );
    }

    uploadOpeningDayFile(file: File, isOverridden?: boolean) {
        let url = isOverridden ? UPLOAD_FILE : UPLOAD_FILE + '?checkExisting=true';
        url = `${this.BASE_URL}${url}`;
        const data = new FormData();
        data.set('file', file, file.name);

        const appToken = this.appStorage.appToken;
        const appLangCode = this.appStorage.appLangCode;
        const headers: any = {
            Accept: 'application/json',
            'X-Client-Version': this.appStorage.appVersion,
            'Access-Control-Max-Age': '3600',
            'Accept-Language': appLangCode || 'de'
        };
        if (appToken) {
            Object.assign(headers, {
                Authorization: `Bearer ${appToken}`,
                [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
            });
        }
        const options = { headers };

        return this.http.post(url, data, options);
    }

    getCountryShortCodeBaseOnAffiliate() {
        if (AffiliateUtil.isAffiliateCH(environment.affiliate)) {
            return WSS_COUNTRY_SHORT_CODE.CH;
        }
        if (AffiliateUtil.isAffiliateAT(environment.affiliate)) {
            return WSS_COUNTRY_SHORT_CODE.AT;
        }
        if (AffiliateUtil.isAffiliateCZ(environment.affiliate) || AffiliateUtil.isAxCz(environment.affiliate)) {
            return WSS_COUNTRY_SHORT_CODE.CZ;
        }
        return WSS_COUNTRY_SHORT_CODE.AT;
    }
}
