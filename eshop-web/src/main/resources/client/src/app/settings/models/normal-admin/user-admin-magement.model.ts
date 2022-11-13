export class AdminManagementUser {
    garageName: string;
    salutation: string;
    role: string;
    firstName: string;
    lastName: string;
    id: number;
    canDelete: boolean;
    userName: string;
    userAdmin: boolean;
    userActive: boolean;

    constructor(data?: any) {
        if (data) {
            this.garageName = data.garageName;
            this.salutation = data.salutation;
            this.role = data.role;
            this.firstName = data.firstName;
            this.lastName = data.lastName;
            this.id = data.id;
            this.canDelete = data.canDelete;
            this.userName = data.userName;
            this.userAdmin = data.userAdmin;
            this.userActive = data.userActive;
        }
    }
}
