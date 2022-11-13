export class BoolUtil {
    public static isBool(v: any) {
        return typeof v === "boolean";
    }
    public static toBool(v: string) {
        return (/true/i).test(v);
    }
}
