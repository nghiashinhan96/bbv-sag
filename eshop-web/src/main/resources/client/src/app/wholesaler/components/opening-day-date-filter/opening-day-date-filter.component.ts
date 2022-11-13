import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { NgOption } from '@ng-select/ng-select';
import { TranslateService } from '@ngx-translate/core';
import { IMyDateModel } from 'angular-mydatepicker';
import * as moment from 'moment';

import { DATE_FILTER } from 'src/app/core/enums/date-filter.enum';
import { DateUtil } from 'src/app/core/utils/date.util';

@Component({
    selector: 'connect-opening-day-date-filter',
    templateUrl: './opening-day-date-filter.component.html',
    styleUrls: ['./opening-day-date-filter.component.scss']
})
export class OpeningDayDateFilterComponent implements OnInit {
    @Output() searchEvent = new EventEmitter();
    @Output() searchWithSelectedDay = new EventEmitter();
    @Output() searchWithPredefinedRange = new EventEmitter();

    datePickerCommonSetting = DateUtil.getCommonSetting();
    datePickerCombineSetting = Object.assign({ alignSelectorRight: true }, DateUtil.getCommonSetting());
    dateRangePickerSetting = Object.assign({}, DateUtil.getCommonSetting());
    selectedFilterDateIndex = 0;
    selectedFilterDate: NgOption;
    filterRangeOptions: Array<NgOption>;
    filterDateFromModel = DateUtil.buildDataDatePickerMoment(DateUtil.buildDateFromRange(DATE_FILTER.THIRTY_DAYS));
    filterDateToModel = DateUtil.buildDataDatePickerMoment(moment());
    filterDateRangeModel = DateUtil.buildDataDatePickerMoment(moment());
    isFilterDateOptionVisible: boolean;
    isDateRangeSearchActivated: boolean;
    isPredefinedDateRangeSearchActivated: boolean;
    isInvalidDateRange: boolean;
    locale = '';
    filterForm: FormGroup;


    constructor(private translateService: TranslateService, private fb: FormBuilder) {
        this.filterRangeOptions = this.getDateRangeSelect();
        this.isFilterDateOptionVisible = true;
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.isDateRangeSearchActivated = true;
        this.selectedFilterDate = this.filterRangeOptions[this.selectedFilterDateIndex];
        this.buildFilterForm();
    }

    search() {
        this.isDateRangeSearchActivated = true;
        this.isPredefinedDateRangeSearchActivated = false;
        this.searchEvent.emit({ from: this.filterDateFromModel, to: this.filterDateToModel });
    }

    validateDateRange($event, type) {
        if (type === 'from') {
            this.filterDateFromModel = $event;
            this.filterDateToModel = this.filterForm.get('dateTo').value;
        }
        if (type === 'to') {
            this.filterDateFromModel = this.filterForm.get('dateFrom').value;
            this.filterDateToModel = $event;
        }

        const from = new Date(this.filterDateFromModel.singleDate.date.year,
            this.filterDateFromModel.singleDate.date.month - 1,
            this.filterDateFromModel.singleDate.date.day);
        const to = new Date(this.filterDateToModel.singleDate.date.year,
            this.filterDateToModel.singleDate.date.month - 1,
            this.filterDateToModel.singleDate.date.day);
        this.isInvalidDateRange = !this.isValidRange(from, to);
    }

    isValidRange(from: Date, to: Date) {
        return moment(to).isSameOrAfter(from);
    }

    onDateRangeChange(event: IMyDateModel) {
        this.isDateRangeSearchActivated = false;
        this.isFilterDateOptionVisible = false;
        this.isPredefinedDateRangeSearchActivated = true;

        this.filterDateRangeModel = this.buildDateData(event.singleDate.date);
        this.searchWithSelectedDay.emit(this.filterDateRangeModel.singleDate);
        this.selectedFilterDateIndex = null;
    }

    private buildDateData(date) {
        return {
            isRange: false,
            singleDate: {
                date
            }
        } as IMyDateModel;
    }

    onFilterSelected(option: NgOption) {
        this.isDateRangeSearchActivated = false;
        this.isPredefinedDateRangeSearchActivated = true;
        this.isFilterDateOptionVisible = true;
        this.selectedFilterDate = option;
        this.searchWithPredefinedRange.emit({filter: this.selectedFilterDate });
    }


    private buildFilterForm() {
        this.filterForm = this.fb.group({
            dateFrom: this.filterDateFromModel,
            dateTo: this.filterDateToModel,
        });
    }

    getDateRangeSelect() {
        return [
            {
                value: DATE_FILTER.TODAY,
                label: `COMMON_LABEL.${DATE_FILTER.TODAY}`
            },
            {
                value: DATE_FILTER.TWO_DAYS,
                label: `COMMON_LABEL.${DATE_FILTER.TWO_DAYS}`
            },
            {
                value: DATE_FILTER.SEVEN_DAYS,
                label: `COMMON_LABEL.${DATE_FILTER.SEVEN_DAYS}`
            },
            {
                value:  DATE_FILTER.THIRTYONE_DAYS,
                label: `COMMON_LABEL.${DATE_FILTER.THIRTYONE_DAYS}`
            }
        ];
    }

}
