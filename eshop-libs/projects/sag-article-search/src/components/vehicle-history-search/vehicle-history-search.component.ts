import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { of } from 'rxjs/internal/observable/of';
import { ArticleSearchService } from '../../services/article-search.service';
import { finalize, map } from 'rxjs/operators';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import { SEARCH_MODE } from 'sag-article-list';
import { SearchTermUtil } from '../../utils/search-term.util';
import { VehicleSearchHistory } from '../../models/vehicle-search-history.model';
@Component({
    selector: 'sag-search-vehicle-history',
    templateUrl: './vehicle-history-search.component.html',
    styleUrls: ['./vehicle-history-search.component.scss']
})
export class SagSearchVehicleHistoryComponent implements OnInit, OnChanges {
    @Input() custNr: string;
    @Input() vehicleClass?: string;
    @Output() selectedVehicleHistoryEmitter = new EventEmitter<VehicleSearchHistory>();
    vehicleHistorySearch$: Observable<any[]>;
    dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;

    constructor(private searchService: ArticleSearchService, private config: ArticleSearchConfigService) { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.custNr && !changes.custNr.firstChange) {
            this.vehicleHistorySearch$ = of(null);
            setTimeout(() => {
                this.vehicleHistorySearch$ = this.loadHistory();
            });
        }
    }

    ngOnInit() {
        this.vehicleHistorySearch$ = this.loadHistory();
    }

    viewVehicle(vehicle: VehicleSearchHistory) {
        if (vehicle) {
            this.selectedVehicleHistoryEmitter.emit(vehicle);
        }
    }

    private loadHistory() {
        const spinner = this.config.spinner.start('sag-vehicle-history-search .list-group');
        return this.searchService.getVehicleHistorySearch(this.vehicleClass)
            .pipe(
                finalize(() => this.config.spinner.stop(spinner))
            );
    }
}
