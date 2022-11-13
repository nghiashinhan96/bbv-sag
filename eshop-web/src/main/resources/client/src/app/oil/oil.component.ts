import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgOption } from '@ng-select/ng-select';
import { TranslateService } from '@ngx-translate/core';
import { omitBy } from 'lodash';
import { finalize } from 'rxjs/operators';
import { SEARCH_MODE } from 'sag-article-list';
import { ADS_TARGET_NAME, AffiliateEnum, AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { OilAnalyticService } from '../analytic-logging/services/oil-analytic.service';
import { Constant } from '../core/conts/app.constant';
import { UserDetail } from '../core/models/user-detail.model';
import { AppStorageService } from '../core/services/app-storage.service';
import { UserService } from '../core/services/user.service';
import { AdditionalInfo } from '../shared/connect-common/components/additional-info/additional-info.model';
import { CriteriaStatistic } from '../shared/connect-common/models/criteria-statistic.model';
import { LangInfo } from './models/lang-info.model';
import { OilAggregations } from './models/oil-aggregations.model';
import { OilFilterRequest } from './models/oil-filter-request.model';
import { OilFilter } from './models/oil-filter.model';
import { OilModel } from './models/oil.model';
import { OilService } from './services/oil.service';

@Component({
    selector: 'connect-oil',
    templateUrl: './oil.component.html',
    styleUrls: ['./oil.component.scss']
})
export class OilComponent implements OnInit {
    user: UserDetail;
    oilIsUpdated = false;
    data = new OilModel();
    models: OilFilter = new OilFilter();

    private readonly WHICH_ENGINE_URL = 'http://www.technomag.ch';
    private readonly MOBILE_PRODUCT_URL = 'http://www.exxonmobil.com/pdssearch/search.aspx';
    private readonly MOBILE_SAFETY_URL = 'http://www.msds.exxonmobil.com/psims/psims.aspx';
    private readonly LUBRICANT_CATALOG_URL = 'http://issuu.com/technomag-ch/docs/schmiermittelkatalog_2015';

    private key: string;

    readonly adsTargetName = ADS_TARGET_NAME.OIL;
    showAdditionalInfo = false;
    additionalInfo = this.getAdditionalInfo();

    bottleSizeHistoryData = Constant.SPACE;
    fullBottleSizeDataUsedForRenderTemplate: CriteriaStatistic[] = [];
    bottleSizeData: CriteriaStatistic[] = [];
    isSliderDataLoading = false;

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private appStorageService: AppStorageService,
        private oilAnalyticService: OilAnalyticService,
        private userService: UserService,
        private oilService: OilService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
        if (this.user && this.user.custNr) {
            this.key = `${this.user.custNr}`;
        }
        this.initSearchFilter();
        this.showAdditionalInfo = environment.affiliate === AffiliateEnum.TECHNO_CH;
    }

    viewArticles(models: OilFilter) {
        const request: any = omitBy(models, (val: string) => val === Constant.SPACE);
        request.type = SEARCH_MODE.OIL_SEARCH;
        request.totalElements = this.data.totalElements;
        request.returnUrl = Constant.OIL_PAGE;
        this.addOilSearchHistory(request);
        this.oilAnalyticService.sendEventData(request);
        this.router.navigate(['/article/result'], { queryParams: request });
    }

    initSearchFilter() {
        const searchHistory = this.appStorageService.oilFilterHistory;

        if (searchHistory && searchHistory[this.key]) {
            this.updateSearchHistoryToForm(searchHistory[this.key]);
            this.getOilData(this.models, true);
        } else {
            this.getOilData(new OilFilter(), true);
        }
    }

    updateSearchHistoryToForm(searchHistory: OilFilter) {
        this.oilIsUpdated = false;
        this.data.totalElements = searchHistory.totalElements;

        for (const prop in this.models) {
            if (this.models.hasOwnProperty(prop)) {
                const options = searchHistory[prop] ? [searchHistory[prop]] : [];

                if (prop === 'isRangeBottleSizeCb') {
                    this.models[prop] = String(searchHistory[prop]) === Constant.TRUE_AS_STRING;
                } else {
                    this.data.content[prop] = this.buildSelectOptions(options);
                    this.models[prop] = searchHistory[prop] || Constant.SPACE;
                }

                this.bottleSizeHistoryData = this.models.bottleSize;
            }
        }

        this.oilIsUpdated = true;
    }

    updateFilters(models: OilFilter) {
        this.getOilData(models);
    }

    resetFilter() {
        this.models = new OilFilter();
        this.getOilData(this.models, true);
        this.addOilSearchHistory(null);
    }

    getOilData(request: OilFilter, isInit = false, shouldSkipSliderDataUpdating?: boolean) {
        this.oilIsUpdated = false;

        if (!shouldSkipSliderDataUpdating && request.isRangeBottleSizeCb) {
            this.isSliderDataLoading = true;
        }

        const params = new OilFilterRequest({ ...request });
        this.oilService.getOilDataFilter(params)
            .pipe(finalize(() => {
                this.oilIsUpdated = true;
                this.isSliderDataLoading = false;
            }))
            .subscribe(
                res => {
                    if (!res) {
                        return;
                    }

                    this.data = res;
                    this.models.totalElements = this.data.totalElements;

                    if (this.data.content && this.data.content.bottle_sizes) {

                        if (request.isRangeBottleSizeCb) {
                            if (!shouldSkipSliderDataUpdating) {
                                this.bottleSizeData = [...this.data.content.bottle_sizes as CriteriaStatistic[]];
                            }
                        } else {
                            const singleBottleSizeSelection =
                                [...this.data.content.bottle_sizes as CriteriaStatistic[]].map(x => x.criteriaValue);
                            this.data.content.bottle_sizes = singleBottleSizeSelection as any[];
                        }
                    }
                    if (this.data.totalElements === 0) {
                        return;
                    }


                    this.updateFilterOptions(this.data.content);

                    if (isInit) {
                        this.getFullBottleSizeDataUsedForRenderTemplate();
                    }
                },
                err => {
                    console.log(err);
                }
            );
    }

    updateFilterOptions(aggregations: OilAggregations) {
        const options = {
            vehicles: this.buildSelectOptions(aggregations.vehicles),
            aggregates: this.buildSelectOptions(aggregations.aggregates),
            viscosities: this.buildSelectOptions(aggregations.viscosities),
            bottle_sizes: this.buildSelectOptions(aggregations.bottle_sizes),
            approved_list: this.buildSelectOptions(aggregations.approved_list),
            specifications: this.buildSelectOptions(aggregations.specifications),
            brands: this.buildSelectOptions(aggregations.brands)
        };

        this.data.content = new OilAggregations(options);
    }

    buildSelectOptions(data, prefix?: string, withImage?: boolean): NgOption[] {
        const options: NgOption[] = [{
            value: Constant.SPACE,
            label: 'COMMON_LABEL.ALL'
        }];
        const customText = prefix ? prefix : '';


        data
            .filter(item => item !== Constant.FILTER_OPTION_ALL_KEY)
            .forEach(element => {
                options.push({ value: element, label: `${customText}${element}` });
            });

        return options;
    }

    buildLangInfo() {
        const langInfo = new LangInfo();
        langInfo.countryLang = AffiliateUtil.isAffiliateCH(environment.affiliate) ? 'Switzerland' : Constant.EMPTY_STRING;
        langInfo.langCode = this.appStorageService.appLangCode || Constant.LANG_CODE_DE;

        switch (langInfo.langCode) {
            case Constant.LANG_CODE_FR:
                langInfo.lang = this.translateService.instant('SETTINGS.PROFILE.LANGUAGE.LANG_FR');
                langInfo.engineInfo = 'lubrifiants-mobil/huiles-moteurs-pour-voitures/quelle-huile-moteur-recherche/';
                langInfo.languageselectedvalue = '5';
                break;
            case Constant.LANG_CODE_IT:
                langInfo.lang = this.translateService.instant('SETTINGS.PROFILE.LANGUAGE.LANG_IT');
                langInfo.languageselectedvalue = '6';
                break;
            default:
                langInfo.lang = this.translateService.instant('SETTINGS.PROFILE.LANGUAGE.LANG_DE');
                langInfo.engineInfo = 'mobil-schmierstoffe/pkw-motorenoele/welches-motorenoel-suche-starten/';
                langInfo.languageselectedvalue = '2';
        }
        return langInfo;
    }

    sendAdsClick() {
        this.oilAnalyticService.sendAdsEventData();
    }

    onSliderValueChangeEmitter(request: OilFilter) {
        this.getOilData(request, false, true);
    }

    private addOilSearchHistory(data) {
        const value = this.appStorageService.oilFilterHistory || {};
        value[this.key] = data;
        this.appStorageService.oilFilterHistory = value;
    }

    private getFullBottleSizeDataUsedForRenderTemplate(): void {
        this.oilService.getOilDataFilter(new OilFilterRequest(new OilFilter())).subscribe((res) => {
            if (res && res.totalElements) {
                this.fullBottleSizeDataUsedForRenderTemplate = res.content.bottle_sizes as CriteriaStatistic[];
                if (this.bottleSizeData) {
                    this.bottleSizeData = [...this.bottleSizeData];
                }
            }
        });
    }

    private getAdditionalInfo(): AdditionalInfo[] {
        const { langCode, engineInfo, countryLang, lang, languageselectedvalue } = this.buildLangInfo();
        let oilLink = '';
        let productLink;
        let safetyLink;

        if ((engineInfo || '').trim()) {
            oilLink = `${this.WHICH_ENGINE_URL}/${langCode}/microsites/${engineInfo}`;
        }

        productLink =
            `${this.MOBILE_PRODUCT_URL}?chooseLanguage=${langCode}&CountryValue=${countryLang}`;

        // tslint:disable-next-line: max-line-length
        safetyLink = `${this.MOBILE_SAFETY_URL}?language=${langCode.toUpperCase()}&brand=XOM&lstext=${lang}&languageselectedvalue=${languageselectedvalue}`;

        return [
            {
                url: oilLink,
                text: 'SEARCH.SEARCH_OIL.WHICH_ENGINE_OIL'
            },
            {
                url: productLink,
                text: 'SEARCH.SEARCH_OIL.MOBILE_PRODUCT_DATA_SHEETS'
            },
            {
                url: safetyLink,
                text: 'SEARCH.SEARCH_OIL.MOBILE_SAFETY_DATA_SHEETS'
            },
            {
                url: this.LUBRICANT_CATALOG_URL,
                text: 'SEARCH.SEARCH_OIL.LUBRICANT_CATALOG'
            }
        ];
    }
}
