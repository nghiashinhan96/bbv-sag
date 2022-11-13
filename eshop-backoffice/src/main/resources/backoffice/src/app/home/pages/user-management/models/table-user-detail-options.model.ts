export class TableUserDetailOptions {
    public static StatusAllValue = '';
    public static StatusActiveValue = 'true';
    public static StatusInactiveValue = 'false';
    selectedStatus: any;
    constructor(options?) {
        if (!options) {
            return;
        }
    }
}
