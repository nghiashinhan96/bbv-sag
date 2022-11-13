export class ChangePasswordModel {
    password: string;
    redirectUrl: string;
    constructor(data) {
        if (!data) {
            return;
        }
        this.password = data.password;
        this.redirectUrl = data.redirectUrl;
    }
}
