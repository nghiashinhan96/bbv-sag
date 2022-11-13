import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

export interface OpeningDayModelResponse {
    content: OpeningDayModel[];
    first: boolean;
    last: boolean;
    pageable: {};
    numberOfElements: number;
    size: number;
    sort: {};
    totalElements: number;
    totalPages: number;
}
export interface OpeningDayModel {
    id: number;
    date: Date;
    workingDayCode: string;
    countryName: string;
    expAffiliate: string;
    expBranchInfo: string;
    expWorkingDayCode: string;
    expDeliveryAddressId: string;
}

export interface OpeningDayRequestModel {
    id: number;
    date: string;
    countryId: number;
    workingDayCode: string;
    exceptions: OpeningDayException;
}

export interface OpeningDayExceptionRequest {
    affiliate: string;
    branches: string[];
    deliveryAdrressIDs: string[];
    workingDayCode: string;
}

export interface SearchCriteriaModel {
    dateFrom: string;
    dateTo: string;
    countryName: string;
    workingDayCode: string;
    expAffiliate: string;
    expBranchInfo: string;
    expWorkingDayCode: string;
    expDeliveryAddressId: string;
    orderDescByDate: boolean;
    orderDescByCountryName: boolean;
}

export class OpeningDayRequest implements OpeningDayRequestModel {
    public id = null;
    public countryId = 0;
    public workingDayCode = EMPTY_STRING;
    public exceptions = null;
    constructor(public date: string) { }
}

export class OpeningDayException implements OpeningDayExceptionRequest {
    public affiliate = EMPTY_STRING;
    public branches = [];
    public workingDayCode = EMPTY_STRING;
    public deliveryAdrressIDs = [];

    constructor() { }
}

export class OpeningDay implements OpeningDayModel {
    public id = 0;
    public date = new Date();
    public workingDayCode = EMPTY_STRING;
    public countryName = EMPTY_STRING;
    public expAffiliate = EMPTY_STRING;
    public expBranchInfo = EMPTY_STRING;
    public expWorkingDayCode = EMPTY_STRING;
    public expDeliveryAddressId = EMPTY_STRING;
    public orderDescByDate = false;
    public orderDescByCountryName = false;

    constructor() { }
}

export class SearchCriteria implements SearchCriteriaModel {
    dateFrom = EMPTY_STRING;
    dateTo = EMPTY_STRING;
    countryName = null;
    workingDayCode = null;
    expAffiliate = null;
    expBranchInfo = null;
    expWorkingDayCode = null;
    expDeliveryAddressId = null;
    orderDescByDate = true;
    orderDescByCountryName = false;
}
