export class ExternalVendorSort {
    orderByCountryDesc: boolean;
    orderByVendorIdDesc: boolean;
    orderByVendorNameDesc: boolean;
    orderByVendorPriorityDesc: boolean;
    orderByDeleviryProfileNameDesc: boolean;
    orderByAvailabilityTypeDesc: boolean;
    constructor(sort?: ExternalVendorSort) {
        if (sort) {
            this.orderByCountryDesc = sort.orderByCountryDesc;
            this.orderByVendorIdDesc = sort.orderByVendorIdDesc;
            this.orderByVendorNameDesc = sort.orderByVendorNameDesc;
            this.orderByVendorPriorityDesc = sort.orderByVendorPriorityDesc;
            this.orderByDeleviryProfileNameDesc = sort.orderByDeleviryProfileNameDesc;
            this.orderByAvailabilityTypeDesc = sort.orderByAvailabilityTypeDesc;
        }
    }
}
