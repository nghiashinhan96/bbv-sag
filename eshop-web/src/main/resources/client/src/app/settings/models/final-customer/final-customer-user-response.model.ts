import { FinalCustomerUser } from './final-customer-user.model';

export class FinalCustomerUserResponse {
    data: FinalCustomerUser[];
    first: boolean;
    last: boolean;
    pageNr: number;
    numberOfElements: number;
    pageable: any;
    size: number;
    sort: any;
    totalElements: number;
    total: number;

    constructor(response?: any) {
        if (!response) {
            return;
        }
        this.data = (response.content || []).map(user => new FinalCustomerUser(user));
        this.first = response.first;
        this.last = response.last;
        this.pageNr = response.pageNr;
        this.numberOfElements = response.numberOfElements;
        this.pageable = response.pageable;
        this.size = response.size;
        this.sort = response.sort;
        this.totalElements = response.totalElements;
        this.total = response.total;
    }
}
