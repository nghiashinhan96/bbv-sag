import { Constant } from 'src/app/core/conts/app.constant';

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
    branchNr = Constant.EMPTY_STRING;
    branchCode = Constant.EMPTY_STRING;
    openingTime = Constant.EMPTY_STRING;
    closingTime = Constant.EMPTY_STRING;
    lunchStartTime = Constant.EMPTY_STRING;
    lunchEndTime = Constant.EMPTY_STRING;
    orderDescByBranchNr = null;
    orderDescByBranchCode = null;
    orderDescByOpeningTime = null;
    orderDescByClosingTime = null;
    orderDescByLunchStartTime = null;
    orderDescByLunchEndTime = null;
}
