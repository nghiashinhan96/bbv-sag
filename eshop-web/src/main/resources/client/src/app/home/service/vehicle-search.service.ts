import { Constant } from 'src/app/core/conts/app.constant';
import { VehicleSearchRequest } from 'src/app/home/models/vehicle-search-request.model';
import { AffiliateUtil, VEHICLE_CLASS } from 'sag-common';
import { VinPackage } from './../models/vin-package.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { finalize, first, map } from 'rxjs/operators';
import { VinSecurityRequest } from '../models/vin-security-request.model';
import { GtMotiveEstimate } from '../models/gt-motive-estimate.model';
import { SEARCH_MODE } from 'sag-article-list';
import { ARTICLE_SEARCH_MODE, ArticleSearchService } from 'sag-article-search';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { ArticleListSearchStorageService } from 'src/app/article-list/services/article-list-storage.service';

@Injectable({
    providedIn: 'root'
})
export class VehicleSearchService {

    private readonly ADD_VIN_URL = 'vin/add';
    private readonly VEHICLE_HISTORY_REVAMP_SEARCH_URL = 'history/vehicles';
    private readonly CHECK_VIN_SECURITY_URL = 'gtmotive/vin/security-check';

    private baseUrl = environment.baseUrl;

    private readonly VALID_VIN_LENGTH = 17;

    constructor(
        private http: HttpClient,
        private router: Router,
        private translateService: TranslateService,
        private analyticService: AnalyticLoggingService,
        private articleListSearchStorageService: ArticleListSearchStorageService,
        private articleSearchService: ArticleSearchService
    ) { }

    getVehicleHistoryRevampSearch(queryData) {
        const url = `${this.baseUrl}${this.VEHICLE_HISTORY_REVAMP_SEARCH_URL}?page=${queryData.pageNumber}`;
        return this.http.post(url, queryData);
    }

    addVinToShoppingCart(packageId: number) {
        const url = `${this.baseUrl}${this.ADD_VIN_URL}?packageNumber=${packageId}`;
        return this.http.post(url, { observe: 'body' });
    }

    checkVinSecurity(body: VinSecurityRequest): Observable<GtMotiveEstimate> {
        const url = `${this.baseUrl}${this.CHECK_VIN_SECURITY_URL}`;
        return this.http.post(url, body).pipe(map(res => new GtMotiveEstimate(res)));
    }

    // =================== business functions =============================================================

    getDefaultVinPackage(vinPackages: VinPackage[]): VinPackage {
        if (!vinPackages || !vinPackages.length) {
            return new VinPackage();
        }
        const defaultVinPkgName = AffiliateUtil.isBaseAT(environment.affiliate) ? 'VIN-10' : 'VIN-20';
        return vinPackages.filter(pkg => pkg.packName.trim() === defaultVinPkgName)[0];
    }

    buildRequestBody(requestBody: any, searchType: string) {
        let body: any;
        const queryParam = {
            size: 10,
            page: 0
        };
        switch (searchType) {
            case Constant.VEHICLE_SEARCH_FREETEXT:
                body = new VehicleSearchRequest().buildVehicleCodeRequest(requestBody);
                break;
            case Constant.VEHICLE_SEARCH_DESC_YEAR:
                body = new VehicleSearchRequest().buildVehicleDescriptionRequest(requestBody);
                break;
        }
        return { body, queryParam };
    }

    isValidVinSearchValue(vinInput: string): boolean {
        return vinInput.length === this.VALID_VIN_LENGTH;
    }

    createVinSecurityRequest(vin: string, requestMode = 'VIN', language: string): VinSecurityRequest {
        return new VinSecurityRequest(vin, requestMode, language);
    }

