import { environment } from 'src/environments/environment';

export class UrlUtil {
    public static autonetServer(country) {
        return `${environment.autonetServer}${country}/`;
    }
}