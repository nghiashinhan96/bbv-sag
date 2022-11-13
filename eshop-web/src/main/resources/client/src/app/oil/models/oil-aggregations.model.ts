import { NgOption } from '@ng-select/ng-select';

export class OilAggregations {
    public vehicles?: NgOption[] = [];
    public aggregates?: NgOption[] = [];
    public viscosities?: NgOption[] = [];
    public bottle_sizes?: NgOption[] = [];
    public approved_list?: NgOption[] = [];
    public specifications?: NgOption[] = [];
    public brands?: NgOption[] = [];

    constructor(aggregations?: any) {
        if (!aggregations) {
            return;
        }
        this.vehicles = aggregations.vehicles;
        this.aggregates = aggregations.aggregates;
        this.viscosities = aggregations.viscosities;
        this.bottle_sizes = aggregations.bottle_sizes;
        this.approved_list = aggregations.approved_list;
        this.specifications = aggregations.specifications;
        this.brands = aggregations.brands;
    }
}