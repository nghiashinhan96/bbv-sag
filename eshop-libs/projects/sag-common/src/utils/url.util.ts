// @dynamic
export class UrlUtil {

    // This is the special case because Regex string cannot wrapt line.
    // tslint:disable-next-line:max-line-length
    private static readonly URL_PATTERN = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-_]*)?\??(?:[\-\+=&;%@\.\w_]*)#?(?:[\.\!\/\\\w]*))?)/;

    public static parseUrlStr(str: string, classOptions?: string[], targetOptions?: string[]) {
        const url = UrlUtil.parseUrl(str);
        if (!url) {
            return str;
        }
        const document = window.document;
        const aTag = document.createElement('a');
        if (classOptions) {
            aTag.setAttribute('class', classOptions.join(' '));
        }
        if (targetOptions) {
            aTag.setAttribute('target', targetOptions.toString());
        }
        aTag.setAttribute('href', url);
        aTag.setAttribute('rel', 'noopener noreferrer');
        aTag.innerText = str;
        return aTag.outerHTML;
    }

    public static parseUrl(str: string): string {
        if (!str) {
            return '';
        }
        const match = str.match(this.URL_PATTERN);
        if (!match) {
            return '';
        }
        const firstUrl = match[0];
        if (!firstUrl || firstUrl.length !== str.length) {
            return '';
        }
        return str.replace(/\s/g, '');
    }

    public static toBoolean(value) {
        return /^true$/gi.test(value);
    }

    public static isImageUrl(str) {
        if (!str) {
            return;
        }
        if (!this.parseUrl(str)) {
            return;
        }
        const isImgUrl = str.toLowerCase().includes('.jpg') || str.toLowerCase().includes('.png');
        return isImgUrl;
    }
}
