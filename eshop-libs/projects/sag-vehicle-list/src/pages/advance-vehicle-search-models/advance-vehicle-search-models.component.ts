import { Component, OnInit, Output, EventEmitter, OnDestroy, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { finalize, map } from 'rxjs/operators';
import { ArticleSearchService } from 'sag-article-search';
import { VEHICLE_CLASS } from 'sag-common';
import { SagVehicleListConfigService } from '../../services/vehicle-list-config.service';
import { SubSink } from 'subsink';
import { TranslateService } from '@ngx-translate/core';
import { ReplaySubject } from 'rxjs';
import { LibUserSetting } from 'sag-article-detail';

@Component({
    selector: 'sag-advance-vehicle-search-models-page',
    templateUrl: './advance-vehicle-search-models.component.html'
})
export class SagAdvanceVehicleSearchModelsComponent implements OnInit, OnDestroy {
    @Input() userSetting: LibUserSetting;
    @Output() navigate = new EventEmitter();

    make;
    breadcrumbs = [];

    popoverDelay = 0;

    beginYear: number;
    endYear: number;
    yearStep: number = 15;
    yearFilterList = [];
    yearFrom = '';

    quickSelectionList = [];
    quickSelectionArray = [];
    quickSelectionArray$ = new ReplaySubject<any>(1);
    quickSelectionColumns = 4;
    quickSelectionRows: number = 0;

    modelSeriesGroupListColumn = 2;
    modelSeriesGroupListLoopCount = new Array(this.modelSeriesGroupListColumn);
    modelSeriesGroupList = [];
    modelSeriesGroupArray = [];
    modelSeriesGroupArray$ = new ReplaySubject<any>(1);
    modelSeriesGroupRows: number = 1;
    marginOfmodelSeriesGroupItem: number = 30;
    heightOfmodelSeriesTitle: number = 40;
    heightOfmodelInGroup: number = 38;

    private subs = new SubSink();

    constructor(
        private searchService: ArticleSearchService,
        private router: Router,
        private config: SagVehicleListConfigService,
        private activatedRoute: ActivatedRoute,
        private translateService: TranslateService
    ) { }

    ngOnInit(): void {
        this.subs.sink = this.activatedRoute.queryParams.subscribe(({ make }) => {
            if (make) {
                this.make = JSON.parse(make);
                this.loadModelsData(this.make.id);
                this.updateBreadcrumbs();
            }
        });
        this.popoverDelay = this.userSetting && this.userSetting.mouseOverFlyoutDelay || 0;
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    filterByYear() {
        this.loadModelsData(this.make.id, this.yearFrom);
    }

    scrollToGroup(groupId) {
        let element = document.getElementById('group-' + groupId);
        let allTitleElement = document.getElementsByClassName('avs-models-group-title');
        if (element && allTitleElement) {
            Array.from(allTitleElement).forEach(item => item.classList.remove('active'));
            element.classList.add('active');
            element.scrollIntoView({
                behavior: 'smooth',
                block: 'center'
            });
        }
    }

    navigateToTypesList(model) {
        this.navigate.emit(model);
    }

    private updateModelSeriesGroupListLayout() {
        let theGapKeepLayoutInCaseHaveLessGroup = 5;
        let maxHeight = Math.floor(this.getHeightOfModelSeriesGroupList() / this.modelSeriesGroupListColumn) - theGapKeepLayoutInCaseHaveLessGroup;
        let tempHeight = 0;
        let groupCount = 0;
        this.modelSeriesGroupArray = [];

        this.modelSeriesGroupList.forEach(group => {
            tempHeight += this.getHeightOfOneGroup(group);

            if (!this.modelSeriesGroupArray[groupCount]) {
                this.modelSeriesGroupArray[groupCount] = [];
            }
            this.modelSeriesGroupArray[groupCount].push(group);

            if (!(tempHeight < maxHeight || groupCount === (this.modelSeriesGroupListColumn - 1))) {
                groupCount++;
                maxHeight = maxHeight * (groupCount + 1);
            }
        });
        this.modelSeriesGroupArray$.next(this.modelSeriesGroupArray);
    }

    private updateQuickSelectionLayout() {
        let groupCount = 0;
        let maxItems = Math.ceil(this.quickSelectionList.length /  this.quickSelectionColumns); 
        this.quickSelectionArray = [];

        this.quickSelectionList.forEach(item => {
            if (!this.quickSelectionArray[groupCount]) {
                this.quickSelectionArray[groupCount] = [];
            }
            this.quickSelectionArray[groupCount].push(item);

            if (this.quickSelectionArray[groupCount].length === maxItems) {
                groupCount++;
            }
        });
        this.quickSelectionArray$.next(this.quickSelectionArray);
    }

    private getHeightOfModelSeriesGroupList() {
        let height = 0;
        if (!this.modelSeriesGroupList && this.modelSeriesGroupList.length === 0) {
            return;
        }
        this.modelSeriesGroupList.forEach(group => {
            height += this.getHeightOfOneGroup(group);
        });
        // Remove last margin
        height -= this.marginOfmodelSeriesGroupItem;
        return height;
    }

    private getHeightOfOneGroup(group) {
        return (group.models.length * this.heightOfmodelInGroup) + this.heightOfmodelSeriesTitle + this.marginOfmodelSeriesGroupItem;
    }

    private loadModelsData(makeId, yearFrom = '') {
        this.config.spinner.start();
        let payload = {
            fuelType: null,
            makeCode: makeId,
            modelCode: null,
            typeCode: null,
            vehicleType: VEHICLE_CLASS.PC,
            yearFrom: yearFrom,
        };
        this.searchService.getAdvanceVehicleSearchModelData(payload).pipe(
            finalize(() => {
                this.initDataQuickSelection();
                this.config.spinner.stop();
            }),
            map((res: any) => {
                if (res && res.years && res.years.length > 0 && !yearFrom) {
                    this.initDataYearFilter(res.years);
                }
                if (res && res.models) {
                    this.initDataModelSeriesGroupList(res.models);
                    this.quickSelectionList = [];
                    Object.keys(res.models).map((key) => {
                        if (res.models.hasOwnProperty(key)) {
                            this.quickSelectionList.push(key.toString());
                        }
                    });
                }
            }),
        ).subscribe();
    }

    private isValidObj(obj) {
        if (!obj || obj === '0') {
            return false;
        }
        return true;
    }

    private formatYear(date) {
        if (!this.isValidObj(date)) {
            return '';
        }
        const year = date.toString().substring(0, 4);
        return `${year}`;
    }

    private formatDateWithMonthAndYear(date, dateSeparator = '/') {
        if (!this.isValidObj(date)) {
            return '';
        }
        const year = date.toString().substring(0, 4);
        const month = date.toString().substring(4, date.length);
        return `${('0' + month).slice(-2)}${dateSeparator}${year}`;
    }

    private initDataYearFilter(year) {
        let yearList = year.map(item => {
            return item.toString().substring(0, 4);
        })
        let distincYearList: Array<any> = [... new Set(yearList)];
        distincYearList = distincYearList.sort((a, b) => a.localeCompare(b));
        this.yearFilterList = distincYearList.map(year => {
            return { value: year, label: this.formatYear(year) }
        });
        this.yearFilterList.unshift({ value: '', label: this.translateService.instant('TYRE.ALL') });
    }

    private initDataQuickSelection() {
        this.quickSelectionList = [... new Set(this.quickSelectionList)];
        this.quickSelectionList.sort((a, b) => a.localeCompare(b));
        this.updateQuickSelectionLayout();
    }

    private initDataModelSeriesGroupList(models) {
        if (!models) {
            return;
        }
        this.modelSeriesGroupList = [];
        Object.keys(models).map((key) => {
            if (models.hasOwnProperty(key)) {
                this.modelSeriesGroupList.push({
                    series: key.toString(),
                    models: models[key].map(({ modelId, model, modelDateBegin, modelDateEnd, modelSeries, modelGen }) => {
                        let yearFormated = `${this.formatDateWithMonthAndYear(modelDateBegin)} - ${this.formatDateWithMonthAndYear(modelDateEnd)}`;
                        return { id: modelId, label: model, year: yearFormated, series: modelSeries, gen: modelGen };
                    })
                });
            }
        });
        this.modelSeriesGroupList.sort((a, b) => a.series.toString().localeCompare(b.series.toString()));
        this.updateModelSeriesGroupListLayout();
    }

    private updateBreadcrumbs() {
        if (this.breadcrumbs.length === 0) {
            this.breadcrumbs.push(
                { navigateTo: 'makes', text: 'ADVANCE_VEHICLE_SEARCH.MAKES' },
                { navigateTo: 'models', text: this.make.label }
            );
        }
    }
}