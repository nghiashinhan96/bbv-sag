export class BulbFilter {
    suppliers: string[] = [];
    voltages: string[] = [];
    watts: string[] = [];
    codes: string[] = [];
    totalElements: number;

    constructor(data?: any) {
        if (data) {
            this.suppliers = data.supplier ? [...this.suppliers, data.supplier] : [];
            this.voltages = data.voltage ? [...this.voltages, data.voltage] : [];
            this.watts = data.watt ? [...this.watts, data.watt] : [];
            this.codes = data.code ? [...this.codes, data.code] : [];
            this.totalElements = data.totalElements;
        }
    }

    setAdditionalFields(isChecked: boolean, fieldName: string, fieldValue: string) {
        if (fieldName === 'suppliers') {
            this.suppliers = isChecked ? [...this.suppliers, fieldValue] : this.suppliers.filter(supplier => supplier !== fieldValue);
        } else if (fieldName === 'voltages') {
            this.voltages = isChecked ? [...this.voltages, fieldValue] : this.voltages.filter(vol => vol !== fieldValue);
        } else if (fieldName === 'watts') {
            this.watts = isChecked ? [...this.watts, fieldValue] : this.watts.filter(watt => watt !== fieldValue);
        } else if (fieldName === 'codes') {
            this.codes = isChecked ? [...this.codes, fieldValue] : this.codes.filter(code => code !== fieldValue);
        }
    }
}
