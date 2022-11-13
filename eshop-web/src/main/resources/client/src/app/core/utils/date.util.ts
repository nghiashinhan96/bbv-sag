import { DatePipe } from '@angular/common';
import { IMyDate, IMyDateModel } from 'angular-mydatepicker';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Constant } from '../conts/app.constant';
import { DATE_FILTER } from '../enums/date-filter.enum';

export class DateUtil {

    private static DEFAULT_LOCALE_ID = 'en-US';
    public static DEFAULT_DAY_FORMAT = 'dd.mm.yyyy';
    public static SECONDARY_DAY_FORMAT = 'yyyy-MM-dd';
    public static getCommonSetting() {
        return {
            dateFormat: this.DEFAULT_DAY_FORMAT,
            height: '30px',
            showClearDateBtn: false,
            editableDateField: false,
            openSelectorOnInputClick: true,
            markCurrentDay: true,
            showFooterToday: true,
            focusInputOnDateSelect: false
        };
    }

    public static formatDateInTime(date) {
        // let datePipe = new DatePipe();
        // return datePipe.transform(date, 'HH:mm');
        // Fix datePipe in IE
        if (!DateUtil.isValidObj(date)) {
            return '';
        }
        const d = new Date(date);
        const hour = (d.getHours() < 10 ? '0' : '') + d.getHours();
        const minute = (d.getMinutes() < 10 ? '0' : '') + d.getMinutes();
        return hour + ':' + minute;
    }

    public static formatDateInDate(date) {
        if (!DateUtil.isValidObj(date)) {
            return '';
        }
        return new DatePipe(DateUtil.DEFAULT_LOCALE_ID).transform(date, 'dd.MM.yyyy');
    }

    public static format(date, format) {
        if (!this.isValidObj(date)) {
            return '';
        }
        return new DatePipe(this.DEFAULT_LOCALE_ID).transform(date, format);
    }

    public static formatDateWithMonthAndYear(date, dateSeparator = '/') {
        if (!DateUtil.isValidObj(date)) {
            return '';
        }
        const year = date.toString().substring(0, 4);
        const month = date.toString().substring(4, date.length);
        return `${('0' + month).slice(-2)}${dateSeparator}${year}`;
    }

    private static isValidObj(obj) {
        if (!obj || obj === '0') {
            return false;
        }
        return true;
    }

    public static getCurrentTimeZone() {
        return Intl.DateTimeFormat().resolvedOptions().timeZone;
    }

    public static getCurrentDateTime() {
        const now = new Date();
        const date = DateUtil.formatDateInDate(now);
        const time = DateUtil.formatDateInTime(now);
        const dateTime = date + ' ' + time;
        return dateTime;
    }

    public static buildDataDatePicker(dateInJavascript) {
        return {
            singleDate: {
                date: {
                    year: dateInJavascript.getFullYear(),
                    month: dateInJavascript.getMonth() + 1,
                    day: dateInJavascript.getDate()
                }
            }
        };
    }

    public static buildDataDatePickerMoment(date: Moment) {
        return {
            isRange: false,
            singleDate: {
                date: {
                    year: date.year(),
                    month: date.month() + 1,
                    day: date.date()
                }
            }
        } as IMyDateModel;
    }

    public static datePickerToJsDate(date) {
        return Date.UTC(date.year, date.month - 1, date.day);
    }

    public static formatDateInDateTime(date) {
        const value = moment(date);
        return value.isValid() ? `${DateUtil.formatDateInDate(value)} ${DateUtil.formatDateInTime(value)}` : '';
    }

    public static getTimeInSeconds() {
        return Math.round(new Date().getTime() / 1000);
    }

    public static isDateInRange(date, compareDate, isBefore) {
        const mDate = moment(date);
        const mCompareDate = moment(compareDate);
        if (isBefore) {
            return mDate.isBefore(mCompareDate);
        }

        return mDate.isAfter(mCompareDate);
    }

    public static buildFullDateData(date: IMyDate) {
        return `${date.year}-${date.month}-${date.day}`;
    }

