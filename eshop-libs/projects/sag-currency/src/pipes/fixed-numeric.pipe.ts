import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'fixedNumeric'
})
export class FixedNumericPipe implements PipeTransform {

    transform(value: any, ...args: any[]): any {
        return null;
    }

}
