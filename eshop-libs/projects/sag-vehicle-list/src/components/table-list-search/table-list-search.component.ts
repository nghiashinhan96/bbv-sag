import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SagCommonSortObj } from 'sag-common';
import { SubSink } from 'subsink';
import { VehicleListService } from '../../services/vehicle-list.service';
import { SCROLL_EVENT_TYPE } from '../../enums/vehicle-list.enum';

@Component({
    selector: 'sag-table-list-search',
    templateUrl: './table-list-search.component.html'
})
export class SagTableListSearchComponent implements OnInit {
    @Input() columns;
    @Input() sort: SagCommonSortObj;
    @Input() searchModel;
    @Input() result;
    @Input() isFetching;
    @Input() queryParams;

    @Output() sortDataEvent = new EventEmitter();
    @Output() filterFullNameEvent = new EventEmitter();
    @Output() optionSelectedEvent = new EventEmitter();
    @Output() fetchMoreVehiclesEvent = new EventEmitter();
    @Output() filteringDataEvent = new EventEmitter();
    @Output() navigateToListPartEvent = new EventEmitter();

    SCROLL_EVENT_TYPE = SCROLL_EVENT_TYPE;

    private subs = new SubSink();

    @ViewChild('scroller', { static: false }) scroller: ElementRef;

    constructor(
        private vehicleListService: VehicleListService,
        private router: Router,
        private activatedRoute: ActivatedRoute) {
        this.subs.sink = this.vehicleListService.scrollderObservable.subscribe(res => {
            if (res) {
                this.handleScrollEvent(res.type);
            }
        });
    }

    ngOnInit(): void {
        if (this.columns) {
            this.columns.sort((a, b) => a.order - b.order);
        }
    }

    sortData(sortObject: SagCommonSortObj) {
        this.sortDataEvent.emit(sortObject);
    }

    filterFullName() {
        this.filterFullNameEvent.emit();
    }

    optionSelected() {
        this.optionSelectedEvent.emit();
    }

    fetchMoreVehicles(event) {
        this.fetchMoreVehiclesEvent.emit(event);
    }

    filteringData(filedName, val) {
        this.filteringDataEvent.emit({ filedName, val });
    }

    handleScrollEvent(type, offset = 0) {
        if (!type || !this.scroller) {
            return;
        }

        if (type === SCROLL_EVENT_TYPE.FETCH) {
            this.fetchMoreVehicles({ currentTarget: this.scroller });
        } else {
            this.scroller.nativeElement.scrollTop = offset;
        }
    }

    navigateToListPart(item) {
        if (!item || !this.queryParams) {
            return;
        }
        if (this.queryParams && this.queryParams.isAdvanceVehicleSearch) {
            let yearFrom = `${this.vehicleListService.formatDateWithMonthAndYear(item.vehicle_built_year_from)}`;
            let yearTill = `${this.vehicleListService.formatDateWithMonthAndYear(item.vehicle_built_year_till)}`;
            let result = this.queryParams.keywords + ` [${yearFrom} - ${yearTill}] ${item.vehicle_fuel_type} ${item.vehicle_name} ${item.vehicle_power_kw} kW ${item.vehicle_engine_code}`;
            this.queryParams.keywords = encodeURIComponent(result);
            this.navigateToListPartEvent.emit({item: item});
        }
        const queryParams = this.queryParams;
        this.router.navigate(['/vehicle/' + item.vehid], { relativeTo: this.activatedRoute, queryParams });
    }
}