    public static updateDateFromWhenChangeDateTo(inDateFrom, inDateTo, onValid, onError) {
        const dateTo = (inDateTo as IMyDateModel).singleDate.date;
        const dateToData = DateUtil.buildFullDateData(dateTo);
        if (!inDateFrom) {
            if (onValid) {
                onValid(dateToData);
            }
            return;
        }

        const dateFrom = (inDateFrom as IMyDateModel).singleDate.date;
        const dateFromData = DateUtil.buildFullDateData(dateFrom);

        // dateTo < dateFrom
        if (moment(dateToData).isBefore(moment(dateFromData))) {
            if (onValid) {
                onValid(dateToData);
            }

            // dateFrom - dateTo > 31
            if (moment(dateFromData).diff(moment(dateToData), 'days') > Constant.FILTER_RANGE_DATE) {
                if (onError) {
                    onError('SEARCH.ERROR_MESSAGE.FROM_DATE_CHANGED');
                }
            }
        }

        // dateTo > dateFrom && dateTo - dateFrom > 31
        if (moment(dateToData).isAfter(moment(dateFromData))
            && moment(dateToData).diff(moment(dateFromData), 'days') > Constant.FILTER_RANGE_DATE) {
            if (onValid) {
                onValid(dateToData);
            }
            if (onError) {
                onError('SEARCH.ERROR_MESSAGE.FROM_DATE_CHANGED');
            }
        }
    }

    public static updateDateToWhenChangeDateFrom(inDateFrom, inDateTo, onValid, onError) {
        const dateFrom = (inDateFrom as IMyDateModel).singleDate.date;
        const dateFromData = DateUtil.buildFullDateData(dateFrom);
        if (!inDateTo) {
            if (onValid) {
                onValid(dateFromData);
            }
            return;
        }

        const dateTo = (inDateTo as IMyDateModel).singleDate.date;
        const dateToData = DateUtil.buildFullDateData(dateTo);

        // dateFrom > dateTo
        if (moment(dateFromData).isAfter(moment(dateToData))) {
            if (onValid) {
                onValid(dateFromData);
            }

            // dateFrom - dateTo > 31
            if (moment(dateFromData).diff(moment(dateToData), 'days') > Constant.FILTER_RANGE_DATE) {
                if (onError) {
                    onError('SEARCH.ERROR_MESSAGE.TO_DATE_CHANGED');
                }
            }
        }

        // dateFrom < dateTo && dateTo - dateFrom > 31
        if (moment(dateFromData).isBefore(moment(dateToData))
            && moment(dateToData).diff(moment(dateFromData), 'days') > Constant.FILTER_RANGE_DATE) {
            if (onValid) {
                onValid(dateFromData);
            }
            if (onError) {
                onError('SEARCH.ERROR_MESSAGE.TO_DATE_CHANGED');
            }
        }
    }

    public static buildDateFromRange(range: string) {
        switch (range) {
            case DATE_FILTER.TODAY:
                return moment();
            case DATE_FILTER.TWO_DAYS:
                return moment().subtract(2, 'days');
            case DATE_FILTER.SEVEN_DAYS:
                return moment().subtract(7, 'days');
            case DATE_FILTER.THIRTY_DAYS:
                return moment().subtract(30, 'days');
            case DATE_FILTER.THIRTYONE_DAYS:
                return moment().subtract(31, 'days');
            case DATE_FILTER.ADD_THIRTYONE_DAYS:
                return moment().add(31, 'days');
        }
    }

    public static buildDataTimePicker(minMins = 0) {
        let hours = 0;
        const result = [];
        while (hours < 24) {
            const threshold = 30;
            const formatHour = hours < 10 ? `0${hours}` : hours.toString();
            let initialMins = 0;
            while (initialMins < 60) {
                let formatMins = `${initialMins}`.padStart(2,'0');
                if (hours == 0 && initialMins == 0) {
                    formatMins =  `${minMins}`.padStart(2,'0');
                }
                result.push({ hour: formatHour, mins: formatMins, formatText: `${formatHour}:${formatMins}` });
                initialMins += threshold;
            }
            hours++;
        }

        return result;
    }

    public static durationObjectToSeconds(obj: any) {
        if (!obj) {
            return null;
        }
        const minutes = Number(obj.mins);
        const hours = Number(obj.hour);
        return (hours * 60 + minutes) * 60;
    }

    public static parseTime(timeString) {
        if (timeString == '') return null;
        let d = new Date();
        let time = timeString.match(/(\d+)(:(\d\d))?\s*(p?)/i);
        d.setHours(parseInt(time[1], 10) + ((parseInt(time[1], 10) < 12 && time[4]) ? 12 : 0));
        d.setMinutes(parseInt(time[3], 10) || 0);
        d.setSeconds(0, 0);
        return d;
    }
}
