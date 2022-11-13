import { VehicleRequestTermModel } from './vehicle-request-term.model';
import { VehicleRequestFilterModel } from './vehicle-request-filter.model';
import { VehicleRequestSortModel } from './vehicle-request-sort.model';

export class VehicleRequestModel {
    term: VehicleRequestTermModel = new VehicleRequestTermModel();
    filtering: VehicleRequestFilterModel = new VehicleRequestFilterModel();
    sort: VehicleRequestSortModel = new VehicleRequestSortModel();
    aggregation = true;
    constructor(json?: VehicleRequestModel) {
        if (json) {
            this.term = new VehicleRequestTermModel(json.term);
            this.filtering = new VehicleRequestFilterModel(json.filtering);
            this.sort = new VehicleRequestSortModel(json.sort);
            this.aggregation = json.aggregation;
        }
    }
}
