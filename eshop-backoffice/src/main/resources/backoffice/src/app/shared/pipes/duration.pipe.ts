import { Pipe, PipeTransform } from '@angular/core';
import { DateUtil } from 'src/app/core/utils/date.util';

@Pipe({
    name: 'duration'
})
export class DurationPipe implements PipeTransform {

    transform(seconds: number, args?: any): any {
        if (seconds == null || Number.isNaN(Number(seconds)))  {
            return '';
        }
        const obj = DateUtil.secondsToDurationObject(seconds);
        const h = ('0' + (obj.hours || 0)).slice(-2);
        const m = ('0' + (obj.minutes || 0)).slice(-2);
        return `${h}:${m}`;
    }

}
