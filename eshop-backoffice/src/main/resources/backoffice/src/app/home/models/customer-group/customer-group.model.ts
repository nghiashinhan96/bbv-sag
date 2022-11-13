export const MAXIMUM_CUSTOMER_NR = 4;

export class CustomerGroupModel {
    affiliate: string;
    collectionName: string;
    customerNr: string;
    showIconInfo = false;
    collectionShortName: string;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.affiliate = data.affiliate;
        this.collectionName = data.collectionName;
        this.customerNr = data.customerNr;
        this.collectionShortName = data.collectionShortName;

        if (data.customerNr) {
            const customerNr = data.customerNr.replace(' ', '').split(', ');

            if (customerNr.length > MAXIMUM_CUSTOMER_NR) {
                this.showIconInfo = true;
            }

            this.customerNr = customerNr.join(' - ');
        }
    }
}
