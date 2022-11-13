import { DeliveryProfileCriteriaSortModel } from './delivery-profile-criteria-sort.model';
import { DateUtil } from 'src/app/core/utils/date.util';

export class DeliveryProfileCriteria {
    page = 0;
    size = 10;
    country?: string;
    deliveryProfileName?: string;
    distributionBranchCode?: number;
    deliveryBranchCode?: number;
    vendorCutOffTime?: Date;
    lastestTime?: Date;
    lastDelivery?: Date;
    deliveryDuration?: string;
    sort?: DeliveryProfileCriteriaSortModel = new DeliveryProfileCriteriaSortModel();

    readonly SORT_FIELD_MAP = {
        country: 'orderByCountryDesc',
        deliveryProfileName: 'orderByDeleviryProfileNameDesc',
        distributionBranchCode: 'orderByDistributionBranchCodeDesc',
        deliveryBranchCode: 'orderByDeliveryBranchCodeDesc',
        vendorCutOffTime: 'orderByVendorCutOffTimeDesc',
        lastDelivery: 'orderByLastDeliveryDesc',
        latestTime: 'orderByLastestTimeDesc',
        deliveryDuration: 'orderByDeliveryDuration',
    };

    constructor(json?: DeliveryProfileCriteria) {
        if (!json) {
            return;
        }
        this.page = json.page;
        this.size = json.size;
        this.country = json.country;
        this.deliveryProfileName = json.deliveryProfileName;
        this.distributionBranchCode = json.distributionBranchCode;
        this.deliveryBranchCode = json.deliveryBranchCode;
        this.vendorCutOffTime = null;
        if (!!json.vendorCutOffTime) {
            this.vendorCutOffTime = new Date(json.vendorCutOffTime);
        }
        this.lastestTime = null;
        if (!!json.lastestTime) {
            this.lastestTime = new Date(json.lastestTime);
        }
        this.lastDelivery = null;
        if (!!json.lastDelivery) {
            this.lastDelivery = new Date(json.lastDelivery);
        }
        this.deliveryDuration = json.deliveryDuration;
        this.sort = new DeliveryProfileCriteriaSortModel(json.sort);
    }

    setSearchModel(json: DeliveryProfileCriteria) {
        this.country = json.country;
        this.deliveryProfileName = json.deliveryProfileName;
        this.distributionBranchCode = json.distributionBranchCode;
        this.deliveryBranchCode = json.deliveryBranchCode;
        this.vendorCutOffTime = null;
        if (!!json.vendorCutOffTime) {
            this.vendorCutOffTime = new Date(json.vendorCutOffTime);
        }
        this.lastestTime = null;
        if (!!json.lastestTime) {
            this.lastestTime = new Date(json.lastestTime);
        }
        this.lastDelivery = null;
        if (!!json.lastDelivery) {
            this.lastDelivery = new Date(json.lastDelivery);
        }
        this.deliveryDuration = json.deliveryDuration;
    }

    setSortModel(sortField, isDesc) {
        const mappedField = this.SORT_FIELD_MAP[sortField];
        this.sort = new DeliveryProfileCriteriaSortModel({
            [mappedField]: Boolean(isDesc)
        });
    }

    setPage(p: number) {
        this.page = p;
    }

    setSize(s: number) {
        this.size = s;
    }

    get dto() {
        return {
            page: this.page,
            size: this.size,
            country: this.country,
            deliveryProfileName: this.deliveryProfileName,
            distributionBranchCode: this.distributionBranchCode,
            deliveryBranchCode: this.deliveryBranchCode,
            vendorCutOffTime: this.vendorCutOffTime && DateUtil.dateToString(this.vendorCutOffTime),
            lastestTime: this.lastestTime && DateUtil.dateToString(this.lastestTime),
            lastDelivery: this.lastDelivery && DateUtil.dateToString(this.lastDelivery),
            deliveryDuration: this.deliveryDuration && DateUtil.durationObjectToSeconds(this.deliveryDuration),
            sort: this.sort
        };
    }
}
