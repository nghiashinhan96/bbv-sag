
export class SalesSearchResultItemModel {
    id: number;
    firstName: string;
    lastName: string;
    primaryContactEmail: string;
    personalNumber: string;
    gender: string;
    legalEntityId: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.primaryContactEmail = data.primaryContactEmail;
        this.personalNumber = data.personalNumber;
        this.gender = data.gender;
        this.legalEntityId = data.legalEntityId;
    }
}
