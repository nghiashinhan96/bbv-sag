import { Constant } from 'src/app/core/conts/app.constant';

export interface BranchModel {
    branchNr: number;
    branchCode: string;
    wssBranchOpeningTimes: WssBranchOpeningTime[];
    addressStreet: string;
    addressCity: string;
    addressDesc: string;
    addressCountry: string;
    countryId: number;
    zip: string;
    orgId: number;
    regionId: string;
    primaryPhone: string;
    primaryFax: string;
    primaryEmail: string;
    validForKSL: boolean;
    primaryUrl: string;
}

export class Branch implements BranchModel {
    branchNr = 0;
    branchCode = Constant.EMPTY_STRING;
    wssBranchOpeningTimes: WssBranchOpeningTime[] = [];
    addressStreet = Constant.EMPTY_STRING;
    addressCity = Constant.EMPTY_STRING;
    addressDesc = Constant.EMPTY_STRING;
    addressCountry = Constant.EMPTY_STRING;
    countryId = null;
    zip = Constant.EMPTY_STRING;
    orgId = 0;
    regionId = Constant.EMPTY_STRING;
    primaryPhone = Constant.EMPTY_STRING;
    primaryFax = Constant.EMPTY_STRING;
    primaryEmail = Constant.EMPTY_STRING;
    validForKSL = false;
    primaryUrl = Constant.EMPTY_STRING;

    constructor() { }
}

export interface CountryBranchModel {
    id: number;
    shortCode: string;
    value: string;
    label: string;
}

export class WssBranchOpeningTime {
    id: number;
    weekDay: string;
    wssBranchId: number;
    openingTime: string;
    closingTime: string;
    lunchStartTime: string;
    lunchEndTime: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.weekDay = data.weekDay;
        this.wssBranchId = data.wssBranchId;
        this.openingTime = data.openingTime;
        this.closingTime = data.closingTime;
        this.lunchStartTime = data.lunchStartTime;
        this.lunchEndTime = data.lunchEndTime;
    }
}