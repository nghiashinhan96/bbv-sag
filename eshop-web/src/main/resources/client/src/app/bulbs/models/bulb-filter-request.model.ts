import { BulbFilter } from './bulb-filter.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class BulbFilterRequest {
    suppliers: string[] = [];
    voltages: string[] = [];
    watts: string[] = [];
    codes: string[] = [];
    total_elements = 0;

    constructor(bulbFilter?: BulbFilter) {
        if (!bulbFilter) {
            return;
        }
        this.suppliers = bulbFilter.supplier !== Constant.SPACE ? [bulbFilter.supplier] : [];
        this.voltages = bulbFilter.voltage !== Constant.SPACE ? [bulbFilter.voltage] : [];
        this.watts = bulbFilter.watt !== Constant.SPACE ? [bulbFilter.watt] : [];
        this.codes = bulbFilter.code !== Constant.SPACE ? [bulbFilter.code] : [];
        this.total_elements = bulbFilter.totalElements;
    }
}