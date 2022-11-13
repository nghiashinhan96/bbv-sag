import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { ArticleSearchService } from '../../services/article-search.service';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { VEHICLE_CLASS } from 'sag-common';
import { finalize, map } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
    selector: 'sag-advance-search-vehicle',
    templateUrl: './advance-search-vehicle.component.html'
})
export class SagAdvanceSearchVehicleComponent implements OnInit {
    @Output() navigateToModelPageEventEmitter = new EventEmitter();

    makes = [];
    makesArray = [];
    makesArray$ = new ReplaySubject<any>(1);

    columns: number = 3; // number of columns make

    constructor(
        private searchService: ArticleSearchService,
        private config: ArticleSearchConfigService,
        private router: Router,
    ) { }

    ngOnInit() {
        this.loadData();
    }

    private loadData() {
        const spinner = this.config.spinner.start('.advance-vehicle-search');
        const { vehicleType, yearFrom } = { vehicleType: VEHICLE_CLASS.PC, yearFrom: '' };
        this.searchService.getMakeModelTypeData({ vehicleType, yearFrom }).pipe(
            finalize(() => {
                this.updateLayout();
                this.config.spinner.stop(spinner);
            }),
            map((res: any) => {
                (res.MAKE || []).map(({ makeId, make, top }) => {
                    return { id: makeId, label: make, top };
                }).forEach((item: any) => {
                    if (item.top) {
                        this.makes.push(item);
                    }
                });
            })).subscribe();
    }

    private updateLayout() {
        let groupCount = 0;
        let maxItems = 5;
        this.makesArray = [];
        this.makes.sort((a, b) => a.label.toString().localeCompare(b.label.toString()));
        this.makes.forEach(item => {
            if (groupCount < this.columns) {
                if (!this.makesArray[groupCount]) {
                    this.makesArray[groupCount] = [];
                }
                this.makesArray[groupCount].push(item);
    
                if (this.makesArray[groupCount].length === maxItems) {
                    groupCount++;
                }
            }
        });
        this.makesArray$.next(this.makesArray);
    }

    navigateToMakesPage() {
        this.router.navigate(['advance-vehicle-search', 'makes']);
    }

    navigateToModelsPage(make) {
        this.navigateToModelPageEventEmitter.emit(make);
    }
}
