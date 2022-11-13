export class FinalCustomerAddressModel {
    address1: string;
    address2: string;
    customerName: string;
    place: string;
    postCode: string;
    street: string;
    constructor(json?) {
        if (json) {
            this.address1 = json.address1;
            this.address2 = json.address2;
            this.customerName = json.customerName;
            this.place = json.place;
            this.postCode = json.postCode;
            this.street = json.street;
        }
    }
}
