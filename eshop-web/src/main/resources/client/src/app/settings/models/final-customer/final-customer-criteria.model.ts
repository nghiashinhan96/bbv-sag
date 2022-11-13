import { Constant } from 'src/app/core/conts/app.constant';

export class FinalCustomerSearchCriteria {
    term: {
        name: string;
        finalCustomerType: string;
        address: string;
        contactInfo: string;
        status: string;
    } = {
            name: Constant.EMPTY_STRING,
            finalCustomerType: null,
            address: Constant.EMPTY_STRING,
            contactInfo: Constant.EMPTY_STRING,
            status: null
        };
    sort: {
        orderDescByName: boolean;
    } = {
            orderDescByName: false
        };

    constructor(term?, sort?) {
        if (term) {
            this.term.name = term.name;
            this.term.finalCustomerType = term.finalCustomerType;
            this.term.address = term.addressInfo;
            this.term.contactInfo = term.contactInfo;
            this.term.status = term.status;
        }
        if (sort) {
            this.sort.orderDescByName = sort.orderDescByName;
        }
    }

    resetSort() {
        this.sort.orderDescByName = null;
    }
}
