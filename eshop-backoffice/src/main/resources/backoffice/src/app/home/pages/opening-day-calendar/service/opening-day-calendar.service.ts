import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';

import { OpeningDayRequestModel, SearchCriteriaModel } from '../model/opening-day.model';
import { CountryModel } from '../model/country.model';
import { AffiliateModel } from '../model/affiliate.model';
import { WorkingDayCodeModel } from '../model/working-day-code.model';
import { BranchModel } from '../model/branch.model';
import AffUtils from 'src/app/core/utils/aff-utils';
import { ApiUtil } from 'src/app/core/utils/api.util';
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
} from '../constant';
import { UploadService } from 'src/app/core/services/upload.service';



@Injectable()
export class OpeningDayCalendarService {
    private readonly CONTRY_CODE = AffUtils.getCountryCode();

    constructor(
        private http: HttpClient,
        private uploadService: UploadService,
        private translateService: TranslateService) { }

    createOpeningDay(data: OpeningDayRequestModel) {
        const url = ApiUtil.getUrl(CREATE_OPENING_DAY);
        return this.http.post(url, data);
    }

    updateOpeningDay(data: OpeningDayRequestModel) {
        const url = ApiUtil.getUrl(UPDATE_OPENING_DAY);
        return this.http.put(url, data);
    }

    getOpeningDayList(criteria: SearchCriteriaModel, pagenation?): Observable<any> {
        let url = pagenation ? `${GET_OPENING_DAY_LIST}?page=${pagenation.page}&size=${pagenation.size}` : GET_OPENING_DAY_LIST;
        url = ApiUtil.getUrl(url);
        return this.http.post(url, criteria);
    }

    getOpeningDay(id: number): Observable<any> {
        const url = ApiUtil.getUrl(GET_OPENING_DAY_ITEM + id);
        return this.http.get(url);
    }

    removeOpeningDay(id: number): Observable<any> {
        const url = ApiUtil.getUrl(REMOVE_OPENING_ITEM + id);
        return this.http.delete(url);
    }

    getAllCountries(): Observable<any> {
        const url = ApiUtil.getUrl(`${GET_ALL_COUNTRIES}${this.CONTRY_CODE}`);
        return this.http.get(url).pipe(
            map((countries: CountryModel[]) => {
                return (countries || []).map(country => {
                    return { value: country.id.toString(), label: country.fullName };
                });
            }),
            catchError((err) => of(null))
        );
    }

    getAllAffiliates() {
        const url = ApiUtil.getUrl(`${GET_ALL_AFFILIATES}${this.CONTRY_CODE}`);
        return this.http.get(url).pipe(
            map((affiliates: AffiliateModel[]) => {
                let id = 0;
                return (affiliates || []).map(affiliate => {
                    id++;
                    return { value: id.toString(), label: affiliate.name };
                });
            }),
            catchError((err) => of(null))
        );
    }

    getWorkingDayCode() {
        const url = ApiUtil.getUrl(GET_WORKING_DAY_CODE)
        return this.http.get(url).pipe(
            map((codes: WorkingDayCodeModel[]) => {
                return codes.map(code => {
                    return {
                        value: code.id.toString(),
                        label: this.translateService.instant(`OPENING_DAY.${code.code}`),
                        originalCode: code.code
                    };
                });
            }),
            catchError((err) => of(null))
        );
    }

    getBranches() {
        const url = ApiUtil.getUrl(`${GET_BRANCHES}${this.CONTRY_CODE}`);
        return this.http.get(url).pipe(
            map((branches: BranchModel[]) => {
                return (branches || []).map(branch => {
                    return {
                        value: branch.branchNr.toString(),
                        label: `${branch.branchNr} - ${branch.branchCode}`,
                        branch: branch.branchNr.toString(),
                        code: branch.branchCode
                    };
                });
            }),
            catchError((err) => of(null))
        );
    }

    uploadOpeningDayFile(file: File, isOverridden?: boolean) {
        const url = isOverridden ? UPLOAD_FILE : UPLOAD_FILE + '?checkExisting=true';
        const data = new FormData();
        data.set('file', file, file.name);
        return this.uploadService.executeUpload(url, data);
    }
}