    navigateToHistory(data) {

        //JSON event with parameter as confirmed in #3290
        const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
        let request: any;
        switch (data.searchMode) {
            case SEARCH_MODE.VIN_CODE:
                this.navigateToVin(data);
                return;
            case SEARCH_MODE.FREE_TEXT:
                request = this.analyticService.createVehicleSearchEventData(
                    {
                        vehSearchTermEnteredTsKzNc: data.searchTerm,
                        vehSearchNumberOfHits: 1
                    },
                    true
                );
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
            case SEARCH_MODE.VEHICLE_DESC:
                request = this.analyticService.createVehicleDescSearchEventData(
                    {
                        vehSearchTermEnteredFzb: data.searchTerm,
                        vehSearchNumberOfHits: 1
                    },
                    true
                );
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
            case SEARCH_MODE.MAKE_MODEL_TYPE:
                let values = [];
                try {
                    values = JSON.parse(data.jsonSearchTerm || '[]') || [];
                } catch { }
                if (AffiliateUtil.isAffiliateApplyMotorbikeShop(environment.affiliate) && data.vehicleClass && data.vehicleClass === VEHICLE_CLASS.MB) {
                    request = this.analyticService.createMotoMakeModelSearchEventData(
                        {
                            vehSearchTypeSelected: 'veh_moto_make_model',
                            vehBrandSelected: values[0],
                            vehModelSelected: values[2],
                            vehVehicleIdResult: data.vehicleId,
                            vehVehicleNameResult: data.searchTerm
                        },
                        true
                    );
                } else {
                    request = this.analyticService.createMakeModelTypeSearchEventData(
                        {
                            vehSearchTypeSelected: 'veh_make_model',
                            vehBrandSelected: values[0],
                            vehModelSelected: values[1],
                            vehTypeSelected: values[3],
                            vehVehicleIdResult: data.vehicleId,
                            vehVehicleNameResult: data.searchTerm
                        },
                        true
                    );
                }
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.jsonSearchTerm, searchMode: data.searchMode } }
                );
                break;
            default:
                request = this.analyticService.createVehicleSearchEventData(
                    {
                        vehSearchTermEnteredTsKzNc: data.searchTerm,
                        vehSearchNumberOfHits: 1
                    },
                    true
                );
                this.router.navigate(['vehicle', data.vehicleId],
                    { queryParams: { keywords: data.searchTerm, searchMode: data.searchMode } }
                );
                break;
        }
        if (request) {
            this.analyticService.postEventFulltextSearch(request, eventType)
                .pipe(first())
                .toPromise();
        }
    }

    navigateToVin(data, onError?) {
        const vinRequestBody = new VinSecurityRequest(data.searchTerm, 'VIN', this.translateService.currentLang.toUpperCase());
        SpinnerService.start();
        this.checkVinSecurity(vinRequestBody)
            .pipe(finalize(() => SpinnerService.stop()))
            .subscribe(res => {
                if (res.errorCode) {
                    if (onError) {
                        onError(res.errorMessage, res.errorCode);
                    }
                    return null;
                }

                const search = {
                    vinCode: data.searchTerm,
                    estimateId: res.estimateId,
                    searchByVin: true,
                    keywords: data.searchTerm,
                    searchMode: data.searchMode
                };
                this.router.navigate(['vin', search]);
                this.sendVinSearchJsonEvent(data.searchTerm);
            }, error => {
                if (onError) {
                    onError(error);
                }
            });
    }

    navigateToArticleSearch(data, userId) {
        let url = ['article', 'result'];
        let queryParams: any = {
            keywords: data.searchTerm,
            ref: data.searchMode
        };
        switch (data.searchMode) {
            case ARTICLE_SEARCH_MODE.ARTICLE_NUMBER:
                queryParams.type = SEARCH_MODE.ARTICLE_NUMBER;
                queryParams.articleNr = data.searchTerm;
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_DESC:
                queryParams.type = SEARCH_MODE.FREE_TEXT;
                queryParams.articleId = data.searchTerm;
                break;
            case ARTICLE_SEARCH_MODE.ARTICLE_ID:
                queryParams.type = SEARCH_MODE.ID_SAGSYS;
                queryParams.articleId = data.articleId;
                break;
            case ARTICLE_SEARCH_MODE.FREE_TEXT:
            case ARTICLE_SEARCH_MODE.SHOPPING_LIST:
                queryParams.type = SEARCH_MODE.FREE_TEXT;
                queryParams.articleId = data.searchTerm;
                break;
            default:
                url = null;
                break;
        }
        if (!url) {
            return;
        }
        this.articleSearchService.updateHistory(data.id).subscribe();
        this.router.navigate(url, { queryParams });
    }

    private sendVinSearchJsonEvent(searchTerm) {
        const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
        const request = this.analyticService.createVinSearchEventData(
            {
                vehSearchTermEnteredVinChn: searchTerm,
                vehSearchNumberOfHits: 1
            }
        );
        if (request) {
            this.analyticService.postEventFulltextSearch(request, eventType)
                .pipe(first())
                .toPromise();
        }
    }
}
