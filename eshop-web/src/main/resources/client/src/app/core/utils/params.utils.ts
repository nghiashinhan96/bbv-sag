export class ParamUtil {
    public static toBoolean(value) {
        return /^true$/gi.test(value);
    }

    public static parseQueryFromUrl(url: string) {
        const result = {};
        const varibales = url.split('&');
        varibales.forEach(varibale => {
            const pair = varibale.split('=');
            Object.assign(result, { [pair[0]]: decodeURIComponent(pair[1]) });
        });
        return result;
    }
}
