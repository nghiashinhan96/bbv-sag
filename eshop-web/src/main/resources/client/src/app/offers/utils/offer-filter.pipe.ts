import { Pipe, PipeTransform } from '@angular/core';
import { isEmpty, isArray, isString, includes } from 'lodash';

@Pipe({ name: 'filterOffer' })
export class OfferFilterPipe implements PipeTransform {
    constructor() { }

    transform(n: Array<any>, type: any): Array<any> {
        if (isEmpty(n)) {
            return [];
        }

        if (isArray(type)) {
            return n.filter(item => includes(type, item.type));
        }

        if (isString(type)) {
            return n.filter(item => item.type === type);
        }
        return [];
    }
}
