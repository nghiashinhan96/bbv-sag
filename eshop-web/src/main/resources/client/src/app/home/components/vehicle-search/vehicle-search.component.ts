import { Component, OnInit, Input, AfterViewInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { catchError } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';

import {
    LIB_VEHICLE_SEARCH_DESC_YEAR,
    LIB_VEHICLE_SEARCH_VIN,
    LIB_VEHICLE_SEARCH_FREETEXT,
    LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE,
    VIN_MODE
} from 'sag-article-search';

import { get } from 'lodash';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { DmsInfo } from 'src/app/dms/models/dms-info.model';
import { DmsConstant } from 'src/app/dms/constants/dms.constant';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { FeedbackVehicleSearch } from 'src/app/feedback/models/feedback-vehicle-search.model';
import { VehicleSearchService } from '../../service/vehicle-search.service';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { UserService } from 'src/app/core/services/user.service';
import { first } from 'rxjs/operators';
import { GoogleAnalyticsService } from 'src/app/analytic-logging/services/google-analytics.service';
import { GA_SEARCH_CATEGORIES } from 'src/app/analytic-logging/enums/ga-search-category.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { permissions } from 'src/app/core/utils/permission';
import { environment } from 'src/environments/environment';
import { SEARCH_MODE } from 'sag-article-list';
import { AffiliateUtil } from 'sag-common';
import { MOTORBIKE_PERMISSION_CODE } from 'src/app/shared/connect-common/enums/motorbike-permission-code.enum';

@Component({
    selector: 'connect-vehicle-search',
    templateUrl: './vehicle-search.component.html',
    styleUrls: ['./vehicle-search.component.scss']
})
export class VehicleSearchComponent implements OnInit, AfterViewInit, OnDestroy {
    @Input() customerNumber = '';
    @Input() set isMatikChCustomer(val: boolean) {
        this.isMatikCh = val;
        this.isSearchboxCollapsed = val;
    }
    @Input() autoFilledVinValue = '';

    language = '';
    dmsSearchQuery = null;
    dmsInfo: DmsInfo;
    isFinalUserRole = false;
    isSalesOnBeHalf = false;
    hasVinSearchPermission = false;
    subscription = new Subscription();
    isSearchboxCollapsed = false;
    isMatikCh = false;
    isCH = AffiliateUtil.isAffiliateCH(environment.affiliate);
    isAT = AffiliateUtil.isAffiliateAT(environment.affiliate);
    isAffiliateApplyMotorbikeShop = AffiliateUtil.isAffiliateApplyMotorbikeShop(environment.affiliate);
    MOTORBIKE_PERMISSION_CODE = MOTORBIKE_PERMISSION_CODE;

    constructor(
        private translateService: TranslateService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService,
        private fbRecordingService: FeedbackRecordingService,
        private dmsService: ActiveDmsProcessor,
        private vehicleSearchService: VehicleSearchService,
        private shoppingBasketService: ShoppingBasketService,
        private userService: UserService,
        private analyticService: AnalyticLoggingService,
        private gaService: GoogleAnalyticsService
    ) {
        this.language = this.translateService.currentLang;

    }

    ngOnInit() {
        this.isFinalUserRole = this.userService.userDetail.isFinalUserRole;
        this.isSalesOnBeHalf = this.userService.userDetail.isSalesOnBeHalf;
        this.subscription.add(
            this.userService.hasPermissions([permissions.vin.functions.search]).subscribe(value => {
                this.hasVinSearchPermission = value;
            })
        );
    }

    ngAfterViewInit() {
        this.dmsInfo = this.dmsService.getDmsInfo();
        this.handleDmsVersion();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
    }

    handleDmsVersion() {
        if (!this.dmsInfo) {
            return;
        }

        const { version } = this.dmsInfo;
        if (version === DmsConstant.VERSION_3) {
            this.dmsSearchQuery = {
                vehicleName: this.dmsInfo.vehicleText,
                vehicleVin: this.dmsInfo.vin,
                vehicleCode: this.dmsInfo.typenschein,
                isDmsVersion3: true,
            };
            this.dmsService.updateDmsInfo(this.dmsInfo);
        } else {
            if (!this.dmsInfo.searched) {
                this.dmsSearchQuery = {
                    vehicleName: this.dmsInfo.vehicleText,
                    vehicleVin: this.dmsInfo.vin,
                    vehicleCode: this.dmsInfo.typenschein,
                    isDmsVersion3: false,
                };
                this.dmsInfo.searched = true;
                this.dmsService.updateDmsInfo(this.dmsInfo);
            }
        }
    }

    searchVehicle(data) {
        const searchType = data.searchType;
        const search = data.search || {};
        const { filtering, term } = search;
        let feedbackModel;
        this.sendVehicleSearchDataAnalytic(data);
        switch (searchType) {

            case LIB_VEHICLE_SEARCH_FREETEXT:
                feedbackModel = new FeedbackVehicleSearch({
                    type: this.translateService.instant('SEARCH.VEHICLES'),
                    searchText: data.info
                });

                this.router.navigate(['../vehicle', search], {
                    queryParams: {
                        keywords: data.info,
                        searchMode: SEARCH_MODE.VEHICLE_CODE
                    },
                    relativeTo: this.activatedRoute
                });
                break;
            case LIB_VEHICLE_SEARCH_DESC_YEAR:
                this.appStorage.vehicleFilter = JSON.stringify(search);
                let descYearSearchText;
                let descYearSearchType;
                if (term && term.vehicleDesc) {
                    descYearSearchType = this.translateService.instant('SEARCH.VEHICLES_DESC');
                    descYearSearchText = term.vehicleDesc;
                }
                if (filtering && filtering.built_year_month_from) {
                    descYearSearchType += '/' + this.translateService.instant('SEARCH.YEAR');
                    descYearSearchText += '/' + filtering.built_year_month_from;
                }
                feedbackModel = new FeedbackVehicleSearch({
                    type: descYearSearchType,
                    searchText: descYearSearchText
                });

                this.router.navigate(['vehicle-filtering']);
                break;
            case LIB_VEHICLE_SEARCH_VIN:
                if (search.vinMode === VIN_MODE.STANDARD) {
                    const queryParams = {
                        vin: search.vinCode
                    };
                    this.router.navigate(['saginfo'], { queryParams });
                } else {
                    this.router.navigate(['../vin', search], {
                        relativeTo: this.activatedRoute
                    });
                }
                break;
            case LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE:
                this.router.navigate(['../vehicle', data.body.typeCode], {
                    queryParams: {
                        keywords: data.searchKey,
                        searchMode: SEARCH_MODE.MAKE_MODEL_TYPE
                    },
                    relativeTo: this.activatedRoute
                });
                break;
        }

        if (feedbackModel) {
            this.fbRecordingService.recordVehicleSearch(feedbackModel);
        }
    }

    searchMakeModel({ typeCode }) {
        this.router.navigate(['../vehicle', typeCode], {
            relativeTo: this.activatedRoute
        });
    }

    buyVin(packId) {
        if (packId) {
            this.vehicleSearchService.addVinToShoppingCart(packId)
                .pipe(
                    catchError(err => {
                        return of(null);
                    })
                )
                .subscribe(
                    res => {
                        this.shoppingBasketService.updateOtherProcess(res);

                        this.router.navigate(['shopping-basket', 'cart']);
                    }
                );
        }
    }

    openAdvanceVehicleSearch() {
        this.router.navigate(['advance-vehicle-search', 'makes']);
    }

    private sendVehicleSearchDataAnalytic(data: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
            let request: any;
            let searchTerm;
            switch (data.searchType) {
                case LIB_VEHICLE_SEARCH_DESC_YEAR:
                    searchTerm = get(data, 'analytic.searchTerm');
                    request = this.analyticService.createVehicleDescSearchEventData(
                        {
                            vehSearchTermEnteredFzb: get(data, 'analytic.searchTerm'),
                            vehSearchTermEnteredJg: get(data, 'analytic.searchYear'),
                            vehSearchNumberOfHits: get(data, 'analytic.numberOfHits')
                        }
                    );
                    break;
                case LIB_VEHICLE_SEARCH_FREETEXT:
                    searchTerm = get(data, 'analytic.searchTerm');
                    request = this.analyticService.createVehicleSearchEventData(
                        {
                            vehSearchTermEnteredTsKzNc: get(data, 'analytic.searchTerm'),
                            vehSearchNumberOfHits: get(data, 'analytic.numberOfHits')
                        }
                    );
                    break;
                case LIB_VEHICLE_SEARCH_VIN:
                    searchTerm = get(data, 'search.vinCode');
                    request = this.analyticService.createVinSearchEventData(
                        {
                            vehSearchTermEnteredVinChn: get(data, 'search.vinCode'),
                            vehSearchNumberOfHits: 1
                        }
                    );
                    break;
                case LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE:
                    let values = [];
                    try {
                        values = JSON.parse(data.searchKey || '[]') || [];
                    } catch { }
                    request = this.analyticService.createMakeModelTypeSearchEventData(
                        {
                            vehSearchTypeSelected: 'veh_make_model',
                            vehBrandSelected: values[0],
                            vehModelSelected: values[1],
                            vehTypeSelected: values[3],
                            vehVehicleIdResult: get(data, 'body.typeCode'),
                            vehVehicleNameResult: values.length > 0 ? values.join(' ') : data.searchKey
                        }
                    );
                default:
                    break;
            }
            if (request) {
                // GA4 Search event
                const sourceId = request.basket_item_source_id || '';
                const sourceDesc = request.basket_item_source_desc || '';
                this.gaService.search('', searchTerm || '', sourceId, sourceDesc);

                // Json event
                this.analyticService.postEventFulltextSearch(request, eventType)
                    .pipe(first())
                    .toPromise();
            }
        });
    }

    navigateToMotorbikeShop() {
        this.router.navigate(['moto']);
    }

    navigateToModelsPage(make) {
        if (!make) {
            return;
        }
        this.appStorage.advanceVehicleSearchMake = JSON.stringify(make);
        this.router.navigate(['/advance-vehicle-search', 'models'], { relativeTo: this.activatedRoute });
    }
}
