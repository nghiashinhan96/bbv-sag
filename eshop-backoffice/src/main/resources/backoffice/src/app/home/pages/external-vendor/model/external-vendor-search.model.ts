import { ExternalVendorSort } from './external-vendor-sort.model';
import { ExternalVendorConstants } from '../external-vendor.constant';

export class ExternalVendorSearch {
    country: number;
    vendorId: string;
    vendorName: string;
    vendorPriority: string;
    deliveryProfileName: number;
    availabilityTypeId: number;
    page: number;
    id: number;
    size = 10;
    sort = new ExternalVendorSort();

    constructor(criteria?: ExternalVendorSearch) {
        if (!criteria) {
            return;
        }
        this.id = criteria.id;
        this.country = criteria.country;
        this.vendorId = criteria.vendorId;
        this.vendorName = criteria.vendorName;
        this.vendorPriority = criteria.vendorPriority;
        this.deliveryProfileName = criteria.deliveryProfileName;
        this.availabilityTypeId = criteria.availabilityTypeId;
        this.page = criteria.page;
        this.size = criteria.size;
        this.sort = criteria.sort;
    }

    resetSort() {
        this.sort = new ExternalVendorSort();
    }

    getSortFieldByCriteriaName(name) {
        return ExternalVendorConstants.SORT_FIELD_MAP[name];
    }
}


