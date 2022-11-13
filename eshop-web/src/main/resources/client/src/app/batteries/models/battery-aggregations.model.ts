import { NgOption } from '@ng-select/ng-select';

export class BatteryAggregations {
    public voltages?: NgOption[] = [];
    public ampere_hours?: NgOption[] = [];
    public widths?: NgOption[] = [];
    public heights?: NgOption[] = [];
    public lengths?: NgOption[] = [];
    public interconnections?: NgOption[] = [];
    public typeOfPoles?: NgOption[] = [];

    constructor(aggregations?: any) {
        if (!aggregations) {
            return;
        }
        this.voltages = aggregations.voltages;
        this.ampere_hours = aggregations.ampere_hours;
        this.widths = aggregations.widths;
        this.heights = aggregations.heights;
        this.lengths = aggregations.lengths;
        this.interconnections = aggregations.interconnections;
        this.typeOfPoles = aggregations.poles;
    }
}
