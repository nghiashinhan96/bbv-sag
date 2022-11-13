import {
    Component,
    OnInit,
    Input,
    Output,
    EventEmitter,
    AfterContentInit,
    OnChanges,
    SimpleChanges,
    ViewChild,
    TemplateRef
} from '@angular/core';

import uuid from 'uuid/v4';

import { FilterPipe } from 'ngx-filter-pipe';
import { TranslateService } from '@ngx-translate/core';
import { IAngularMyDpOptions } from 'angular-mydatepicker';
import { SagCommonSortObj } from 'sag-common';

import { SagTableConfigService } from '../services/sag-table.config.service';
import { SagTableColumn } from '../models/table-column.model';
import { TablePage } from '../models/table-page.model';
import { SagTableRequestModel } from '../models/table-request.model';
import { SagTableResponseModel } from '../models/table-respose.model';

const ONLINE = 'online';
const OFFLINE = 'offline';
@Component({
    selector: 'sag-table',
    templateUrl: './table.component.html',
    styleUrls: ['table.component.scss']
})
export class SagTableComponent implements OnInit, OnChanges, AfterContentInit {
    @Input() columns: SagTableColumn[] = [];
    @Input() rows: any[] = null;
    @Input() mode: 'online' | 'offline' = 'offline';
    @Input() sort: SagCommonSortObj;
    @Input() rowHeight: string;
    @Input() firstlySearch = true;
    @Input() notFoundText = 'SEARCH.ERROR_MESSAGE.VEHICLE_NOT_FOUND';
    @Input() selectable = false;
    @Input() searchModel: any = {};
    @Input() page = new TablePage();
    @Input() isHover = true;
    @Input() showNotFoundOnFirstLoad = true;
    @Input() isStriped = true;
    @Input() useSpinner = true;

    @Output() selectRow = new EventEmitter();
    @Output() searchData = new EventEmitter();

    defaultDateFormat = 'dd.MM.yyyy';
    myDateRangePickerOptions: IAngularMyDpOptions;
    locale: string;
    tableId = '';
    isChanged = false;

    private textSearchTimer;
    private offlineData: any;

    @ViewChild('dateRef', { static: true }) dateRef: TemplateRef<any>;
    @ViewChild('currencyRef', { static: true }) currencyRef: TemplateRef<any>;
    constructor(
        private filter: FilterPipe,
        private translateService: TranslateService,
        private config: SagTableConfigService
    ) {

    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.rows && !changes.rows.firstChange && this.mode === OFFLINE) {
            this.offlineData = [...changes.rows.currentValue];
            this.page.totalItems = this.offlineData.length;
            this.isChanged = true;
        }
        if (changes.searchModel && !changes.searchModel.firstChange) {
            this.fireSearchData();
        }

        if (changes.columns && !changes.columns.firstChange) {
            this.initTable();
        }
    }

    ngOnInit(): void {
        if (this.rows && this.rows.length > 0 && this.mode === OFFLINE) {
            this.offlineData = [...this.rows];
            this.page.totalItems = this.rows.length;
        }
        this.locale = this.translateService.currentLang;
        this.initTable();
        this.getDateRangeOptions();
        this.tableId = uuid();
    }
    private initTable() {
        this.columns.forEach(col => {
            switch (col.type) {
                case 'date':
                    col.cellTemplate = this.dateRef;
                    break;
                case 'currency':
                    col.cellClass = 'align-middle text-right';
                    col.class = 'text-right';
                    col.cellTemplate = this.currencyRef;
                    col.currencyOptions = col.currencyOptions || {};
                    break;
            }
            if (col.defaultValue !== undefined) {
                this.searchModel[col.id] = col.defaultValue;
            }
        });
    }

    onDateChange(col, data) {
        this.searchModel[col.id] = data;
        this.filterData();
    }

    ngAfterContentInit(): void {
        if (this.firstlySearch) {
            this.fireSearchData();
        }
    }

    onSelectRow(row) {
        if (this.selectable) {
            this.selectRow.emit(row);
        }
    }

    sortData(sortObject: SagCommonSortObj) {
        if (this.mode === ONLINE) {
            this.fireSearchData();
        } else {
            const col = this.columns.find(column => column.id === sortObject.field);
            const direction = sortObject.direction === 'asc' ? -1 : 1;
            const key = col.sortKey || col.id;
            this.rows.sort((rowX, rowY) => {
                let a = rowX[key];
                let b = rowY[key];
                if (Array.isArray(a) && Array.isArray(b)) {
                    a = (a || []).join();
                    b = (b || []).join();
                }
                if (typeof a === 'string' && typeof b === 'string') {
                    a = (a || '').toLowerCase();
                    b = (b || '').toLowerCase();
                }
                return (a >= b ? 1 : -1) * direction;
            });
        }
    }

    filterData(searchModel = null) {
        const filterValue = searchModel ? searchModel : this.searchModel;

        this.page.currentPage = 1;
        if (this.mode === ONLINE) {
            this.fireSearchData();
        } else {
            this.rows = this.filter.transform(this.offlineData, filterValue);
            this.page.totalItems = this.rows.length;
        }
    }

    onTextInputChange($event) {
        if (this.textSearchTimer) {
            clearTimeout(this.textSearchTimer);
        }
        this.textSearchTimer = setTimeout(() => {
            this.filterData();
        }, 1000);
    }

    pageChanged(currentPage: number) {
        this.page.currentPage = currentPage;
        if (this.mode === ONLINE) {
            this.fireSearchData();
        }
    }


    private fireSearchData() {
        let spinner;
        if (this.useSpinner) {
            spinner = this.config.spinner.start('sag-lib-table .table-container');
        }
        this.searchData.emit({
            request: {
                sort: { ...this.sort },
                filter: { ...this.searchModel },
                page: this.page.currentPage,
            } as SagTableRequestModel,
            callback: (res: SagTableResponseModel) => {
                if (res) {
                    this.rows = [...res.rows];
                    this.page.totalItems = res.totalItems;
                    if (res.itemsPerPage) {
                        this.page.itemsPerPage = res.itemsPerPage;
                    }
                    if (res.page) {
                        this.page.currentPage = res.page;
                    }
                }
                if (spinner) {
                    setTimeout(() => {
                        this.config.spinner.stop(spinner);
                    });
                }
            }
        });
    }

    private getDateRangeOptions() {
        this.myDateRangePickerOptions = {
            dateRange: true,
            dateFormat: 'dd.mm.yyyy',
            markCurrentDay: true,
            showFooterToday: true,
            focusInputOnDateSelect: false
        };
    }
}
