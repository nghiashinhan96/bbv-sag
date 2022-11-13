export class InvoiceAddress {
    id: string;
    street: string;
    postOfficeBox: string;
    active: boolean;
    postCode: string;
    city: string;
    countryCode: string;
    country: string;
    state: string;
    addressTypeCode: string;
    addressType: string;
    addressId: string;
    streetNumber: string;
    primary: boolean;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.street = data.street;
            this.postOfficeBox = data.postOfficeBox;
            this.active = data.active;
            this.postCode = data.postCode;
            this.city = data.city;
            this.countryCode = data.countryCode;
            this.country = data.country;
            this.state = data.state;
            this.addressTypeCode = data.addressTypeCode;
            this.addressType = data.addressType;
            this.addressId = data.addressId;
            this.streetNumber = data.streetNumber;
            this.primary = data.primary;
        }
    }
}
