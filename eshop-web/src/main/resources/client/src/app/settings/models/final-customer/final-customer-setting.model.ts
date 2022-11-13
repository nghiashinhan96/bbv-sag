export class FinalCustomerSetting {
    address1: string;
    address2: string;
    customerNumber: string;
    email: string;
    fax: string;
    firstname: string;
    phone: string;
    place: string;
    poBox: string;
    postCode: string;
    salutaion: string;
    status: boolean;
    street: string;
    surname: string;
    tdWorkshop: string;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.address1 = data.address1;
        this.address2 = data.address2;
        this.customerNumber = data.customerNumber;
        this.email = data.email;
        this.fax = data.fax;
        this.firstname = data.firstname;
        this.phone = data.phone;
        this.place = data.place;
        this.poBox = data.poBox;
        this.postCode = data.postCode;
        this.salutaion = data.salutaion;
        this.status = data.status;
        this.street = data.street;
        this.surname = data.surname;
        this.tdWorkshop = data.tdWorkshop;
    }
}
