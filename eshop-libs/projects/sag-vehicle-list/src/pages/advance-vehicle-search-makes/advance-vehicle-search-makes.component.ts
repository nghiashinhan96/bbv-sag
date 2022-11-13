import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { ArticleSearchService } from 'sag-article-search';
import { VEHICLE_CLASS } from 'sag-common';
import { SagVehicleListConfigService } from '../../services/vehicle-list-config.service';

@Component({
    selector: 'sag-advance-vehicle-search-makes-page',
    templateUrl: './advance-vehicle-search-makes.component.html'
})
export class SagAdvanceVehicleSearchMakesComponent implements OnInit {
    @Output() navigate = new EventEmitter();

    makes$;

    topMakes = [];
    topMakesArray = [];
    topMakesArray$ = new ReplaySubject<any>(1);

    allMakes = [];
    allMakesArray = [];
    allMakesArray$ = new ReplaySubject<any>(1);

    columns: number = 4;

    breadcrumbs = [
        { navigateTo: 'makes', text: 'ADVANCE_VEHICLE_SEARCH.MAKES' }
    ];

    constructor(
        private searchService: ArticleSearchService,
        private config: SagVehicleListConfigService
    ) { }

    ngOnInit(): void {
        this.loadMakesData();
    }

    navigateToModelsList(make) {
        this.navigate.emit(make);
    }

    private loadMakesData() {
        this.config.spinner.start();
        const { vehicleType, yearFrom } = { vehicleType: VEHICLE_CLASS.PC, yearFrom: '' };
        this.searchService.getMakeModelTypeData({ vehicleType, yearFrom }).pipe(
            finalize(() => {
                this.updateTopMakesLayout();
                this.updateAllMakesLayout();
                this.config.spinner.stop();
            }),
            map((res: any) => {
                (res.MAKE || []).map(({ makeId, make, top }) => {
                    return { id: makeId, label: make, top };
                }).forEach((item: any) => {
                    if (item.top) {
                        this.topMakes.push(item);
                    }
                    this.allMakes.push(item);
                });
            })).subscribe();
    }

    private updateTopMakesLayout() {
        let groupCount = 0;
        let maxItems = Math.ceil(this.topMakes.length / this.columns);
        this.topMakesArray = [];

        this.topMakes.sort((a, b) => a.label.toString().localeCompare(b.label.toString()));
        this.topMakes.forEach(item => {
            if (!this.topMakesArray[groupCount]) {
                this.topMakesArray[groupCount] = [];
            }
            this.topMakesArray[groupCount].push(item);

            if (this.topMakesArray[groupCount].length === maxItems) {
                groupCount++;
            }
        });
        this.topMakesArray$.next(this.topMakesArray);
    }

    private updateAllMakesLayout() {
        let groupCount = 0;
        let maxItems = Math.ceil(this.allMakes.length / this.columns);
        this.allMakesArray = [];

        this.allMakes.sort((a, b) => a.label.toString().localeCompare(b.label.toString()));
        this.allMakes.forEach(item => {
            if (!this.allMakesArray[groupCount]) {
                this.allMakesArray[groupCount] = [];
            }
            this.allMakesArray[groupCount].push(item);

            if (this.allMakesArray[groupCount].length === maxItems) {
                groupCount++;
            }
        });
        this.allMakesArray$.next(this.allMakesArray);
    }
}