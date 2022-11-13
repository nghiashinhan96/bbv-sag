import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { NgOption } from '@ng-select/ng-select';
import { IMyDateModel } from 'angular-mydatepicker';

import { INTERVAL_THIRTY_ONE_DAYS } from '../../constant';
import { FilterDateService } from '../../service/filter-date.service';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'backoffice-opening-day-date-filter',
    templateUrl: './opening-day-date-filter.component.html',
    styleUrls: ['./opening-day-date-filter.component.scss']
})
export class OpeningDayDateFilterComponent implements OnInit {
    @Output() searchEvent = new EventEmitter();
    @Output() searchWithSelectedDay = new EventEmitter();
    @Output() searchWithPredefinedRange = new EventEmitter();

    datePickerCommonSetting = DatePickerUtil.commonSetting;
    datePickerCombineSetting = Object.assign({ alignSelectorRight: true }, DatePickerUtil.commonSetting);
    // dateRangePickerSetting = Object.assign({}, DatePickerUtil.commonSetting);
    selectedFilterDateIndex = '0';
    selectedFilterDate: NgOption;
    filterRangeOptions: Array<NgOption>;
    filterDateFromModel = this.filterOptionService.getDateFromGivenDate(DatePickerUtil.initDate().date, INTERVAL_THIRTY_ONE_DAYS);
    filterDateToModel = DatePickerUtil.initDate();
    filterDateRangeModel = DatePickerUtil.initDate();
    isFilterDateOptionVisible: boolean;
    isDateRangeSearchActivated: boolean;
    isPredefinedDateRangeSearchActivated: boolean;
    isInvalidDateRange: boolean;
    locale = '';
    filterForm: FormGroup;

    constructor(
        private filterOptionService: FilterDateService,
        private fb: FormBuilder,
        private translateService: TranslateService) {
        this.filterRangeOptions = this.filterOptionService.getFilterDateOption();
        this.isFilterDateOptionVisible = true;
        this.locale = this.translateService.currentLang;
    }

    ngOnInit() {
        this.isDateRangeSearchActivated = true;
        this.selectedFilterDate = this.filterOptionService.getFilterOption(+this.selectedFilterDateIndex);
        this.buildFilterForm();
    }

    search() {
        this.isDateRangeSearchActivated = true;
        this.isPredefinedDateRangeSearchActivated = false;
        this.searchEvent.emit({ from: this.filterDateFromModel, to: this.filterDateToModel });
    }

    validateDateRange($event, type) {
        if (type === 'from') {
            this.filterDateFromModel = $event.singleDate;
            this.filterDateToModel = this.filterForm.get('dateTo').value.singleDate;
        }
        if (type === 'to') {
            this.filterDateFromModel = this.filterForm.get('dateFrom').value.singleDate;
            this.filterDateToModel = $event.singleDate;
        }

        const from = new Date(this.filterDateFromModel.date.year,
            this.filterDateFromModel.date.month - 1,
            this.filterDateFromModel.date.day);
        const to = new Date(this.filterDateToModel.date.year, this.filterDateToModel.date.month - 1, this.filterDateToModel.date.day);
        this.isInvalidDateRange = !this.filterOptionService.isValidRange(from, to);
    }

    onDateRangeChange(event: IMyDateModel) {
        this.isDateRangeSearchActivated = false;
        this.isFilterDateOptionVisible = false;
        this.isPredefinedDateRangeSearchActivated = true;

        this.filterDateRangeModel = { date: event.singleDate.date };
        this.searchWithSelectedDay.emit(this.filterDateRangeModel);
        this.selectedFilterDateIndex = null;
    }

    onFilterSelected(option: NgOption) {
        this.isDateRangeSearchActivated = false;
        this.isPredefinedDateRangeSearchActivated = true;
        this.isFilterDateOptionVisible = true;
        this.selectedFilterDate = this.filterOptionService.getFilterOption(+option.value);
        this.searchWithPredefinedRange.emit({ from: this.filterDateRangeModel, filter: this.selectedFilterDate.label });
    }

    private buildDateData({ date }) {
        return {
            isRange: false,
            singleDate: {
                date
            }
        } as IMyDateModel;
    }

    private buildFilterForm() {
        this.filterForm = this.fb.group({
            dateFrom: this.buildDateData(this.filterDateFromModel),
            dateTo: this.buildDateData(this.filterDateToModel),
        });
    }
}
