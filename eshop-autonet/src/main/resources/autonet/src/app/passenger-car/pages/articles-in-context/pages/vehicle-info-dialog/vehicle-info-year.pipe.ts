import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'vehicleInfoYear'
})
export class VehicleInfoYearPipe implements PipeTransform {

    transform(date: any): any {
        if (date.toString().length !== 6) {
            return '';
        }
        const year = date.substring(0, 4);
        const month = date.substring(4, date.length);
        return `${year}.${month}`;
    }

}
