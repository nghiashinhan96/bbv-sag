export class DeliveryProfileCriteriaSortModel {
    orderByCountryDesc?= true;
    orderByDeleviryProfileNameDesc?: boolean;
    orderByDistributionBranchCodeDesc?: boolean;
    orderByDeliveryBranchCodeDesc?: boolean;
    orderByVendorCutOffTimeDesc?: boolean;
    orderByLastestTimeDesc?: boolean;
    orderByLastDeliveryDesc?: boolean;
    orderByDeliveryDuration?: boolean;

    constructor(json?: DeliveryProfileCriteriaSortModel) {
        if (!json) {
            return;
        }
        this.orderByCountryDesc = json.orderByCountryDesc;
        this.orderByDeleviryProfileNameDesc = json.orderByDeleviryProfileNameDesc;
        this.orderByDistributionBranchCodeDesc = json.orderByDistributionBranchCodeDesc;
        this.orderByDeliveryBranchCodeDesc = json.orderByDeliveryBranchCodeDesc;
        this.orderByVendorCutOffTimeDesc = json.orderByVendorCutOffTimeDesc;
        this.orderByLastestTimeDesc = json.orderByLastestTimeDesc;
        this.orderByLastDeliveryDesc = json.orderByLastDeliveryDesc;
        this.orderByDeliveryDuration = json.orderByDeliveryDuration;
    }
}
