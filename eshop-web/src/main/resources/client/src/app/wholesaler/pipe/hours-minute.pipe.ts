import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'hoursMinute'
})
export class HoursMinutePipe implements PipeTransform {

    transform(value: string | Date, args?: any): any {
        if (!value) {
            return '';
        }
        if (value instanceof Date) {
            const h = ('0' + (value.getHours() || 0)).slice(-2);
            const m = ('0' + (value.getMinutes() || 0)).slice(-2);
            return `${h}:${m}`;
        } else {
            const arr = value.split(':');
            if (arr.length > 1) {
                return `${arr[0]}:${arr[1]}`;
            }
        }
        return '';
    }
}
