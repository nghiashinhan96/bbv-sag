import { Injectable } from '@angular/core';
import { NonMerkmaleAttribute } from '../models/non-merkmale-filter-attribute.model';
import { SEARCH_MODE } from 'sag-article-list';

@Injectable({
    providedIn: 'root'
})
export class NonMerkmaleBusinessService {
    constructor() { }

    composeAttributes(attribute: any) {
        const typeMode = attribute.type;

        switch (typeMode) {
            case SEARCH_MODE.TYRES_SEARCH:
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
                return new NonMerkmaleAttribute(attribute).toTyreAttribute(attribute);
            case SEARCH_MODE.BATTERY_SEARCH:
                return new NonMerkmaleAttribute(attribute).toBatteryAttribute(attribute);
            case SEARCH_MODE.BULB_SEARCH:
                return new NonMerkmaleAttribute(attribute).toBulbAttribute(attribute);
            case SEARCH_MODE.OIL_SEARCH:
                return new NonMerkmaleAttribute(attribute).toOilAttribute(attribute);
        }
    }
}
