export class CustomerAddressModel {
    id: string;
    // Customer address info
    keyword: string;
    surname: string;
    name: string;
    companyName: string;
    salutation: string;
    salutationCode: string;
    salutationDesc: string;
    // Customer address
    addressType: string;
    addressTypeCode: string;
    addressTypeDesc: string;
    addressId: string;
    street: string;
    streetNumber: string;
    postOfficeBox: string;
    section: string;
    addon: string;
    active: boolean;
    postCode: string;
    city: string;
    state: string;
    stateCode: string;
    stateDesc: string;
    countryCode: string;
    country: string;
    countryDesc: string;
    primary: boolean;
    fullAddress: string;
}
