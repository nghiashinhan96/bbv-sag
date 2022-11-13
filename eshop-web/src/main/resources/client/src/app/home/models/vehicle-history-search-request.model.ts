import { omitBy } from 'lodash';
export class HistorySearchRequest {
    filterMode: string;
    fromDate?: any;
    toDate?: any;
    orderDescBySearchTerm: boolean;
    orderDescBySelectDate = true;
    orderDescByVehicleName?: boolean;
    searchTerm?: string;
    selectDate?: any;
    userId?: string;
    vehicleName?: string;
    fullName?: string;
    pageNumber = 0;
    searchType: string;
    vehicleClass?: string;

    constructor(data?: any) {
    }

    createDto() {
        let dtoObject: any = { ...this };
        dtoObject = omitBy(dtoObject, (val) => val === null);
        return dtoObject;
    }
}
