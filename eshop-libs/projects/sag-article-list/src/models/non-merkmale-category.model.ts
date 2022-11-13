import { FilterCategory } from './filter-category.model';
import { TyreFilter } from './tyre-filter.model';
import { BulbFilter } from './bulb-filter.model';
import { OilFilter } from './oil-filter.model';
import { BatteryFilter } from './battery-filter.model';
import { SEARCH_MODE } from '../enums/search-mode.enum';

export class NonMerkmaleCategory extends FilterCategory {
    tyreSearchRequest?: TyreFilter;
    motorTyreSearchRequest?: TyreFilter;
    bulbSearchRequest?: BulbFilter;
    oilSearchRequest?: OilFilter;
    batterySearchRequest?: BatteryFilter;

    constructor(searchMode: string, data?: any) {
        super(data);
        if (searchMode) {
            this.filterMode = searchMode;
        }
    }

    setFilterType(requestFields: any) {
        switch (this.filterMode) {
            case SEARCH_MODE.TYRES_SEARCH.toString():
                this.tyreSearchRequest = new TyreFilter(requestFields).toTyreRequest(requestFields);
                break;
            case SEARCH_MODE.MOTOR_TYRES_SEARCH.toString():
                this.motorTyreSearchRequest = new TyreFilter(requestFields).toTyreMotorRequest(requestFields);
                break;
            case SEARCH_MODE.BULB_SEARCH.toString():
                this.bulbSearchRequest = new BulbFilter(requestFields);
                break;
            case SEARCH_MODE.OIL_SEARCH.toString():
                this.oilSearchRequest = new OilFilter(requestFields);
                break;
            case SEARCH_MODE.BATTERY_SEARCH.toString():
                this.batterySearchRequest = new BatteryFilter(requestFields);
                break;
        }
    }
}
