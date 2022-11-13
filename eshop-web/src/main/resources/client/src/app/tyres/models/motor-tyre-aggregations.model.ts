import { NgOption } from '@ng-select/ng-select';
import { TyresAggregations } from './tyre-aggregations.model';

export class MotorTyresAggregations extends TyresAggregations {
    motor_categories?: NgOption[] = [];

    constructor(aggregations?: any) {
        super(aggregations);

        if (!aggregations) {
            return;
        }
        this.motor_categories = aggregations.motor_categories;
    }
}