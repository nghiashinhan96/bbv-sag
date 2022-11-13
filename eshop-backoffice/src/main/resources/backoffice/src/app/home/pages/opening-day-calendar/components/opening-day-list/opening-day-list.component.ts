import { Component, OnInit, Input, Output, EventEmitter, ViewChild, TemplateRef, OnChanges, SimpleChanges } from '@angular/core';

import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { NgOption } from '@ng-select/ng-select';
import { SagTableColumn, SagTableControl } from 'sag-table';

import { OpeningDayModel } from '../../model/opening-day.model';
import { SortModel } from 'src/app/shared/models/sort.model';
import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

@Component({
    selector: 'backoffice-opening-day-list',
    templateUrl: './opening-day-list.component.html',
    styleUrls: ['./opening-day-list.component.scss']
})
export class OpeningDayListComponent implements OnInit, SagTableControl, OnChanges {
    // @ViewChild('fromDate', { static: true }) fromDate: any;
    // @ViewChild('exeptFromDate', { static: false }) exeptFromDate: any;

    @Input() openingDayList: OpeningDayModel[] = [];
    @Input() workingCodes: Array<any>;
    @Input() totalElements: number;
    @Input() itemsPerPage: number;
    @Input() resetFilterEvent = new Subject<boolean>();

    @Output() filterWorkingCode = new EventEmitter();
    @Output() filterTyping = new EventEmitter();
    @Output() sortEvent = new EventEmitter();
    @Output() pageEvent = new EventEmitter();
    @Output() delete = new EventEmitter();
    @Output() tableSearch = new EventEmitter<any>();

    typedCountry: string;
    typedAffiliate: string;
    typedBranch: string;
    typedAddressId: string;
    page = 1;
    isNotReload = true;

    private typeFilter$ = new Subject<{}>();

    columns = [];
    orderByDateDesc = true;
    orderByLandDesc: boolean = null;

    @ViewChild('colDate', { static: true }) colDate: TemplateRef<any>;
    @ViewChild('colWorkingDayCode', { static: true }) colWorkingDayCode: TemplateRef<any>;
    @ViewChild('colBranchCodes', { static: true }) colBranchCodes: TemplateRef<any>;
    @ViewChild('colExpWorkingCode', { static: true }) colExpWorkingCode: TemplateRef<any>;
    @ViewChild('colExpAddressIds', { static: true }) colExpAddressIds: TemplateRef<any>;

    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;


    constructor() { }

    ngOnInit() {
        this.typeFilter$.pipe(
            debounceTime(500),
            distinctUntilChanged()
        ).subscribe((data) => {
            this.filterTyping.emit(data);
        });

        this.resetFilterEvent.subscribe(isReset => {
            if (isReset) {
                this.isNotReload = false;
                this.resetFilter();
                setTimeout(() => {
                    this.page = 1;
                    this.isNotReload = true;
                }, 0);
            }
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.workingCodes && changes.workingCodes.currentValue.length > 0) {
            this.buildColumns();
        }
    }

    // onWorkingCodeSelect(option: NgOption, exception: boolean) {
    //     this.filterWorkingCode.emit({ option, exception });
    // }

    // filterWithUserInput() {
    //     this.typeFilter$.next({
    //         country: this.typedCountry,
    //         affiliate: this.typedAffiliate,
    //         branch: this.typedBranch,
    //         address: this.typedAddressId
    //     });
    // }

    deleteItem(openingDay: OpeningDayModel) {
        this.delete.next(openingDay);
    }

    // goToPage(page: number) {
    //     this.page = page;
    //     this.pageEvent.emit(this.page - 1);
    // }

    onSortWith(sortModel: SortModel) {
        this.sortEvent.emit(sortModel);
    }

    searchTableData($event): void {
        this.tableSearch.emit($event);
    }

    formatText(text: string): string {
        return text.replace(/,/g, ', ');
    }

    formatExpWorkingCode(text: string): string {
        return text ? `OPENING_DAY.${text}` : '';
    }

    sortByDate() {
        this.orderByDateDesc = !this.orderByDateDesc;
        this.onSortWith({
            field: 'SORT_COLUMN_DATE',
            direction: this.orderByDateDesc ? 1 : 0
        });
        this.orderByLandDesc = null;
    }

    sortByLand() {
        if (this.orderByLandDesc !== null) {
            this.orderByLandDesc = !this.orderByLandDesc;
            this.onSortWith({
                field: 'SORT_COLUMN_LAND',
                direction: this.orderByLandDesc ? 1 : 0
            });
        } else {
            this.orderByLandDesc = false;
            this.onSortWith({
                field: 'SORT_COLUMN_LAND',
                direction: this.orderByLandDesc ? 1 : 0
            });
        }
        this.orderByDateDesc = null;
    }

    private resetFilter(): void {
        this.typedCountry = EMPTY_STRING;
        this.typedAffiliate = EMPTY_STRING;
        this.typedBranch = EMPTY_STRING;
        this.typedAddressId = EMPTY_STRING;
    }

    private buildColumns() {
        this.columns = [
            {
                id: 'date',
                i18n: 'OPENING_DAY.OPENING_DAY_COLUMNS.DATE',
                sortable: true,
                cellTemplate: this.colDate,
                width: '9%',
            },
            {
                id: 'workingDayCode',
                i18n: 'OPENING_DAY.OPENING_DAY_COLUMNS.DAY_TYPE',
                filterable: true,
                type: 'select',
                selectSource: this.workingCodes,
                defaultValue: this.workingCodes[0],
                cellTemplate: this.colWorkingDayCode,
                width: '13%',
            },
            {
                id: 'countryName',
                i18n: 'OPENING_DAY.OPENING_DAY_COLUMNS.LAND',
                filterable: true,
                sortable: true,
                width: '13%',
            },
            {
                id: 'expAffiliate',
                i18n: '',
                cellClass: 'text-right',
                filterable: true,
                width: '13%',
            },
            {
                id: 'expBranchInfo',
                i18n: '',
                cellClass: 'text-right',
                cellTemplate: this.colBranchCodes,
                filterable: true,
                width: '13%',
            },
            {
                id: 'expWorkingDayCode',
                i18n: '',
                cellClass: 'text-right',
                filterable: true,
                type: 'select',
                selectSource: this.workingCodes,
                defaultValue: this.workingCodes[0],
                cellTemplate: this.colExpWorkingCode,
                width: '13%',
            },
            {
                id: 'expDeliveryAddressId',
                i18n: '',
                cellClass: 'text-right',
                cellTemplate: this.colExpAddressIds,
                filterable: true,
                width: '19%',
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
            },
        ] as SagTableColumn[];
    }
}
