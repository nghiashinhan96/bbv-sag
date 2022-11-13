import { FinalCustomerModel } from './final-customer.model';

export class FinalCustomerSearchTerm {
    name: string;
    finalCustomerType: string = null;
    address: string;
    contactInfo: string;
    status: string;

    constructor(data?: FinalCustomerModel) {
        if (data) {
            this.name = data.name;
            this.finalCustomerType = data.finalCustomerType;
            this.address = data.addressInfo;
            this.contactInfo = data.contactInfo;
            this.status = data.status;
        }
    }
}
