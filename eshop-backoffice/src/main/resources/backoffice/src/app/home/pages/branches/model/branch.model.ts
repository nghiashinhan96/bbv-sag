import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import { BranchOpeningTime } from './branch-response.model';

export interface BranchModel {
    branchNr: number;
    branchCode: string;
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
    hideFromCustomers: boolean;
    hideFromSales: boolean;
    branchOpeningTimes: BranchOpeningTime[];
}

export class Branch implements BranchModel {
    branchNr = 0;
    branchCode = EMPTY_STRING;
    addressStreet = EMPTY_STRING;
    addressCity = EMPTY_STRING;
    addressDesc = EMPTY_STRING;
    addressCountry = EMPTY_STRING;
    countryId = null;
    zip = EMPTY_STRING;
    orgId = 0;
    regionId = EMPTY_STRING;
    primaryPhone = EMPTY_STRING;
    primaryFax = EMPTY_STRING;
    primaryEmail = EMPTY_STRING;
    validForKSL = false;
    primaryUrl = EMPTY_STRING;
    hideFromCustomers = false;
    hideFromSales = false;
    branchOpeningTimes: BranchOpeningTime[] = [];

    constructor() { }
}

export interface CountryBranchModel {
    id: number;
    shortCode: string;
    value: string;
    label: string;
}
