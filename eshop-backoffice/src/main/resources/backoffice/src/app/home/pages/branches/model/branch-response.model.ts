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
    closingTime: string;
    id: number;
    lunchEndTime: string;
    lunchStartTime: string;
    openingTime: string;
    branchOpeningTimes: BranchOpeningTime[];
}

export class BranchOpeningTime {
    id: number;
    weekDay: string;
    branchId: number;
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
        this.branchId = data.branchId;
        this.openingTime = data.openingTime;
        this.closingTime = data.closingTime;
        this.lunchStartTime = data.lunchStartTime;
        this.lunchEndTime = data.lunchEndTime;
    }
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
