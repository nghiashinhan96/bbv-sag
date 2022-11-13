export class UserAddress {
    id: number;
    addressId: number;
    salutationCode: string;
    salutation: string;
    salutationDesc: string;
    keyword: string;
    surname: string;
    name: string;
    companyName: string;
    street: string;
    postOfficeBox: string;
    section: string;
    addon: string;
    active: boolean;
    postCode: string;
    city: string;
    countryCode: string;
    country: string;
    countryDesc: string;
    stateCode: string;
    state: string;
    stateDesc: string;
    addressTypeCode: string;
    addressType: string;
    addressTypeDesc: string;
    fullAddress: string;
    
    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.addressId = data.addressId;
            this.salutationCode = data.salutationCode;
            this.salutation = data.salutation;
            this.salutationDesc = data.salutationDesc;
            this.keyword = data.keyword;
            this.surname = data.surname;
            this.name = data.name;
            this.companyName = data.companyName;
            this.street = data.street;
            this.postOfficeBox = data.postOfficeBox;
            this.section = data.section;
            this.addon = data.addon;
            this.active = data.active;
            this.postCode = data.postCode;
            this.city = data.city;
            this.countryCode = data.countryCode;
            this.country = data.country;
            this.countryDesc = data.countryDesc;
            this.stateCode = data.stateCode;
            this.state = data.state;
            this.stateDesc = data.stateDesc;
            this.addressTypeCode = data.addressTypeCode;
            this.addressType = data.addressType;
            this.addressTypeDesc = data.addressTypeDesc;
            this.fullAddress = this.getFullAddress();
        }
    }

    public static getCustomerCity(user: any) {
        return ((user && user.addresses) || []).find(((address: UserAddress) => address.addressTypeCode === 'DEFAULT'));
    }

    getFullAddress() {
        return [
            this.companyName,
            this.street,
            this.postCode,
            this.city,
            this.country
        ].filter(item => (item || '').trim()).join(', ');
    }

}
