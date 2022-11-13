import { Component, Input, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { SAG_COMMON_DATETIME_FORMAT, VEHICLE_CLASS } from 'sag-common';
import { TranslateService } from '@ngx-translate/core';
import { IMyDateModel } from 'angular-mydatepicker';
import * as moment from 'moment';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';

import {
    SagTableColumn
} from 'sag-table';

import { Constant } from 'src/app/core/conts/app.constant';
import { DATE_FILTER } from 'src/app/core/enums/date-filter.enum';
import { DateUtil } from 'src/app/core/utils/date.util';
import { ALL } from 'src/app/settings/settings.constant';
import { HistorySearchRequest } from '../../../../home/models/vehicle-history-search-request.model';
import { VehicleSearchService } from '../../../../home/service/vehicle-search.service';
import { SEARCH_MODE } from 'sag-article-list';
import { ArticleSearchService, ARTICLE_FILTER_MODE, SearchTermUtil, VehicleSearchHistory } from 'sag-article-search';
import { UserService } from 'src/app/core/services/user.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { catchError, finalize } from 'rxjs/operators';
import { of } from 'rxjs';
import { get } from 'lodash';
import { HISTORY_SEARCH_MODE } from 'src/app/home/enums/search-history.enums';

@Component({
    selector: 'connect-search-history-modal',
    templateUrl: './search-history-modal.component.html',
    styleUrls: ['./search-history-modal.component.scss']
})
export class SearchHistoryModalComponent implements OnInit, OnDestroy {

    @ViewChild('nameRef', { static: true }) nameRef: TemplateRef<any>;
    @ViewChild('articleSearchMethod', { static: true }) articleSearchMethod: TemplateRef<any>;
    @ViewChild('articleSearchTermDisplay', { static: true }) articleSearchTermDisplay: TemplateRef<any>;
    @ViewChild('vehicleSearchTerm', { static: true }) vehicleSearchTerm: TemplateRef<any>;
    @ViewChild('vehicleName', { static: true }) vehicleName: TemplateRef<any>;

    @Input() mode = HISTORY_SEARCH_MODE.VEHICLE;
    @Input() title: string;
    @Input() vehicleClass?: string;

    private readonly INIT_PAGE = 0;
    historyRequest: HistorySearchRequest;
    columns: SagTableColumn[];
    tableCallback = null;
    tableRequest = null;
    notFoundMessage = '';

    dateFromOption = {
        ...DateUtil.getCommonSetting(),
        disableSince: DateUtil.buildDataDatePickerMoment(moment().add(1, 'day')).singleDate.date
    };
    dateToOption = {
        ...DateUtil.getCommonSetting(),
        disableSince: DateUtil.buildDataDatePickerMoment(moment().add(1, 'day')).singleDate.date
    };

    dateRangeOptions = this.getDateRangeSelect();
    filterModeOptions = this.getFilterModeOptions();
    isShownDatePicker = false;
    isUsedDateRangePicker = false;
    isUsedDatePicker = true;
    filterForm: FormGroup;
    locale = '';
    subs = new SubSink();

    private readonly dateFormat = 'YYYY-MM-DD';
    private readonly C4S_MODE = 'C4S';
    readonly C4C_MODE = 'C4C';
    private shouldFilterByMotorbike = true;
    readonly VEHICLE_CLASS = VEHICLE_CLASS;

    constructor(
        public bsModalRef: BsModalRef,
        public translateService: TranslateService,
        private fb: FormBuilder,
        private searchService: VehicleSearchService,
        private userService: UserService,
        private articleSearchService: ArticleSearchService
    ) {
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.initForm();
        this.buildColumns();
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    private initForm() {
        const currentDate = moment();

        const dateToPicker = DateUtil.buildDataDatePickerMoment(currentDate);

        if (this.mode === HISTORY_SEARCH_MODE.VEHICLE) {
            this.notFoundMessage = 'SEARCH.NO_VEHICLE_SEARCH_FOUND';
        } else {
            this.notFoundMessage = 'SEARCH.NO_ARTICLE_SEARCH_FOUND';
        }

        this.filterForm = this.fb.group({
            dateFrom: null,
            dateTo: null,
            datePicker: dateToPicker,
            dateRangeSearch: DATE_FILTER.ALL,
            filterMode: this.filterModeOptions[1].id
        });

        this.subs.sink = this.filterMode.valueChanges.subscribe(value => {
            this.searchHistory(this.INIT_PAGE);
        })

        this.subs.sink = this.dateFrom.valueChanges.subscribe((date: IMyDateModel) => {
            DateUtil.updateDateToWhenChangeDateFrom(
                this.dateFrom.value,
                this.dateTo.value,
                this.updateDateToByDateFrom.bind(this),
                this.updateErrormsg.bind(this)
            );
        });

        this.subs.sink = this.dateTo.valueChanges.subscribe((date: IMyDateModel) => {
            DateUtil.updateDateFromWhenChangeDateTo(
                this.dateFrom.value,
                this.dateTo.value,
                this.updateDateFromByDateTo.bind(this),
                this.updateErrormsg.bind(this));
        });

        this.subs.sink = this.datePicker.valueChanges.subscribe((date: IMyDateModel) => {
            const selectedDate = moment(DateUtil.buildFullDateData(date.singleDate.date));
            this.isShownDatePicker = true;
            this.isUsedDatePicker = true;
            this.isUsedDateRangePicker = false;

            this.dateTo.setValue(DateUtil.buildDataDatePickerMoment(selectedDate));
            this.dateFrom.setValue(DateUtil.buildDataDatePickerMoment(selectedDate));
        });

        this.subs.sink = this.dateRangeSelect.valueChanges.subscribe((data) => {
            this.isShownDatePicker = false;
            this.isUsedDatePicker = false;
            this.isUsedDateRangePicker = true;
            const fromTodayObj = DateUtil.buildDateFromRange(data);

            if (data === DATE_FILTER.TODAY) {
                this.isUsedDatePicker = true;
                this.isUsedDateRangePicker = false;
                this.datePicker.setValue(DateUtil.buildDataDatePicker(new Date()));
                this.dateTo.setValue(DateUtil.buildDataDatePickerMoment(fromTodayObj));
                this.dateFrom.setValue(DateUtil.buildDataDatePickerMoment(fromTodayObj));
                return;
            }
            if (data === DATE_FILTER.ALL) {
                this.dateTo.setValue(null, { emitEvent: false });
                this.dateFrom.setValue(null, { emitEvent: false });
            } else {
                this.dateTo.setValue(DateUtil.buildDataDatePickerMoment(moment()));
                this.dateFrom.setValue(DateUtil.buildDataDatePickerMoment(fromTodayObj));
            }
        });

    }

    buildColumns() {
        let column: any = this.mode === HISTORY_SEARCH_MODE.VEHICLE ? {
            id: 'vehicleName',
            i18n: 'COMMON_LABEL.COLUMNS.VEHICLE',
            sortable: false,
            filterable: true,
            width: '380px',
            cellTemplate: this.vehicleName
        } : {
            id: 'searchMode',
            i18n: 'COMMON_LABEL.COLUMNS.SEARCH_METHOD',
            sortable: false,
            filterable: true,
            width: '380px',
            cellTemplate: this.articleSearchMethod,
            type: 'select',
            selectSource: this.getArticleSearchModeOptions()
        };

        this.columns = [
            column,
            {
                id: 'searchTerm',
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.KEYWORD',
                sortable: false,
                filterable: true,
                width: '300px',
                cellTemplate: this.mode === HISTORY_SEARCH_MODE.ARTICLE ? this.articleSearchTermDisplay : this.vehicleSearchTerm
            },
            {
                id: 'selectDate',
                i18n: 'COMMON_LABEL.DATE',
                sortable: true,
                filterable: false,
                type: 'date',
                dateFormat: SAG_COMMON_DATETIME_FORMAT,
            },
            {
                id: 'fullName',
                i18n: 'SETTINGS.MY_ORDER.FILTER.USER_NAME',
                sortable: false,
                filterable: true,
                cellTemplate: this.nameRef,
            }
        ];
    }

    getArticleSearchModeOptions() {
        return [
            {
                value: '',
                label: `COMMON_LABEL.ALL`
            },
            {
                value: ARTICLE_FILTER_MODE.FREE_TEXT,
                label: `SEARCH.ARTICLE_FULLTEXT`
            },
            {
                value: ARTICLE_FILTER_MODE.ARTICLE_NR,
                label: `SEARCH.ARTICLE_TIPS`
            },
            {
                value: ARTICLE_FILTER_MODE.ARTICLE_DESC,
                label: `SEARCH.ARTICLE_DESC`
            }
        ];
    }

    getDateRangeSelect() {
        return [
            {
                id: DATE_FILTER.ALL,
                code: `COMMON_LABEL.${DATE_FILTER.ALL}`
            },
            {
                id: DATE_FILTER.TODAY,
                code: `COMMON_LABEL.${DATE_FILTER.TODAY}`
            },
            {
                id: DATE_FILTER.TWO_DAYS,
                code: `COMMON_LABEL.${DATE_FILTER.TWO_DAYS}`
            },
            {
                id: DATE_FILTER.SEVEN_DAYS,
                code: `COMMON_LABEL.${DATE_FILTER.SEVEN_DAYS}`
            },
            {
                id: DATE_FILTER.THIRTYONE_DAYS,
                code: `COMMON_LABEL.${DATE_FILTER.THIRTYONE_DAYS}`
            }
        ];
    }

    searchTableData(data) {
        this.tableCallback = data.callback;
        this.tableRequest = data.request;
        this.searchHistory();
    }

    async searchHistory(page?) {
        if (!this.tableCallback) {
            return;
        }
        const spinner = SpinnerService.start('connect-search-history-modal .modal-body');
        await this.getFilterData(page);
        let obs;
        if (this.mode === HISTORY_SEARCH_MODE.VEHICLE) {
            obs = this.searchService.getVehicleHistoryRevampSearch(this.historyRequest.createDto());
        } else {
            obs = this.articleSearchService.getArticleSearchHistory(this.historyRequest.createDto())
        }

        obs.pipe(
            catchError(() => of(null)),
            finalize(() => SpinnerService.stop(spinner))
        )
        .subscribe(result => {
            const res: any = result;
            if (!res) {
                this.tableCallback({
                    rows: [],
                    totalItems: 0,
                    itemsPerPage: 0,
                    page: 0
                });
                return;
            }
            const { content } = res;

            const rows = content.map(item => {
                if (this.mode === HISTORY_SEARCH_MODE.ARTICLE) {
                    return item;
                }
                return new VehicleSearchHistory(item);
            });

            this.tableCallback({
                rows,
                totalItems: res.totalElements,
                itemsPerPage: res.size,
                page: res.number + 1
            });
        });
    }

    async getFilterData(requestPage?) {
        const { filter, sort, page } = this.tableRequest;

        if (typeof (requestPage) == 'number') {
            this.historyRequest.pageNumber = requestPage;
        } else {
            if (this.historyRequest && (page - 1 !== this.historyRequest.pageNumber)) {
                this.historyRequest.pageNumber = page - 1;
                return;
            }
        }

        this.historyRequest = new HistorySearchRequest();
        if (this.vehicleClass && this.shouldFilterByMotorbike) {
            this.historyRequest.vehicleClass = this.vehicleClass;
        }
        if (this.dateFrom.value) {
            this.historyRequest.fromDate = moment(DateUtil.buildFullDateData(this.dateFrom.value.singleDate.date)).valueOf();
        }
        if (this.dateTo.value) {
            this.historyRequest.toDate = moment(DateUtil.buildFullDateData(this.dateTo.value.singleDate.date)).endOf('day').valueOf();
        }

        if (sort) {
            // Only sort by date
            this.historyRequest.orderDescBySelectDate = sort.direction !== Constant.ASC_LOWERCASE;
        }
        if (filter) {
            this.historyRequest.searchType = filter.searchMode && filter.searchMode.value || null;
            this.historyRequest.searchTerm = filter.searchTerm || null;
            this.historyRequest.vehicleName = filter.vehicleName || null;
            this.historyRequest.fullName = (filter.fullName || '').trim() || null;
        }

        await this.setFilterModeRequest();
    }

    async setFilterModeRequest() {
        this.historyRequest.filterMode = this.filterMode.value;
        if (this.historyRequest.filterMode !== this.C4C_MODE) {
            if (this.historyRequest.fullName) {
                await this.translateService.get('COMMON_LABEL.COLUMNS.SALE_NAME').toPromise().then(text => {
                    if (this.historyRequest.fullName === text) {
                        this.historyRequest.filterMode = this.C4S_MODE;
                    }
                });
            }
        }
    }

    getFilterModeOptions() {
        return [
            {
                id: ALL,
                code: 'COMMON_LABEL.ALL'
            },
            {
                id: this.userService.userDetail.isSalesOnBeHalf ? 'C4S' : 'C4C',
                code: `HISTORY_MODE.OWN_HISTORY`
            }
        ];
    }

    moveBackOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = DateUtil.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, this.dateFormat).subtract(1, 'M');
        const newDateTo = moment(newDateFrom, this.dateFormat).add(1, 'M');
        this.isUsedDateRangePicker = true;
        this.isUsedDatePicker = false;

        this.filterForm.patchValue({
            dateTo: DateUtil.buildDataDatePickerMoment(newDateTo),
            dateFrom: DateUtil.buildDataDatePickerMoment(newDateFrom)
        });
    }

    moveForwardOneMonth() {
        const dateFrom = (this.dateFrom.value as IMyDateModel).singleDate.date;
        const ogirinalDateFrom = DateUtil.buildFullDateData(dateFrom);

        const newDateFrom = moment(ogirinalDateFrom, this.dateFormat).add(1, 'M');
        const newDateTo = moment(newDateFrom, this.dateFormat).add(1, 'M');

        if (newDateTo.isAfter(moment())) {
            return;
        }

        this.isUsedDateRangePicker = true;
        this.isUsedDatePicker = false;

        this.filterForm.patchValue({
            dateFrom: DateUtil.buildDataDatePickerMoment(newDateFrom),
            dateTo: DateUtil.buildDataDatePickerMoment(newDateTo),
        });
    }

    onSelectHistoryItem(data) {
        if (this.mode === HISTORY_SEARCH_MODE.VEHICLE) {
            this.searchService.navigateToHistory(data);
        } else {
            this.searchService.navigateToArticleSearch(data, get(this.userService, 'userDetail.id'));
        }
        this.bsModalRef.hide();
    }

    onSubmit() {
        this.searchHistory(this.INIT_PAGE);
    }

    get dateFrom() { return this.filterForm && this.filterForm.get('dateFrom') || null; }
    get dateTo() { return this.filterForm && this.filterForm.get('dateTo') || null; }
    get datePicker() { return this.filterForm && this.filterForm.get('datePicker') || null; }
    get dateRangeSelect() { return this.filterForm && this.filterForm.get('dateRangeSearch') || null; }
    get filterMode() { return this.filterForm && this.filterForm.get('filterMode') || null; }

    private updateDateFromByDateTo(dateTo: string) {
        const newDateFrom = moment(dateTo).subtract(1, 'M');
        const newDateFromPicker = DateUtil.buildDataDatePickerMoment(newDateFrom);
        this.filterForm.controls['dateFrom'].setValue(newDateFromPicker);
    }

    private updateDateToByDateFrom(dateFrom: string = null) {
        const newDateTo = !!dateFrom ? moment(dateFrom).add(1, 'M') : moment();
        const newDateToPicker = DateUtil.buildDataDatePickerMoment(newDateTo);
        this.filterForm.controls['dateTo'].setValue(newDateToPicker);
    }

    private updateErrormsg(msg: string) {
        // this.errorMessage = msg;
        // this.errorMessageChange.emit(this.errorMessage);
    }

    showMotoOnlyChange(event) {
        this.shouldFilterByMotorbike = event.target.checked;
        this.searchHistory(this.INIT_PAGE);
    }
}
