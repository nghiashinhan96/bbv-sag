import { NgOption } from '@ng-select/ng-select';

export class TyresAggregations {
    widths?: NgOption[] = [];
    heights?: NgOption[] = [];
    radiuses?: NgOption[] = [];
    speed_indexes?: NgOption[] = [];
    suppliers?: NgOption[] = [];
    tyre_segments?: NgOption[] = [];
    load_indexs?: NgOption[] = [];
    motor_categories?: NgOption[] = [];

    constructor(aggregations?: any) {
        if (!aggregations) {
            return;
        }
        this.widths = aggregations.widths;
        this.heights = aggregations.heights;
        this.radiuses = aggregations.radiuses;
        this.speed_indexes = aggregations.speed_indexes;
        this.suppliers = aggregations.suppliers;
        this.tyre_segments = aggregations.tyre_segments;
        this.load_indexs = aggregations.load_indexs;
    }
}