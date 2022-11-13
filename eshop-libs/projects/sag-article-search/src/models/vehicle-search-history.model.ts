import { SEARCH_MODE } from "sag-article-list";
import { SearchTermUtil } from "../utils/search-term.util";

export class VehicleSearchHistory {
    createdBySales: string;
    firstName: string;
    lastName: string;
    searchTerm: string;
    searchMode: string;
    selectDate: boolean;
    vehicleId: string;
    vehicleName: string;
    fullName: string;
    jsonSearchTerm?: string;
    vehicleClass?: string;
    constructor(data?) {
        if (!data) {
            return;
        }

        this.createdBySales = data.createdBySales;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.searchTerm = data.search_term;
        this.selectDate = data.select_date;
        this.vehicleId = data.vehicle_id;
        this.vehicleName = data.vehicle_name;
        this.fullName = data.fullName;
        this.searchMode = data.searchMode;
        this.jsonSearchTerm = data.json_search_term;
        this.vehicleClass = data.vehicle_class;

        if (this.searchMode === SEARCH_MODE.MAKE_MODEL_TYPE) {
            this.jsonSearchTerm = data.search_term;
            this.searchTerm = SearchTermUtil.getMakeModelTypeSearchTerm(data.search_term);
        }
    }
}
