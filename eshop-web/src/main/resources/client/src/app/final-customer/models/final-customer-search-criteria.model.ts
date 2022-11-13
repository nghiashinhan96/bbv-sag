import { FinalCustomerSearchTerm } from './final-customer-search-term.model';
import { FinalCustomerSearchSort } from './final-customer-search-sort.model';

export class FinalCustomerSearchCriteria {
    term: FinalCustomerSearchTerm = new FinalCustomerSearchTerm();
    sort: FinalCustomerSearchSort = new FinalCustomerSearchSort();
    constructor(data?) {
        if (data) {
            this.term = new FinalCustomerSearchTerm(data.term);
            this.sort = new FinalCustomerSearchSort(data.sort);
        }
    }
}
