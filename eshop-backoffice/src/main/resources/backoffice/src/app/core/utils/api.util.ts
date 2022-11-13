import { ERROR_500, ERROR_400 } from '../conts/app.constant';
import { environment } from 'src/environments/environment';

export class ApiUtil {
    public static isFunction(v: any) {
        return typeof v === 'function';
    }

    public static handleErrorReponse(error): string {
        const statusCode = error.error_code || error.code;
        switch (statusCode) {
            case ERROR_500:
                return 'MESSAGES.INTERNAL_SERVER_ERROR';
            case ERROR_400:
                return 'MESSAGES.INTERNAL_SERVER_ERROR';
            default:
                if (statusCode) {
                    return `MESSAGES.${statusCode}`;
                }
                return error.message;
        }
    }

    public static getUrl(path): string {
        return `${environment.baseUrl}${path}`;
    }
}
