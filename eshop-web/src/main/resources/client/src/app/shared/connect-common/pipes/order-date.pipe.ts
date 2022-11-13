import { Pipe, PipeTransform } from '@angular/core';
import { DateUtil } from 'src/app/core/utils/date.util';

@Pipe({ name: 'orderDatePipe' })
export class OrderDatePipe implements PipeTransform {
    transform(value: string): any {
        const formattedValue = this.removeDateTrailing(value);
        return DateUtil.formatDateInDate(formattedValue) + ' ' + DateUtil.formatDateInTime(formattedValue);
    }

    private removeDateTrailing(date: string) {
        if (date && date.toString().lastIndexOf('+') !== -1) {
            return date.toString().substr(0, date.indexOf('+'));
        }
        return date;
    }
}
