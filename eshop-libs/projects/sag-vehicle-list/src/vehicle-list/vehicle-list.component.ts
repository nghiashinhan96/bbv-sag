import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { VehicleRequestModel } from '../models/vehicle-request.model';
import { Router, ActivatedRoute } from '@angular/router';
import { VehicleRequestSortModel } from '../models/vehicle-request-sort.model';
import { VehicleListService } from '../services/vehicle-list.service';
import { finalize } from 'rxjs/operators';
import { SagCommonSortObj } from 'sag-common';
import { SagVehicleListConfigService } from '../services/vehicle-list-config.service';
import { SubSink } from 'subsink';
import { SagVehicleSearchTableColumn } from '../models/list-table-column.model';
import { KEY_RAW } from '../enums/vehicle-list.enum';
import { VEHICLE_LIST_COLUMNS } from '../vehicle-list.const';

const DEFAULT_DATE_SEPARATOR = '/';

@Component({
    selector: 'sag-vehicle-list',
    templateUrl: './vehicle-list.component.html',
    styleUrls: ['./vehicle-list.component.scss']
})
export class SagLibVehicleListComponent implements OnInit, OnDestroy {
    @Output() navigate = new EventEmitter();

    searchModel: VehicleRequestModel = new VehicleRequestModel();
    vehicles = [];
    total: number;
    errorMessage = false;
    columns: SagVehicleSearchTableColumn[] = VEHICLE_LIST_COLUMNS;
    isShowcustomerInformation: boolean;
    isFetching: boolean;
    isInvalidFilterYear: boolean;
    sort: SagCommonSortObj;
    queryParams;
    KEY_RAW = KEY_RAW;

    private defaultSort: SagCommonSortObj = {
        field: '',
        direction: '',
        force: true
    };
    private epsilonHeight = 100;
    private currentPageNo: number;
    private orginalSearch: VehicleRequestModel = null;
    private dateFormatSeparator = DEFAULT_DATE_SEPARATOR;
    private searchTimer;
    private subs = new SubSink();


    constructor(
        public router: Router,
        private vehicleService: VehicleListService,
        private route: ActivatedRoute,
        private config: SagVehicleListConfigService
    ) {
    }

    ngOnInit() {
        this.sort = { ...this.defaultSort };
        this.subs.sink = this.route.queryParams.subscribe(({ search }) => {
            if (!search) {
                this.errorMessage = true;
                return;
            }

            try {
                this.orginalSearch = new VehicleRequestModel(JSON.parse(search) as VehicleRequestModel);
                this.orginalSearch.aggregation = true;
                this.searchModel = new VehicleRequestModel(this.orginalSearch);

                // tslint:disable-next-line: max-line-length
                this.queryParams = { keywords: encodeURIComponent(`${this.searchModel.term.vehicleDesc || ''} ${this.searchModel.filtering.built_year_month_from || ''}`) };

                if (this.sort && this.sort.field) {
                    this.searchModel.sort = {
                        [this.sort.field]: this.sort.direction.toUpperCase()
                    } as VehicleRequestSortModel;
                }
                this.dateFormatSeparator = this.vehicleService.getDateSeparator(this.searchModel.filtering.built_year_month_from || '');
                this.executeSearch();
            } catch (ex) {
                // wrong params
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    fetchMoreVehicles(event) {
        if ((this.total <= this.vehicles.length) || this.isFetching) {
            return;
        }
        const container = event.currentTarget;
        if (container.scrollHeight <= (container.clientHeight + container.scrollTop + this.epsilonHeight) && !this.isFetching) {
            this.isFetching = true;
            this.currentPageNo += 1;
            this.doSearch();
        }
    }

    trackByVehicleId(index, vehicle) {
        return vehicle.vehid;
    }

    clearFilter() {
        if (!this.orginalSearch) {
            // no search param
            return;
        }
        this.searchModel = {
            ...this.orginalSearch,
            term: { ...this.orginalSearch.term },
            filtering: { ...this.orginalSearch.filtering }
        };
        this.sort = { ...this.defaultSort };
        if (this.sort && this.sort.field) {
            this.searchModel.sort = {
                [this.sort.field]: this.sort.direction.toUpperCase()
            } as VehicleRequestSortModel;
        }
        this.dateFormatSeparator = this.vehicleService.getDateSeparator(this.searchModel.filtering.built_year_month_from || '');
        this.executeSearch();
    }

    // navigate to part list page for more detail after doing a search
    navigateToPartList(vehicle) {
        this.navigate.emit(vehicle.vehid);
    }

    optionSelectedEvent() {
        this.executeSearch();
    }

    filteringData(data) {
        if (!data) {
            return;
        }
        this.searchModel.filtering[data.filedName] = String(data.val);
        this.executeSearch();
    }

    filterFullName() {
        if (this.searchTimer) {
            clearTimeout(this.searchTimer);
        }
        this.searchTimer = setTimeout(() => {
            this.executeSearch();
        }, 1000);
    }

    sortData(sortObject: SagCommonSortObj) {
        if (sortObject.field) {
            this.searchModel.sort = {
                [sortObject.field]: sortObject.direction.toUpperCase()
            } as VehicleRequestSortModel;
            this.executeSearch();
        }
    }

    private executeSearch() {
        this.vehicleService.scrollerSubject.next({ type: 'scrollTop' });
        this.currentPageNo = 0;
        this.config.spinner.start();
        this.doSearch();
    }

    private doSearch() {
        this.errorMessage = false;
        const queryParams = {
            page: this.currentPageNo,
            size: 30
        };
        this.vehicleService.searchVehicles(this.searchModel, queryParams)
            .pipe(finalize(() => this.config.spinner.stop()))
            .subscribe(({ vehicles, aggregations }) => {
                if (vehicles) {
                    const data = vehicles.content || [];
                    data.forEach(veh => {
                        const yearFrom = this.vehicleService.formatDateWithMonthAndYear(veh.vehicle_built_year_from, this.dateFormatSeparator);
                        const yearTo = this.vehicleService.formatDateWithMonthAndYear(veh.vehicle_built_year_till, this.dateFormatSeparator);
                        veh.displayYear = `${yearFrom} - ${yearTo}`;
                    });
                    if (this.isFetching) {
                        this.isFetching = false;
                        this.vehicles = [...this.vehicles, ...data];
                    } else {
                        this.vehicles = [...data];
                    }
                    this.handleTooltip();
                    this.total = vehicles.totalElements || 0;
                    this.vehicleService.buildFiltering(aggregations, this.columns);
                    // try to fetch in case there is still have avalable space
                    setTimeout(() => {
                        this.vehicleService.scrollerSubject.next({ type: 'fetch' });
                    });
                }
            }, () => {
                this.errorMessage = true;
                this.vehicles = [];
            });
    }

    private handleTooltip() {
        setTimeout(() => {
            // let tds = $('table td.tcol').toArray();
            // tds.forEach(e => {
            //   if (e.offsetWidth < e.scrollWidth) {
            //     e.title = e.innerText;
            //   }
            // });
        });
    }
}
