import { DateUtil } from 'src/app/core/utils/date.util';

export class DeliveryProfileModel {
    id: number = null;
    country: string;
    deliveryProfileId: string;
    deliveryProfileName: string;
    distributionBranchId: string;
    deliveryBranchId: string;
    vendorCutOffTime: Date;
    lastDelivery: Date;
    latestTime: Date;
    deliveryDuration: any;
    nextDay: number;

    constructor(json?: DeliveryProfileModel) {
        if (!json) {
            return;
        }
        this.id = json.id;
        this.country = json.country.toLowerCase();
        this.deliveryProfileId = json.deliveryProfileId.toString();
        this.deliveryProfileName = json.deliveryProfileName;
        this.distributionBranchId = json.distributionBranchId.toString();
        this.deliveryBranchId = json.deliveryBranchId.toString();

        if (json.vendorCutOffTime instanceof Date) {
            this.vendorCutOffTime = new Date(json.vendorCutOffTime);
        } else {
            this.vendorCutOffTime = DateUtil.stringToDate(json.vendorCutOffTime);
        }

        if (json.lastDelivery instanceof Date) {
            this.lastDelivery = new Date(json.lastDelivery);
        } else {
            this.lastDelivery = DateUtil.stringToDate(json.lastDelivery);
        }

        if (json.latestTime instanceof Date) {
            this.latestTime = new Date(json.latestTime);
        } else {
            this.latestTime = DateUtil.stringToDate(json.latestTime);
        }

        const seconds = Number(json.deliveryDuration);
        if (Number.isNaN(seconds)) {
            this.deliveryDuration = { ...json.deliveryDuration };
        } else {
            this.deliveryDuration = DateUtil.secondsToDurationObject(json.deliveryDuration);
        }

        this.nextDay = json.nextDay;
    }

    get dto() {
        return {
            id: this.id,
            country: this.country,
            deliveryProfileId: this.deliveryProfileId,
            deliveryProfileName: this.deliveryProfileName,
            distributionBranchId: this.distributionBranchId,
            deliveryBranchId: this.deliveryBranchId,
            vendorCutOffTime: this.vendorCutOffTime && DateUtil.dateToString(this.vendorCutOffTime),
            lastDelivery: this.lastDelivery && DateUtil.dateToString(this.lastDelivery),
            latestTime: this.latestTime && DateUtil.dateToString(this.latestTime),
            deliveryDuration: this.deliveryDuration && DateUtil.durationObjectToSeconds(this.deliveryDuration),
            nextDay: this.nextDay
        };
    }
}
