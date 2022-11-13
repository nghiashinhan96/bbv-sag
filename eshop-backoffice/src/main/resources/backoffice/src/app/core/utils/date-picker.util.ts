import { IAngularMyDpOptions } from 'angular-mydatepicker';

export class DatePickerUtil {
    static commonSetting = {
        dateFormat: 'dd.mm.yyyy',
        height: '30px',
        showClearDateBtn: false,
        editableDateField: false,
        openSelectorOnInputClick: true,
        showFooterToday: true,
    };

    static initDate(additionalMonths?: number) {
        const currentDate = new Date();
        if (additionalMonths) {
            currentDate.setMonth(currentDate.getMonth() + additionalMonths);
        }
        return this.buildDataDatePicker(new Date(currentDate));
    }

    static buildDataDatePicker(dateInJavascript) {
        return {
            date: {
                year: dateInJavascript.getFullYear(),
                month: dateInJavascript.getMonth() + 1,
                day: dateInJavascript.getDate(),
            },
        };
    }

    static getDateFromToDatePicker(selectedDate, ymdFormat?) {
        if (selectedDate && selectedDate !== '') {
            const day =
                selectedDate.day < 10 ? '0' + selectedDate.day : selectedDate.day;
            const month =
                selectedDate.month < 10 ? '0' + selectedDate.month : selectedDate.month;
            const year = selectedDate.year;
            if (ymdFormat) {
                return `${year}-${month}-${day}`;
            }
            // return year + '.' + month + '.' + day;
            return `${day}.${month}.${year}`;
        }
        return '';
    }
}
