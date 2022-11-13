export class BrowserUtil {
    public static isIE() {
        const ua = navigator.userAgent;
        /* MSIE used to detect old browsers and Trident used to newer ones*/
        return ua.indexOf('MSIE ') > -1 || ua.indexOf('Trident/') > -1;
    }
}
