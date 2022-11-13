import { Injectable } from '@angular/core';
import { ArticleSearchConfigService } from './article-search-config.service';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { of } from 'rxjs';
import { VinPackage } from '../models/vin-package.model';
import { VehicleMake } from '../models/vehicle-make.model';
import { VinSecurityRequest } from '../models/vin-security-request.model';
import { GtMotiveEstimate } from '../models/gt-motive-estimate.model';
import * as moment_ from 'moment';
import { ArticleSearchHistory } from '../models/article-search-history.model';
import { VehicleSearchHistory } from '../models/vehicle-search-history.model';
const moment = moment_;
@Injectable()
export class ArticleSearchService {

    constructor(
        private config: ArticleSearchConfigService,
        private http: HttpClient
    ) { }

    searchArticleByNumber(articleNumber: string, requestBody: any) {
        const url = `${this.config.baseUrl}search/articles?artNr=${articleNumber}`;
        return this.http.post(url, requestBody);
    }

    searchAricleByDescription(requestBody: any) {
        const url = `${this.config.baseUrl}search/free-text/`;
        return this.http.post(url, requestBody);
    }

    addHistory(data: any) {
        const url = `${this.config.baseUrl}history/article/add`;
        return this.http.post(url, data).pipe(
            catchError(() => {
                return of(null);
            })
        );
    }

    updateHistory(userArticleHistoryId) {
        const url = `${this.config.baseUrl}history/article/update?userArticleHistoryId=${userArticleHistoryId}`;
        return this.http.put(url, null).pipe(
            catchError(() => {
                return of(null);
            })
        );
    }

    getLatestArticleSearchHistory() {
        const url = `${this.config.baseUrl}history/article/latest`;
        return this.http.get(url).pipe(
            map((res: any) => {
                res.content = (res.content || []).map(item => new ArticleSearchHistory(item));
                return res;
            }),
            catchError(() => {
                return of(null);
            })
        );
    }

    getArticleSearchHistory(data: any) {
        const url = `${this.config.baseUrl}history/article/search?page=${data.pageNumber}`;
        return this.http.post(url, data).pipe(
            map((res: any) => {
                res.content = (res.content || []).map(item => new ArticleSearchHistory(item));
                return res;
            }),
            catchError(() => {
                return of(null);
            })
        );
    }

    searchVinLincense(customerNumber: number) {
        const url = `${this.config.baseUrl}vin/calls`;
        const params = new HttpParams()
            .set('customerNr', customerNumber.toString());
        return this.http.get<number>(url, { observe: 'body', params }).pipe(catchError(error => of(null)));
    }

    getVinPackages() {
        const url = `${this.config.baseUrl}vin/packages`;
        return this.http.get<VinPackage[]>(url, { observe: 'body' })
            .pipe(map((list: any[]) => list.map(item => new VinPackage(item))));
    }

    getVehicleMakeList() {
        const url = `${this.config.baseUrl}search/vehicles/makes`;
        return this.http.get<VehicleMake[]>(url, { observe: 'body' })
            .pipe(map((list: any[]) => list.map(item => new VehicleMake(item))));
    }

    searchVehicles(body, params?) {
        const url = `${this.config.baseUrl}search/vehicles`;
        return this.http.post(url, body, { params, observe: 'body' });
    }

    checkVinSecurity(body: VinSecurityRequest) {
        const url = `${this.config.baseUrl}gtmotive/vin/security-check`;
        return this.http.post<GtMotiveEstimate>(url, body).pipe(map(res => new GtMotiveEstimate(res)));
    }

    getVehicleHistorySearch(vehicleClass?: string) {
        let params;
        if (vehicleClass) {
            params = new HttpParams().set('vehicleClass', vehicleClass);
        }
        const url = `${this.config.baseUrl}history/vehicle`;
        return this.http.get(url, {params}).pipe(
            map((res: any) => {
                return (res.content || []).map(item => new VehicleSearchHistory(item));
            }),
            catchError(error => {
                return of([]);
            })
        );
    }

    getMakeModelTypeData(body) {
        const url = `${this.config.baseUrl}search/vehicles/aggregation`;
        return this.http.post(url, body).pipe(
            catchError(error => {
                return of({});
            })
        );
    }

    getDataOfMakeList() {
        return this.getVehicleMakeList().pipe(
            map((res: VehicleMake[]) => {
                return res.map(({ makeId, make }) => {
                    return { id: makeId, label: make };
                });
            }),
            catchError(error => {
                return of(null);
            })
        );
    }

    getDataOfModelList(makeId) {
        const url = `${this.config.baseUrl}search/vehicles/${makeId}/models`;
        return this.http.get(url).pipe(
            map((res: any[]) => {
                return res.map(({ modelId, model, modelDateBegin, modelDateEnd }) => {
                    const begin = moment(modelDateBegin, 'YYYYMM').format('MM/YYYY');
                    const end = moment(modelDateEnd, 'YYYYMM').format('MM/YYYY');
                    return { id: modelId, label: `${model} [${begin} - ${end}]` };
                });
            }),
            catchError(error => {
                return of(null);
            })
        );
    }

    getDataOfTypeList(make, model) {
        const params = { make, model };
        const url = `${this.config.baseUrl}search/vehicles/types`;
        return this.http.get(url, { params }).pipe(
            map((res: any[]) => {
                return res.map(({ vehId, vehicleName, vehiclePowerKw, vehicleEngineCode }) => {
                    return { id: vehId, label: `${vehicleName} ${vehiclePowerKw} KW ${vehicleEngineCode}` };
                });
            }),
            catchError(error => {
                return of(null);
            })
        );
    }

    searchData(make, model, vehId) {
        const requestData = {
            term: {
                make, model, vehId
            },
            filtering: {}
        };
        const url = `${this.config.baseUrl}search/vehicles`;
        return this.http.post(url, requestData);
    }

    getAdvanceVehicleSearchModelData(body) {
        const url = `${this.config.baseUrl}search/vehicles/advance-aggregation`;
        return this.http.post(url, body).pipe(
            catchError(error => {
                return of({});
            })
        );
    }
}
