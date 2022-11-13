
export class MessageFormModel {
    title: string;
    active: boolean;
    locationTypeId: number;
    affiliateShortName: Array<any>;
    customerNr: string;
    supportedAffiliates: any[];
    roleTypeId: number;
    accessRightId: number;
    typeId: number;
    visibilityId: number;
    styleId: number;
    areaId: number;
    subAreaId: number;
    dateValidFrom: any;
    dateValidTo: any;
    ssoTraining: boolean;
    constructor(data?) {
        if (!data) {
            return;
        }
        this.title = data.title;
        this.active = data.active;
        this.locationTypeId = data.locationTypeId;
        this.affiliateShortName = data.affiliateShortName;
        this.customerNr = data.customerNr;
        this.supportedAffiliates = data.supportedAffiliates;
        this.roleTypeId = data.roleTypeId;
        this.accessRightId = data.accessRightId;
        this.typeId = data.typeId;
        this.visibilityId = data.visibilityId;
        this.styleId = data.styleId;
        this.areaId = data.areaId;
        this.subAreaId = data.subAreaId;
        this.dateValidFrom = data.dateValidFrom;
        this.dateValidTo = data.dateValidTo;
        this.ssoTraining = data.ssoTraining;
    }
}
