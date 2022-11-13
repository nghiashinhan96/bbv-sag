import { EMPTY_STR } from 'angular-mydatepicker';

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
    countryId: string;
    exceptions: OpeningDayException;
}

export interface OpeningDayRequestModel {
    id: number;
    date: string;
    countryId: number;
    workingDayCode: string;
    exceptions: OpeningDayException;
}

export interface OpeningDayExceptionRequest {
    branches: string[];
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
    public workingDayCode = EMPTY_STR;
    public exceptions = null;
    constructor(public date: string) { }
}

export class OpeningDayException implements OpeningDayExceptionRequest {
    public branches = [];
    public workingDayCode = EMPTY_STR;

    constructor() { }
}

export class OpeningDay implements OpeningDayModel {
    public id = 0;
    public date = new Date();
    public workingDayCode = EMPTY_STR;
    public countryName = EMPTY_STR;
    public expAffiliate = EMPTY_STR;
    public expBranchInfo = EMPTY_STR;
    public expWorkingDayCode = EMPTY_STR;
    public expDeliveryAddressId = EMPTY_STR;
    public orderDescByDate = false;
    public orderDescByCountryName = false;
    public countryId: string;
    public exceptions: OpeningDayException;
    constructor() { }
}

export class SearchCriteria implements SearchCriteriaModel {
    dateFrom = EMPTY_STR;
    dateTo = EMPTY_STR;
    countryName = null;
    workingDayCode = null;
    expAffiliate = null;
    expBranchInfo = null;
    expWorkingDayCode = null;
    expDeliveryAddressId = null;
    orderDescByDate = true;
    orderDescByCountryName = false;
}
