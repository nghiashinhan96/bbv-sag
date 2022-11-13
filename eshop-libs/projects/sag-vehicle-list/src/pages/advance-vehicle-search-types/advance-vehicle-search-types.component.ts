import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { SagCommonSortObj, SAG_COMMON_ASC } from 'sag-common';
import { SubSink } from 'subsink';
import { VehicleListService } from '../../services/vehicle-list.service';
import { SagVehicleListConfigService } from '../../services/vehicle-list-config.service';
import { VehicleRequestSortModel } from '../../models/vehicle-request-sort.model';
import { VehicleRequestModel } from '../../models/vehicle-request.model';
import { SagVehicleSearchTableColumn } from '../../models/list-table-column.model';
import { KEY_RAW } from '../../enums/vehicle-list.enum';
import { ADVANCE_VEHICLE_SEARCH_TYPE_LIST_COLUMNS } from '../../vehicle-list.const';

const DEFAULT_DATE_SEPARATOR = '/';

@Component({
    selector: 'sag-advance-vehicle-search-types-page',
    templateUrl: './advance-vehicle-search-types.component.html'
})
export class SagAdvanceVehicleSearchTypesComponent implements OnInit {
    @Output() navigateToPartListEvent = new EventEmitter();

    searchModel: VehicleRequestModel = new VehicleRequestModel();
    vehicles = [];
    total: number;
    errorMessage = false;
    modelId;
    make;
    model;
    breadcrumbs = [];
    isShowcustomerInformation: boolean;
    isFetching: boolean;
    isInvalidFilterYear: boolean;
    sort: SagCommonSortObj;
    queryParams;
    columns: SagVehicleSearchTableColumn[] = ADVANCE_VEHICLE_SEARCH_TYPE_LIST_COLUMNS;
    KEY_RAW = KEY_RAW;
    SAG_COMMON_ASC = SAG_COMMON_ASC;

    private defaultSort: SagCommonSortObj = { field: '', direction: '', force: true };
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
        this.subs.sink = this.route.queryParams.subscribe(({ search, make, model }) => {
            if (!search || !make || !model) {
                this.errorMessage = true;
                return;
            }

            try {
                this.make = JSON.parse(make);
                this.model = JSON.parse(model);
                this.updateBreadcrumbs();
            } catch (ex) {
                // wrong params
            }

            try {
                this.orginalSearch = new VehicleRequestModel(JSON.parse(search) as VehicleRequestModel);
                this.orginalSearch.aggregation = true;
                this.searchModel = new VehicleRequestModel(this.orginalSearch);
                this.searchModel.filtering.vehicle_advance_name = '';
                this.searchModel.filtering.capacity_cc_tech = '';

                // Set default list sort by 'type' ascending
                this.searchModel.sort.vehicle_advance_name = this.SAG_COMMON_ASC;

                // Update keyword use in history search
                let model = '';
                if (this.model) {
                    model = this.model.label;
                }

                let breadcrumbs = JSON.stringify(this.breadcrumbs);
                this.queryParams = { keywords: `${model}`, breadcrumbs, isAdvanceVehicleSearch: true };

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

    // navigate to part list page for more detail after doing a search
    navigateToPartList(data) {
        this.navigateToPartListEvent.emit({brand: this.make, model: this.model, type: data.item});
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
        this.vehicleService.searchVehiclesAdvance(this.searchModel, queryParams)
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

    private updateBreadcrumbs() {
        if (this.breadcrumbs.length === 0) {
            this.breadcrumbs.push({ navigateTo: 'makes', text: 'ADVANCE_VEHICLE_SEARCH.MAKES' });
            this.breadcrumbs.push({ navigateTo: 'models', text: this.make.label });
            this.breadcrumbs.push({ navigateTo: 'types', text: this.model.label });
        }
    }
}