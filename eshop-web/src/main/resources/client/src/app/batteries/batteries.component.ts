import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgOption } from '@ng-select/ng-select';
import { TranslateService } from '@ngx-translate/core';
import { omitBy } from 'lodash';
import { finalize } from 'rxjs/operators';
import { BatteryAnalyticService } from '../analytic-logging/services/battery-analytic.service';
import { Constant } from '../core/conts/app.constant';
import { BATTERY_SEARCH_KEY } from '../core/enums/battery-search-key.enum';
import { UserDetail } from '../core/models/user-detail.model';
import { UserService } from '../core/services/user.service';
import { AdditionalInfo } from '../shared/connect-common/components/additional-info/additional-info.model';
import { BatteriesModel } from './models/batteries.model';
import { BatteryAggregations } from './models/battery-aggregations.model';
import { BatteryFilterRequest } from './models/battery-filter-request.model';
import { BatteryFilter } from './models/battery-filter.model';
import { BatteriesService } from './services/batteries.service';
import { SEARCH_MODE } from 'sag-article-list';
import { CustomerUtil } from '../core/utils/customer.util';
import { environment } from 'src/environments/environment';
import { ADS_TARGET_NAME } from 'sag-common';
import { AppStorageService } from '../core/services/app-storage.service';
import { CriteriaStatistic } from '../shared/connect-common/models/criteria-statistic.model';

@Component({
    selector: 'connect-batteries',
    templateUrl: './batteries.component.html',
    styleUrls: ['./batteries.component.scss']
})
export class BatteriesComponent implements OnInit {
    user: UserDetail;
    private readonly BATTERY_IMAGE_URL = 'https://s3.exellio.de/connect_media/common/images/batteries/';

    private readonly InterconnectionImgMap = [
        'Schema 0',
        'Schema 1',
        'Schema 2',
        'Schema 6',
        'Schema 9'
    ];

    private readonly PolartImgMap = [
        'Polart 1',
        'Polart 19',
        'Polart 1+19',
        'Polart 3',
        'Polart 5'
    ];

    batteriesIsUpdated = false;
    data = new BatteriesModel();
    models: BatteryFilter = new BatteryFilter();
    additionalInfo = [];
    readonly adsTargetName = ADS_TARGET_NAME.BATTERIES;
    isAtCustomer = false;

    ampeData: CriteriaStatistic[];
    fullAmpeDataUsedForRenderTemplate: CriteriaStatistic[];
    ampereHourHistoryData = Constant.SPACE;
    isSliderAmpeDataLoading = false;

    lengthData: CriteriaStatistic[];
    fullLengthDataUsedForRenderTemplate: CriteriaStatistic[];
    lengthHistoryData = Constant.SPACE;
    isSliderLengthDataLoading = false;

    widthData: CriteriaStatistic[];
    fullWidthDataUsedForRenderTemplate: CriteriaStatistic[];
    widthHistoryData = Constant.SPACE;
    isSliderWidthDataLoading = false;

    heightData: CriteriaStatistic[];
    fullHeightDataUsedForRenderTemplate: CriteriaStatistic[];
    heightHistoryData = Constant.SPACE;
    isSliderHeightDataLoading = false;

