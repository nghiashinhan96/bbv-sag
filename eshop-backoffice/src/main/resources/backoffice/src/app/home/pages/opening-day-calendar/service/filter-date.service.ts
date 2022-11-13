import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { NgOption } from '@ng-select/ng-select';
import { DatePickerUtil } from 'src/app/core/utils/date-picker.util';

import { FILTER_DATE_TODAY, FILTER_DATE_2_DAYS, FILTER_DATE_7_DAYS, FILTER_DATE_31_DAYS } from '../constant';

@Injectable()
export class FilterDateService {
    private translatedToday = this.translateService.instant(FILTER_DATE_TODAY);
    private translated2Days = this.translateService.instant(FILTER_DATE_2_DAYS);
    private translated7Days = this.translateService.instant(FILTER_DATE_7_DAYS);
    private translated31Days = this.translateService.instant(FILTER_DATE_31_DAYS);

    private filterDateRange: Array<NgOption> = [
        {
            value: '0',
            label: this.translatedToday
        },
        {
            value: '1',
            label: this.translated2Days
        },
        {
            value: '2',
            label: this.translated7Days
        },
        {
            value: '3',
            label: this.translated31Days
        }
    ];

    constructor(private translateService: TranslateService) { }

    getFilterDateOption(): Array<NgOption> {
        return this.filterDateRange;
    }

    getFilterOption(index: number): NgOption {
        return this.filterDateRange[index];
    }

    getDateFromGivenDate(date: any, subtract: number) {
        const dateObj = new Date(date.year, date.month - 1, date.day);
        dateObj.setDate(dateObj.getDate() - subtract);
        return DatePickerUtil.buildDataDatePicker(dateObj);
    }

    isValidRange(from: Date, to: Date) {
        if (from.getFullYear() > to.getFullYear()) {
            return false;
        }

        if (from.getFullYear() === to.getFullYear()) {
            if (from.getMonth() > to.getMonth()) {
                return false;
            }
            if (from.getMonth() === to.getMonth() && from.getDate() > to.getDate()) {
                return false;
            }
        }

        return true;
    }
}
