import { NgOption } from '@ng-select/ng-select';

export class BulbsAggregations {
    public codes?: NgOption[] = [];
    public suppliers?: NgOption[] = [];
    public voltages?: NgOption[] = [];
    public watts?: NgOption[] = [];

    constructor(aggregations?: any) {
        if (!aggregations) {
            return;
        }
        this.codes = aggregations.codes;
        this.suppliers = aggregations.suppliers;
        this.voltages = aggregations.voltages;
        this.watts = aggregations.watts;
    }
}