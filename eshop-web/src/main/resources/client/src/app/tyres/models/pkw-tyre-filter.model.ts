import { Constant } from 'src/app/core/conts/app.constant';
import { TyreFilter } from './tyre-filter.model';

export class PkwTyreFilter extends TyreFilter {
    season: string = Constant.SPACE;
    fzCategory: string = Constant.SPACE;

    constructor(data?: any) {
        super(data);
        if (!data) {
            return;
        }
        this.season = data.season;
        this.fzCategory = data.fzCategory;
    }

    get dto() {
        const speed_index = Array.isArray(this.speedIndex) ? this.speedIndex : [this.speedIndex];
        const tyre_segment = Array.isArray(this.tyreSegment) ? this.tyreSegment : [this.tyreSegment];
        const load_index = Array.isArray(this.loadIndex) ? this.loadIndex : [this.loadIndex];

        return {
            season: this.season,
            width: this.width,
            height: this.height,
            radius: this.radius,
            match_code: this.matchCode,
            speed_index,
            supplier: this.supplier,
            fz_category: this.fzCategory,
            runflat: this.runflat,
            spike: this.spike,
            tyre_segment,
            load_index,
            total_elements: this.totalElements
        };
    }
}
