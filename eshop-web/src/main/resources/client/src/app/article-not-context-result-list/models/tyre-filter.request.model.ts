import { TyreFilter } from './tyre-filter.model';

export class TyreFilterRequest {
    season = '';
    width = '';
    height = '';
    radius = '';
    // tslint:disable-next-line: variable-name
    speed_index?: string[] = [];
    // tslint:disable-next-line: variable-name
    load_index?: string[] = [];
    // tslint:disable-next-line: variable-name
    tyre_segment?: string[] = [];
    supplier = '';
    // tslint:disable-next-line: variable-name
    fz_category = '';
    runflat: boolean;
    spike: boolean;
    category = '';
    // tslint:disable-next-line: variable-name
    cross_section = '';
    // tslint:disable-next-line: variable-name
    match_code = '';
    // tslint:disable-next-line: variable-name
    total_elements: number;

    constructor(data?: TyreFilter) {
        if (data) {
            this.width = data.width || '';
            this.height = data.height || '';
            this.radius = data.radius || '';
            this.speed_index = [...this.speed_index, ...data.speedIndex];
            this.load_index = [...this.load_index, ...data.loadIndex];
            this.tyre_segment = [...this.tyre_segment, ...data.tyreSegment];
            this.supplier = data.supplier || '';
            this.runflat = data.runflat || false;
            this.spike = data.spike || false;
            this.match_code = data.matchCode || '';
            this.total_elements = data.totalElements || 0;
        }
    }

    toTyreRequest(data?: TyreFilter) {
        if (data) {
            this.season = data.season;
            this.fz_category = data.fzCategory || '';
        }
        return this;
    }

    toTyreMotorRequest(data?: TyreFilter) {
        if (data) {
            this.category = data.category || '';
        }
        return this;
    }
}
