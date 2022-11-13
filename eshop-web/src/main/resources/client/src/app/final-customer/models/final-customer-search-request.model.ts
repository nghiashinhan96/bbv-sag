import { FinalCustomerSearchCriteria } from './final-customer-search-criteria.model';

export class FinalCustomerSearchRequestModel {
    criteria: FinalCustomerSearchCriteria = new FinalCustomerSearchCriteria();
    pageNr;
    size;
    constructor(request?) {
        if (request) {
            this.criteria = new FinalCustomerSearchCriteria(request.criteria);
            this.pageNr = request.pageNr;
            this.size = request.size;
        }
    }
}
