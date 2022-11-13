export class StringUtil {

    public static toString(value: any): string {
        if (!value) {
            return '';
        }
        return value.toString();
    }

    public static removeNewLine(source: string) {
        if (!!source) {
            return source.replace(/(\r\n|\n|\r)/gm, '');
        }
        return source;
    }

    public static isNotBlank(str: string): boolean {
        return !this.isBlank(str);
    }

    public static defaultIfBlank(str: string): string {
        return this.isNotBlank(str) ? str : '';
    }

    public static isBlank(str: string): boolean {
        if (!str) {
            return true;
        }
        return str.trim().length === 0;
    }

    public static isIncludesOf(inStr: string, strings: string[]) {
        if (!inStr) {
            return false;
        }
        return strings.some(str => inStr.includes(str));
    }
}
