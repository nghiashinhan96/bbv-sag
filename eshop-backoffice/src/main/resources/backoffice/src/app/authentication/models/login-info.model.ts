
export class LoginInfo {
    username: string;
    password: string;
    grantType?: string;
    scope?: string;

    constructor(json?: LoginInfo | any) {
        if (!json) {
            this.grantType = 'password';
            this.scope = 'read%20write';
            return;
        }
        this.username = json.username;
        this.password = json.password;
        this.grantType = json.grantType || 'password';
        this.scope = json.scope || 'read%20write';
    }

    get dto() {
        return {
            username: this.username,
            password: this.password,
            grant_type: this.grantType,
            scope: this.scope
        };
    }

    get formData() {
        const dto = this.dto;
        const attributes = [];
        Object.keys(dto).forEach(k => {
            attributes.push(`${k}=${dto[k]}`);
        });
        return attributes.join('&');
    }
}
