import { DatePipe } from '@angular/common';
import * as moment from 'moment';

declare var jstz: any;
export class DateUtil {
    private static datePipe: DatePipe;
    public static dateFormat = 'dd.MM.yyyy';

    public static formatDate(date) {
        if (!this.isValidObj(date)) {
            return '';
        }
        return this.datePipe.transform(date, 'dd.MM.yyyy hh:mm');
    }

    public static getCommonSetting() {
        return {
            dateFormat: 'dd.mm.yyyy',
            height: '30px',
            showClearDateBtn: false,
            editableDateField: false,
            openSelectorOnInputClick: true,
            dateRange: false,
            showFooterToday: true,
        };
    }

    public static dateToString(date) {
        // let datePipe = new DatePipe();
        // return datePipe.transform(date, 'HH:mm');
        // Fix datePipe in IE
        if (!this.isValidObj(date)) {
            return '';
        }
        let d = new Date(date);
        const hours = ('0' + d.getHours()).slice(-2);
        const minutes = ('0' + d.getMinutes()).slice(-2);
        return `${hours}:${minutes}`;
    }

    public static formatDateInDate(date) {
        if (!this.isValidObj(date)) {
            return '';
        }
        return this.datePipe.transform(date, 'dd.MM.yyyy');
    }

    public static formatDateWithMonthAndYear(date) {
        if (!this.isValidObj(date)) {
            return '';
        }
        let year = date.toString().substring(0, 4);
        let month = date.toString().substring(5, date.length);
        return month + '/' + year;
    }

    public static isValidObj(obj) {
        if (obj === null || obj === undefined) {
            return false;
        }
        return true;
    }

    public static getTodayMidnightAsMiliSeconds(currentDateAsMiliSeconds) {
        let d = new Date(currentDateAsMiliSeconds);
        d.setHours(23, 59, 0, 0);
        return d.getTime();
    }

    public static getLastMomentTomorrow(currentDateAsMiliSeconds) {
        let tomorrow = new Date(currentDateAsMiliSeconds);
        // tomorrow avoid weekend
        do {
            tomorrow.setDate(tomorrow.getDate() + 1);
        } while (tomorrow.getDay() == 0 || tomorrow.getDay() == 6);
        tomorrow.setHours(23, 59, 0, 0);
        return tomorrow.getTime();
    }

    public static getCurrentTimeZone() {
        return (<any>jstz).determine().name();
    }

    public static getTomorrowDate8AM(currentDateAsMiliSeconds) {
        let currentDate = new Date(currentDateAsMiliSeconds);
        // tomorrow avoid weekend
        do {
            currentDate.setDate(currentDate.getDate() + 1);
        } while (currentDate.getDay() == 0 || currentDate.getDay() == 6);
        currentDate.setHours(8, 0, 0, 0);
        return currentDate.getTime();
    }

    public static getDateAfterTomorrow5AM(currentDateAsMiliSeconds) {
        let currentDate = new Date(currentDateAsMiliSeconds);
        // tomorrow avoid weekend
        do {
            currentDate.setDate(currentDate.getDate() + 1);
        } while (currentDate.getDay() == 0 || currentDate.getDay() == 6);
        // the day after tomorrow avoid weekend
        do {
            currentDate.setDate(currentDate.getDate() + 1);
        } while (currentDate.getDay() == 0 || currentDate.getDay() == 6);
        currentDate.setHours(5, 0, 0, 0);
        return currentDate.getTime();
    }

    public static isDate1BeforeDate2(firstDdate, secondDate) {
        let date1 = firstDdate.getDate();
        let month1 = firstDdate.getMonth();
        let year1 = firstDdate.getFullYear();

        let date2 = secondDate.getDate();
        let month2 = secondDate.getMonth();
        let year2 = secondDate.getFullYear();
        if (year1 < year2) {
            return true;
        }
        if (month1 < month2) {
            return true;
        }
        if (date1 < date2) {
            return true;
        }
        return false;
    }

    public static getCurrentDateTime() {
        let now = new Date();
        let date = this.formatDateInDate(now);
        let time = this.dateToString(now);
        let dateTime = date + ' ' + time;
        return dateTime;
    }

    /**
     * Convert to current date with provided time
     * @param time : 'HH:MM:SS'
     */
    public static stringToDate(time: string) {
        if (!time) {
            return null;
        }
        const d = new Date();
        const timeArr = time.split(':');
        if (timeArr[0]) {
            d.setHours(+timeArr[0]);
        }
        if (timeArr[1]) {
            d.setMinutes(+timeArr[1]);
        }
        if (timeArr[2]) {
            d.setSeconds(+timeArr[2]);
        }
        return d;
    }

    public static objectToDate(timeArr: number[]) {
        if (!timeArr) {
            return null;
        }
        const d = new Date();
        if (!timeArr[0]) {
            d.setHours(+timeArr[0]);
        }
        if (!timeArr[1]) {
            d.setMinutes(+timeArr[1]);
        }
        return d;
    }

    public static dateToObject(d: Date) {
        if (!d) {
            return null;
        }
        return [d.getHours(), d.getMinutes()];
    }

    public static dateToSeconds(d: Date) {
        if (!d) {
            return null;
        }
        return (d.getHours() * 60 + d.getMinutes()) * 60;
    }

    public static secondsToDate(seconds: number) {
        if (!seconds || Number.isNaN(Number(seconds))) {
            return null;
        }
        const d = new Date();
        const minutes = parseInt(`${seconds / 60}`);
        const hours = parseInt(`${minutes / 60}`);
        d.setHours(hours);
        d.setMinutes(minutes - hours * 60);
        return d;
    }

    public static secondsToDurationObject(seconds: number) {
        if (seconds == null || Number.isNaN(Number(seconds))) {
            return null;
        }
        const minutes = parseInt(`${seconds / 60}`);
        const hours = parseInt(`${minutes / 60}`);
        const remainingMin = minutes - hours * 60;
        return {
            hours: ('0' + hours).slice(-2),
            minutes: ('0' + remainingMin).slice(-2),
        };
    }

    public static durationObjectToSeconds(obj: any) {
        if (!obj) {
            return null;
        }
        const minutes = Number(obj.minutes);
        const hours = Number(obj.hours);
        return (hours * 60 + minutes) * 60;
    }

    public static changeMaxDate(date, maxDate) {
        const copy = JSON.parse(JSON.stringify(date));
        copy.disableUntil = maxDate;
        copy.disableSince = '';
        return copy;
    }

    public static changeMinDate(date, minDate) {
        const copy = JSON.parse(JSON.stringify(date));
        copy.disableSince = minDate;
        copy.disableUntil = '';
        return copy;
    }

    public static isDateInRange(date, compareDate, isBefore) {
        const mDate = moment(date);
        const mCompareDate = moment(compareDate);
        if (isBefore) {
            return mDate.isBefore(mCompareDate);
        }

        return mDate.isAfter(mCompareDate);
    }
}
