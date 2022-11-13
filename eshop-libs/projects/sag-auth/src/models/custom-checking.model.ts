export class CustomerCheckingModel {
    customerNumber: string;
    affiliate: string;
    postCode: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.customerNumber = data.customerNumber;
        this.affiliate = data.affiliate;
        this.postCode = data.postCode;
    }
}
