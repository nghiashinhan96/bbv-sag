import { TourAssignModel } from './tour-assign.model';

export class DeliveryProfileModel {
    name: string;
    description: string;
    branchCode: string;
    id: string;
    orgId: string;
    wssDeliveryProfileToursDtos: TourAssignModel[];

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.name = data.name;
        this.description = data.description;
        this.branchCode = data.branchCode;
        this.wssDeliveryProfileToursDtos = (data.wssDeliveryProfileToursDtos || []).map(tour => new TourAssignModel(tour))
    }
}

export class DeliveryProfileRequestModel {
    orgId: string;
    orderDescByProfileName: boolean;
    orderDescByProfileDescription: boolean;
    orderDescByBranchCode: boolean
}