import { Constant } from 'src/app/core/conts/app.constant';
import { TyreFilter } from './tyre-filter.model';

export class MotorTyreFilter extends TyreFilter {
    category: string = Constant.SPACE;

    constructor(data?: any) {
        super(data);

        if (!data) {
            return;
        }
        this.category = data.category;
    }

    get dto() {
        const speed_index = Array.isArray(this.speedIndex) ? this.speedIndex : [this.speedIndex];
        const tyre_segment = Array.isArray(this.tyreSegment) ? this.tyreSegment : [this.tyreSegment];
        const load_index = Array.isArray(this.loadIndex) ? this.loadIndex : [this.loadIndex];

        return {
            category: this.category,
            width: this.width,
            height: this.height,
            radius: this.radius,
            speed_index,
            load_index,
            tyre_segment,
            supplier: this.supplier,
            runflat: this.runflat,
            spike: this.spike,
            match_code: this.matchCode,
            total_elements: this.totalElements
        };
    }
}
