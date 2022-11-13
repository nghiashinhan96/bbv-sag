import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgOption } from '@ng-select/ng-select';
import { TranslateService } from '@ngx-translate/core';
import { keys, omitBy } from 'lodash';
import { finalize } from 'rxjs/operators';
import { BulbAnalyticService } from '../analytic-logging/services/bulb-analytic.service';
import { Constant } from '../core/conts/app.constant';
import { UserDetail } from '../core/models/user-detail.model';
import { UserService } from '../core/services/user.service';
import { AdditionalInfo } from '../shared/connect-common/components/additional-info/additional-info.model';
import { BulbsAggregations } from './models/bulb-aggregations.model';
import { BulbFilterRequest } from './models/bulb-filter-request.model';
import { BulbFilter } from './models/bulb-filter.model';
import { BulbsModel } from './models/bulbs.model';
import { BulbsService } from './services/bulbs.service';
import { SEARCH_MODE } from 'sag-article-list';
import { ADS_TARGET_NAME } from 'sag-common';
import { AppStorageService } from '../core/services/app-storage.service';

@Component({
    selector: 'connect-bulb',
    templateUrl: './bulbs.component.html',
    styleUrls: ['./bulbs.component.scss']
})
export class BulbsComponent implements OnInit {
    user: UserDetail;
    bulbsIsUpdated = false;
    data = new BulbsModel();
    models: BulbFilter = new BulbFilter();

    readonly adsTargetName = ADS_TARGET_NAME.BULBS;
    additionalInfo = this.getAdditionalInfo();

    private key: string;

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private appStorageService: AppStorageService,
        private bulbAnalyticService: BulbAnalyticService,
        private userService: UserService,
        private bulbsService: BulbsService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
        if (this.user && this.user.custNr) {
            this.key = `${this.user.custNr}`;
        }
        this.initSearchFilter();
        this.getInfoLink();
    }

    viewArticles(models: BulbFilter) {
        const request: any = omitBy(models, (val: string) => val === Constant.SPACE);
        request.type = SEARCH_MODE.BULB_SEARCH;
        request.totalElements = this.data.totalElements;
        request.returnUrl = Constant.BULB_PAGE;
        this.addBulbsSearchHistory(request);
        this.bulbAnalyticService.sendEventData(request);
        this.router.navigate(['/article/result'], { queryParams: request });
    }

    initSearchFilter() {
        const searchHistory = this.appStorageService.bulbFilterHistory;
        if (searchHistory && searchHistory[this.key]) {
            this.updateSearchHistoryToForm(searchHistory[this.key]);
            this.getBulbsData(this.models);
        } else {
            this.getBulbsData(new BulbFilter());
        }
    }

    updateSearchHistoryToForm(searchHistory: BulbFilter) {
        this.bulbsIsUpdated = false;
        this.data.totalElements = searchHistory.totalElements;

        for (const prop in this.models) {
            if (this.models.hasOwnProperty(prop)) {
                const options = searchHistory[prop] ? [searchHistory[prop]] : [];
                this.data.content[prop] = this.buildSelectOptions(options);
                this.models[prop] = searchHistory[prop] || Constant.SPACE;
            }
        }

        this.bulbsIsUpdated = true;
    }

    updateFilters(models: BulbFilter) {
        this.getBulbsData(models);
    }

    resetFilter() {
        this.models = new BulbFilter();
        this.getBulbsData(this.models);
        this.addBulbsSearchHistory(null);
    }

    getBulbsData(request: BulbFilter) {
        this.bulbsIsUpdated = false;
        const params = new BulbFilterRequest({ ...request });
        this.bulbsService.getBulbsDataFilter(params)
            .pipe(finalize(() => {
                this.bulbsIsUpdated = true;
            }))
            .subscribe(
                res => {
                    if (!res) {
                        return;
                    }

                    this.data = res;
                    this.models.totalElements = this.data.totalElements;

                    if (this.data.totalElements === 0) {
                        return;
                    }

                    this.updateFilterOptions(this.data.content);
                },
                err => {
                    console.log(err);
                }
            );
    }

    updateFilterOptions(aggregations: BulbsAggregations) {
        const options = {
            codes: this.buildSelectOptions(aggregations.codes, '', true),
            voltages: this.buildSelectOptions(aggregations.voltages),
            watts: this.buildSelectOptions(aggregations.watts),
            suppliers: this.buildSelectOptions(this.bulbsService.sortSupplier(aggregations.suppliers))
        };

        const totalSpecialSupplier = this.bulbsService.totalSpecialSupplier(aggregations.suppliers);

        if (totalSpecialSupplier > 1) {
            options.suppliers.splice(totalSpecialSupplier, 0, { disabled: true });
        }

        this.data.content = new BulbsAggregations(options);
    }

    buildSelectOptions(data, prefix?: string, withImage?: boolean): NgOption[] {
        const options: NgOption[] = [{
            value: Constant.SPACE,
            label: 'COMMON_LABEL.ALL'
        }];
        const customText = prefix ? prefix : '';

        data = data.filter(item => item !== Constant.FILTER_OPTION_ALL_KEY);

        data.forEach(element => {
            let img = '';
            if (withImage) {
                const imgKey = keys(BulbsService.CodesImgMap).find((key) => element.search(key) !== -1);
                if (imgKey) {
                    img = 'https://s3.exellio.de/connect_media/common/images/bulbs/' + BulbsService.CodesImgMap[imgKey];
                }
            }
            options.push({ value: element, label: customText + element, img });
        });

        return options;
    }

    getAdditionalInfo(): AdditionalInfo[] {
        return [
            {
                url: this.getInfoLink(),
                text: 'BULBS.ADDITIONAL_LINK_1'
            }
        ];
    }

    sendAdsClick() {
        this.bulbAnalyticService.sendAdsEventData();
    }

    private addBulbsSearchHistory(data) {
        const value = this.appStorageService.bulbFilterHistory || {};
        value[this.key] = data;
        this.appStorageService.bulbFilterHistory = value;
    }

    private getInfoLink() {
        switch (this.translateService.currentLang) {
            case Constant.LANG_CODE_DE:
                return 'https://am-application.osram.info/de';
            case Constant.LANG_CODE_FR:
                return 'https://am-application.osram.info/fr';
            case Constant.LANG_CODE_IT:
                return 'https://am-application.osram.info/it';
            default:
                return 'https://am-application.osram.info/de';
        }
    }
}
