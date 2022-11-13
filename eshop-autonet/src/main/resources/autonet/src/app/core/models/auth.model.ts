import { LoginModeEnum } from 'src/app/authentication/enums/login-mode.enum';
import { environment } from 'src/environments/environment';

export class AuthModel {
    userName: string;
    password: string;
    grantType?: string;
    scope?: string;
    affiliate?: string;
    onBehalf?: string;
    language?: string;
    loginMode?: string;

    constructor(json?: AuthModel | any) {
        if (!json) {
            this.grantType = 'password';
            this.scope = 'read%20write';
            return;
        }
        this.userName = json.userName;
        this.password = json.password;
        this.grantType = json.grantType;
        this.affiliate = json.affiliate || environment.affiliate;
        this.onBehalf = json.onBehalf || '';
        this.language = json.language || '';
        this.loginMode = json.loginMode || LoginModeEnum.NORMAL;
        this.grantType = json.grantType || 'password';
        this.scope = json.scope || 'read%20write';
    }

    get dto() {
        return {
            username: this.userName,
            password: this.password,
            grant_type: this.grantType,
            scope: this.scope,
            affiliate: this.affiliate,
            onbehalf: this.onBehalf,
            language: this.language,
            login_mode: this.loginMode
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
