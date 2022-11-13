export class RegistrationModel {
    affiliate: string;
    userName: string;
    firstName: string;
    surName: string;
    email: string;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.affiliate = data.affiliate;
        this.userName = data.userName;
        this.firstName = data.firstName;
        this.surName = data.surName;
        this.email = data.email;
    }
}
