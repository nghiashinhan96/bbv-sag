import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, TemplateRef, ViewChild } from '@angular/core';
import { NgOption } from '@ng-select/ng-select';
import { EMPTY_STR } from 'angular-mydatepicker';
import { Subject } from 'rxjs';
import { SagTableColumn, SagTableControl } from 'sag-table';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { OpeningDayModel } from '../../models/opening-day.model';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-opening-day-list',
    templateUrl: './opening-day-list.component.html',
    styleUrls: ['./opening-day-list.component.scss']
})
export class OpeningDayListComponent implements OnInit, OnChanges, OnDestroy {
    @ViewChild('fromDate', { static: true }) fromDate: any;
    @ViewChild('exeptFromDate', { static: false }) exeptFromDate: any;

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
    @Output() update = new EventEmitter();
    @Output() tableSearch = new EventEmitter<any>();

    typedCountry: string;
    typedAffiliate: string;
    typedBranch: string;
    typedAddressId: string;
    page = 1;
    isNotReload = true;

    private typeFilter$ = new Subject<{}>();
    private subs = new SubSink();

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
        this.subs.sink = this.typeFilter$.pipe(
            debounceTime(500),
            distinctUntilChanged()
        ).subscribe((data) => {
            this.filterTyping.emit(data);
        });

        this.subs.sink = this.resetFilterEvent.subscribe(isReset => {
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

    ngOnDestroy(): void {
       this.subs.unsubscribe();
    }

    onWorkingCodeSelect(option: NgOption, exception: boolean) {
        this.filterWorkingCode.emit({ option, exception });
    }

    filterWithUserInput() {
        this.typeFilter$.next({
            country: this.typedCountry,
            affiliate: this.typedAffiliate,
            branch: this.typedBranch,
            address: this.typedAddressId
        });
    }

    deleteItem(openingDay: OpeningDayModel) {
        this.delete.next(openingDay);
    }

    goToPage(page: number) {
        this.page = page;
        this.pageEvent.emit(this.page - 1);
    }

    onSortWith(sortModel) {
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

    updateItem(id) {
        this.update.emit(id);
    }

    private resetFilter(): void {
        this.typedCountry = EMPTY_STR;
        this.typedAffiliate = EMPTY_STR;
        this.typedBranch = EMPTY_STR;
        this.typedAddressId = EMPTY_STR;
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
                id: 'expBranchInfo',
                i18n: '',
                cellClass: 'text-right',
                cellTemplate: this.colBranchCodes,
                filterable: true,
                width: '26%',
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
                width: '26%',
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
