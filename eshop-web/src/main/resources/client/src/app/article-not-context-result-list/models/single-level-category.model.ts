import { LevelCategory } from './level-category.model';
import { TyreFilter } from './tyre-filter.model';
import { BulbFilter } from './bulb-filter.model';
import { OilFilter } from './oil-filter.model';
import { BatteryFilter } from './battery-filter.model';
import { SEARCH_MODE } from 'sag-article-list';

export class SingleLevelCategory extends LevelCategory {
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
        this.totalElementsOfSearching = requestFields.totalElements || 0;

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
