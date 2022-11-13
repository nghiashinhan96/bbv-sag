export class StringHelper {
    public static getEmptyStringIfNull(obj) {
        if (obj) {
            return obj;
        }
        return '';
    }

    public static removeNonDigits(value: string): string {
        return value.replace(/\D/g, '');
    }

    public static isEmpty(str: string): boolean {
        if (!str) {
            return true;
        }
        return str.length === 0;
    }

    public static isNotEmpty(str: string): boolean {
        return !this.isEmpty(str);
    }

    public static isBlank(str: string): boolean {
        if (!str) {
            return true;
        }
        return str.trim().length === 0;
    }
}
