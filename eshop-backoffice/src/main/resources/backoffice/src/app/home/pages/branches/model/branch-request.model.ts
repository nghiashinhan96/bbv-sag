import { EMPTY_STRING } from 'src/app/core/conts/app.constant';

export interface BranchRequestModel {
    branchNr: string;
    branchCode: string;
    openingTime: string;
    closingTime: string;
    lunchStartTime: string;
    lunchEndTime: string;
    orderDescByBranchNr: boolean;
    orderDescByBranchCode: boolean;
    orderDescByOpeningTime: boolean;
    orderDescByClosingTime: boolean;
    orderDescByLunchStartTime: boolean;
    orderDescByLunchEndTime: boolean;
}

export class BranchRequest implements BranchRequestModel {
    branchNr = EMPTY_STRING;
    branchCode = EMPTY_STRING;
    openingTime = EMPTY_STRING;
    closingTime = EMPTY_STRING;
    lunchStartTime = EMPTY_STRING;
    lunchEndTime = EMPTY_STRING;
    orderDescByBranchNr = null;
    orderDescByBranchCode = null;
    orderDescByOpeningTime = null;
    orderDescByClosingTime = null;
    orderDescByLunchStartTime = null;
    orderDescByLunchEndTime = null;
}
