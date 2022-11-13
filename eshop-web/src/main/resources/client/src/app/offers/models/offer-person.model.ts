export class OfferPerson {
    id: number;
    additionalAddress1 = '';
    additionalAddress2 = '';
    companyName = '';
    createdUserId = '';
    email = '';
    fax = '';
    firstName = '';
    lastName = '';
    organisationId = '';
    phone = '';
    place = '';
    poBox = '';
    postCode = '';
    road = '';
    salutation = '';
    type = '';

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.additionalAddress1 = data.additionalAddress1;
        this.additionalAddress2 = data.additionalAddress2;
        this.companyName = data.companyName;
        this.createdUserId = data.createdUserId;
        this.email = data.email;
        this.fax = data.fax;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.organisationId = data.organisationId;
        this.phone = data.phone;
        this.place = data.place;
        this.poBox = data.poBox;
        this.postCode = data.postCode;
        this.road = data.road;
        this.salutation = data.salutation;
        this.type = data.type;
    }
}
