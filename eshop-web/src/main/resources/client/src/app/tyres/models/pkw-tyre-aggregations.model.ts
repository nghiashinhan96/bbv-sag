import { NgOption } from '@ng-select/ng-select';
import { TyresAggregations } from './tyre-aggregations.model';

export class PkwTyresAggregations extends TyresAggregations {
    seasons?: NgOption[] = [];
    fz_categories?: NgOption[] = [];

    constructor(aggregations?: any) {
        super(aggregations);

        if (!aggregations) {
            return;
        }
        this.seasons = aggregations.seasons;
        this.fz_categories = aggregations.fz_categories;
    }
}