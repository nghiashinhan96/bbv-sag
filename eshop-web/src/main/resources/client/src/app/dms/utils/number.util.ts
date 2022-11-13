export class NumberUtil {

    public static isNumber(n: any) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

}
