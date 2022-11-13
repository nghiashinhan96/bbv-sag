export interface ChangePasswordModel {
    oldPassword: string;
    password: string;
    id?: number;
    redirectUrl: string;
}

export class ChangePassword {
    oldPassword: string;
    password: string;
    id?: number;
    redirectUrl: string;

    constructor(data?: any) {
        if (data) {
            this.oldPassword = data.oldPassword;
            this.password = data.password;
            this.id = data.id;
            this.redirectUrl = data.redirectUrl || '';
        }
    }

    toUpdatePasswordRequest(data?: any) {
        return {
            oldPassword: data.oldPassword,
            password: data.newPassword,
            redirectUrl: data.redirectUrl
        } as ChangePasswordModel;
    }
}
