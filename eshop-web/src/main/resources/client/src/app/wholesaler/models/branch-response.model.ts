import { WssBranchOpeningTime } from "./branch-opening-time.model";

export interface BranchResponseModel {
    content: BranchDto[];
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    pageable: { pageable: Pageable };
    size: number;
    sort: { sort: Sort };
    totalElements: number;
    totalPages: number;
}

export interface BranchDto {
    branchCode: string;
    branchNr: number;
    branchAddress: string;
    id: number;
    wssBranchOpeningTimes: WssBranchOpeningTime[];
}

interface Pageable {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: { sort: Sort };
    unpaged: boolean;
}

interface Sort {
    sorted: boolean;
    unsorted: boolean;
}
