
export class SalesSearchItemModel {

    firstName: string;
    lastName: string;
    primaryContactEmail: string;
    personalNumber: string;
    gender: string;
    legalEntityId: string;

    orderDescByFirstName: boolean;
    orderDescByLastName: boolean;
    orderDescByPrimaryContactEmail: boolean;
    orderDescByPersonalNumber: boolean;
    orderDescByGender: boolean;
    orderDescBylegalEntityId: boolean;



    constructor(data?) {
        if (!data) {
            return;
        }
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.primaryContactEmail = data.primaryContactEmail;
        this.personalNumber = data.personalNumber;
        this.gender = data.gender;
        this.legalEntityId = data.legalEntityId;

        this.orderDescByFirstName = data.orderDescByFirstName;
        this.orderDescByLastName = data.orderDescByLastName;
        this.orderDescByPrimaryContactEmail = data.orderDescByPrimaryContactEmail;
        this.orderDescByPersonalNumber = data.orderDescByPersonalNumber;
        this.orderDescByGender = data.orderDescByGender;
        this.orderDescBylegalEntityId = data.orderDescBylegalEntityId;
    }

    public static getEmptyModel(): SalesSearchItemModel {
        return new SalesSearchItemModel();
    }
}