    private key: string;

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private appStorageService: AppStorageService,
        private batteryAnalyticService: BatteryAnalyticService,
        private userService: UserService,
        private batteriesService: BatteriesService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
        if (this.user && this.user.custNr) {
            this.key = `${this.user.custNr}`;
        }
        this.initSearchFilter();
        this.isAtCustomer = CustomerUtil.isAtCustomer(this.user.customer.affiliateShortName);
        this.additionalInfo = this.getAdditionalInfo();
    }

    viewArticles(models: BatteryFilter) {
        const request: any = omitBy(models, (val: string) => val === Constant.SPACE);
        request.type = SEARCH_MODE.BATTERY_SEARCH;
        request.totalElements = this.data.totalElements;
        request.returnUrl = Constant.BATTERY_PAGE;
        this.addBatterysSearchHistory(request);
        this.router.navigate(['/article/result'], { queryParams: request });
    }

    initSearchFilter() {
        const searchHistory = this.appStorageService.batteryFilterHistory;
        if (searchHistory && searchHistory[this.key]) {
            this.updateSearchHistoryToForm(searchHistory[this.key]);
            this.getBatteriesData(this.models, true);
        } else {
            this.getBatteriesData(new BatteryFilter(), true);
        }
    }

    public updateSearchHistoryToForm(searchHistory: BatteryFilter) {
        this.batteriesIsUpdated = false;
        this.data.totalElements = searchHistory.totalElements;

        for (const prop in this.models) {
            if (this.models.hasOwnProperty(prop)) {
                const options = searchHistory[prop] ? [searchHistory[prop]] : [];
                switch (prop) {
                    case BATTERY_SEARCH_KEY.INTERCONNECTION:
                        this.data.content[prop] = this.buildSelectOptions(options, Constant.INTERCONNECTION_PREFIX + Constant.SPACE);
                        this.models[prop] = searchHistory[prop] || Constant.SPACE;
                        break;
                    case BATTERY_SEARCH_KEY.TYPE_OF_POLE:
                        this.data.content[prop] = this.buildSelectOptions(options, Constant.TYPE_OF_POLE_PREFIX + Constant.SPACE);
                        this.models[prop] = searchHistory[prop] || Constant.SPACE;
                        break;
                    case BATTERY_SEARCH_KEY.WITHOUT_START_STOP:
                    case BATTERY_SEARCH_KEY.WITH_START_STOP:
                    case BATTERY_SEARCH_KEY.IS_RANGE_AMPERE_HOUR_CB:
                    case BATTERY_SEARCH_KEY.IS_RANGE_LENGTH_CB:
                    case BATTERY_SEARCH_KEY.IS_RANGE_WIDTH_CB:
                    case BATTERY_SEARCH_KEY.IS_RANGE_HEIGHT_CB:
                        this.models[prop] = String(searchHistory[prop]) === Constant.TRUE_AS_STRING;
                        break;
                    default:
                        this.data.content[prop] = this.buildSelectOptions(options);
                        this.models[prop] = searchHistory[prop] || Constant.SPACE;
                }
                this.ampereHourHistoryData = this.models.ampereHour;
                this.lengthHistoryData = this.models.length;
                this.widthHistoryData = this.models.width;
                this.heightHistoryData = this.models.height;
            }
        }
        this.models.lastUpdatedField = '';

        this.batteriesIsUpdated = true;
    }

    updateFilters(models: BatteryFilter) {
        this.getBatteriesData(models);
    }

    resetFilter() {
        this.models = new BatteryFilter();
        this.getBatteriesData(this.models, true);
        this.addBatterysSearchHistory(null);
    }

    getVoltageOptions(request: BatteryFilter) {
        const params = new BatteryFilterRequest({ ...request });
        this.batteriesService.getBatteriesDataFilter(params).subscribe(
            res => {
                if (res && res.totalElements) {
                    this.fullAmpeDataUsedForRenderTemplate = res.content.ampere_hours as CriteriaStatistic[];
                    this.fullLengthDataUsedForRenderTemplate = res.content.lengths as CriteriaStatistic[];
                    this.fullWidthDataUsedForRenderTemplate = res.content.widths as CriteriaStatistic[];
                    this.fullHeightDataUsedForRenderTemplate = res.content.heights as CriteriaStatistic[];
                    this.triggerDataChange('ampeData');
                    this.triggerDataChange('lengthData');
                    this.triggerDataChange('widthData');
                    this.triggerDataChange('heightData');
                    const voltages = this.buildSelectOptions(res.content.voltages);
                    if (voltages && voltages.length) {
                        voltages.shift();
                        this.data.content = { ...this.data.content, voltages };
                    }
                }
            },
            err => {
                console.log(err);
            });
    }

    getBatteriesData(request: BatteryFilter, isInit?: boolean) {
        this.batteriesIsUpdated = false;
        const params = new BatteryFilterRequest({ ...request });

        const isAmpeSliderChanged = this.models.lastUpdatedField === BATTERY_SEARCH_KEY.AMPERE_HOUR;
        const isLengthSliderChanged = this.models.lastUpdatedField === BATTERY_SEARCH_KEY.LENGTH;
        const isWidthSliderChanged = this.models.lastUpdatedField === BATTERY_SEARCH_KEY.WIDTH;
        const isHeightSliderChanged = this.models.lastUpdatedField === BATTERY_SEARCH_KEY.HEIGHT;

        if (this.models.isFilterChanged) {
            this.isSliderAmpeDataLoading = !isAmpeSliderChanged || this.models.isToggleSliderOn;
            this.isSliderLengthDataLoading = !isLengthSliderChanged || this.models.isToggleSliderOn;
            this.isSliderWidthDataLoading = !isWidthSliderChanged || this.models.isToggleSliderOn;
            this.isSliderHeightDataLoading = !isHeightSliderChanged || this.models.isToggleSliderOn;
        } else {
            this.isSliderAmpeDataLoading = isAmpeSliderChanged;
            this.isSliderLengthDataLoading = isLengthSliderChanged;
            this.isSliderWidthDataLoading = isWidthSliderChanged;
            this.isSliderHeightDataLoading = isHeightSliderChanged;
        }
        
        this.batteriesService.getBatteriesDataFilter(params)
            .pipe(finalize(() => {
                this.batteriesIsUpdated = true;
                this.updateSpinnerAllSlider(false);
            }))
            .subscribe(
                res => {
                    if (!res) {
                        return;
                    }

                    const voltages = this.data.content.voltages;
                    this.data = res;
                    this.data.content.voltages = voltages;
                    this.models.totalElements = this.data.totalElements;

                    const isInitSliderChart = isInit || this.models.isToggleSliderOn;
                    this.updateFilterData('ampere_hours', 'ampeData', request.isRangeAmpereHourCb, this.models.isFilterChanged && isAmpeSliderChanged && !isInitSliderChart);
                    this.updateFilterData('lengths', 'lengthData', request.isRangeLengthCb, this.models.isFilterChanged && isLengthSliderChanged && !isInitSliderChart);
                    this.updateFilterData('widths', 'widthData', request.isRangeWidthCb, this.models.isFilterChanged && isWidthSliderChanged && !isInitSliderChart);
                    this.updateFilterData('heights', 'heightData', request.isRangeHeightCb, this.models.isFilterChanged && isHeightSliderChanged && !isInitSliderChart);
                    
                    if (this.data.totalElements === 0) {
                        return;
                    }
                    this.updateFilterOptions(this.data.content);
                    
                    if (isInit) {
                        // always get full Voltage Options when loaded
                        this.getVoltageOptions(new BatteryFilter(Constant.SPACE));
                    }
                },
                err => {
                    console.log(err);
                }
            );
    }

    updateFilterOptions(aggregations: BatteryAggregations) {
        const options = {
            voltages: aggregations.voltages,
            ampere_hours: this.buildSelectOptions(aggregations.ampere_hours),
            lengths: this.buildSelectOptions(aggregations.lengths),
            widths: this.buildSelectOptions(aggregations.widths),
            heights: this.buildSelectOptions(aggregations.heights),
            interconnections: this.buildSelectOptions(
                aggregations.interconnections, `${Constant.INTERCONNECTION_PREFIX}${Constant.SPACE}`, this.InterconnectionImgMap
            ),
            poles: this.buildSelectOptions(
                aggregations.typeOfPoles, `${Constant.TYPE_OF_POLE_PREFIX}${Constant.SPACE}`, this.PolartImgMap
            )
        };

        this.data.content = new BatteryAggregations(options);
    }

    buildSelectOptions(data, prefix?: string, imageKeys?: string[]): NgOption[] {
        const options: NgOption[] = [{
            value: Constant.SPACE,
            label: this.translateService.instant('COMMON_LABEL.ALL')
        }];
        const customText = prefix ? prefix : '';

        data
            .filter(item => item !== Constant.FILTER_OPTION_ALL_KEY)
            .forEach(element => {
                let img = '';
                const label = `${customText}${element}`;
                if (imageKeys && imageKeys.length) {
                    const imgKey = imageKeys.find(key => key === label);
                    if (imgKey) {
                        const imgFileName = imgKey.replace(/\s+|\+/g, '_').toLowerCase() + '.png';
                        img = `${this.BATTERY_IMAGE_URL}${imgFileName}`;
                    }
                }
                options.push({ value: element, label, img });
            });

        return options;
    }

    sendAdsClick() {
        this.batteryAnalyticService.sendAdsEventData();
    }

    private addBatterysSearchHistory(data) {
        const value = this.appStorageService.batteryFilterHistory || {};
        value[this.key] = data;
        this.appStorageService.batteryFilterHistory = value;
    }

    private getAdditionalInfo(): AdditionalInfo[] {
        const links = [
            {
                url: 'https://s3.exellio.de/connect_media/common/pdfs/batteries/ba_schaltbilder.pdf',
                text: 'BATTERIES.DIAGRAMS'
            },
            {
                url: 'https://s3.exellio.de/connect_media/common/pdfs/batteries/ba_bodenbefestigungen.pdf',
                text: 'BATTERIES.FLOOR_FIXINGS'
            },
            {
                url: 'https://s3.exellio.de/connect_media/common/pdfs/batteries/ba_warnhinweise.pdf',
                text: 'BATTERIES.WARNINGS_AND_SAFETY_PRECAUTIONS'
            },
            {
                url: 'https://s3.exellio.de/connect_media/common/pdfs/batteries/ba_bemerkungen.pdf',
                text: 'BATTERIES.REMARKS'
            },
            {
                url: 'https://s3.exellio.de/connect_media/common/pdfs/batteries/ba_polarten.pdf',
                text: 'BATTERIES.TYPES_OF_POLES'
            }
        ];
        if (this.isAtCustomer) {
            const mediaLink = `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs`;
            links.splice(2,0,{
                url: `${mediaLink}/Exide%20Gewaehrleistungsfristen_SAG.pdf`,
                text: 'BATTERIES.EXIDE'
            })
        }
        return links;
    }

    // adapt data for single selection
    private adaptDataForSingleSelectionMode(aggregationData: any[]) {
        let newAggregationData = (aggregationData || []).slice();
        if (aggregationData && aggregationData.length > 0 && typeof aggregationData[0] === 'object') {
            newAggregationData = aggregationData.map(item => item.criteriaValue);
        }
        return newAggregationData;
    }

    onSliderValueChangeEmitter(models: BatteryFilter) {
        this.getBatteriesData(models, false);
    }

    private triggerDataChange(key) {
        if (this[key]) {
            const data = this[key].slice();
            this[key] = data;
        }
    }

    private updateFilterData(singleValueKey, rangeValueKey, isRange, shouldSkipSliderDataUpdating) {
        if (this.data.content && this.data.content[singleValueKey]) {
            if (isRange) {
                if (!shouldSkipSliderDataUpdating) {
                    const data = this.data.content[singleValueKey] as CriteriaStatistic[];
                    this[rangeValueKey] = data;
                }
            } else {
                this.data.content[singleValueKey] = this.adaptDataForSingleSelectionMode(this.data.content[singleValueKey]);
            }
        }
    }

    private updateSpinnerAllSlider(value: boolean) {
        this.isSliderAmpeDataLoading = value;
        this.isSliderLengthDataLoading = value;
        this.isSliderWidthDataLoading = value;
        this.isSliderHeightDataLoading = value;
    }
}
