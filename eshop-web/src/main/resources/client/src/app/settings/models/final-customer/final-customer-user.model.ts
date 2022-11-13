export class FinalCustomerUser {
    firstName: string;
    fullName: string;
    lastName: string;
    salutation: string;
    userId: number;
    type: string;
    userEmail: string;
    userName: number;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.salutation = data.salutation;
        this.userId = data.userId;
        this.fullName = data.fullName;
        this.type = data.type;
        this.userEmail = data.userEmail;
        this.userName = data.userName;
    }
}
