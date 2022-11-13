import { BulbFilter } from './bulb-filter.model';

export class BulbFilterRequest {
    suppliers: string[] = [];
    voltages: string[] = [];
    watts: string[] = [];
    codes: string[] = [];
    // tslint:disable-next-line: variable-name
    total_elements: number;

    constructor(data?: BulbFilter) {
        if (data) {
            this.suppliers = [...this.suppliers, ...data.suppliers];
            this.voltages = [...this.voltages, ...data.voltages];
            this.watts = [...this.watts, ...data.watts];
            this.codes = [...this.codes, ...data.codes];
            this.total_elements = data.totalElements;
        }
    }
}
