import { NonMerkmaleCategory } from './non-merkmale-category.model';
import { OilFilterRequest } from './oil-filter.request.model';
import { TyreFilterRequest } from './tyre-filter.request.model';
import { SEARCH_MODE } from '../enums/search-mode.enum';
import { BatteryFilterRequest } from './battery-filter-request.model';
import { BulbFilterRequest } from './bulb-filter-request.model';
import { FilterCategoryRequest } from './filter-category-request.model';

export class NonMerkmaleCategoryRequest extends FilterCategoryRequest {

    // tslint:disable-next-line: variable-name
    total_elements_of_searching: number;

    battery_search_request?: BatteryFilterRequest;
    bulb_search_request?: BulbFilterRequest;
    oil_search_request?: OilFilterRequest;
    tyre_search_request?: TyreFilterRequest;
    motor_tyre_search_request?: TyreFilterRequest;

    constructor(request: NonMerkmaleCategory, hasChanged = false) {
        super(request);
        if (request) {
            this.total_elements_of_searching = request.totalElementsOfSearching;
        }
        if (hasChanged) {
            this.context_key = '';
        }
    }

    toRequest(type: string, data: NonMerkmaleCategory) {
        switch (type) {
            case SEARCH_MODE.BATTERY_SEARCH:
                this.battery_search_request = new BatteryFilterRequest(data.batterySearchRequest);
                break;
            case SEARCH_MODE.BULB_SEARCH:
                this.bulb_search_request = new BulbFilterRequest(data.bulbSearchRequest);
                break;
            case SEARCH_MODE.OIL_SEARCH:
                this.oil_search_request = new OilFilterRequest(data.oilSearchRequest);
                break;
            case SEARCH_MODE.TYRES_SEARCH:
                this.tyre_search_request = new TyreFilterRequest(data.tyreSearchRequest).toTyreRequest(data.tyreSearchRequest);
                break;
            case SEARCH_MODE.MOTOR_TYRES_SEARCH:
                this.motor_tyre_search_request = new TyreFilterRequest(data.motorTyreSearchRequest)
                                                        .toTyreMotorRequest(data.motorTyreSearchRequest);
                break;
        }
    }
}
