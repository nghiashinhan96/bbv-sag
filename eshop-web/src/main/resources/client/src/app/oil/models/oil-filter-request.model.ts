import { Constant } from 'src/app/core/conts/app.constant';
import { OilFilter } from './oil-filter.model';

export class OilFilterRequest {
    vehicles: string[] = [];
    aggregates: string[] = [];
    viscosities: string[] = [];
    bottle_sizes: string[] = [];
    approved_list: string[] = [];
    specifications: string[] = [];
    brands: string[] = [];
    total_elements = 0;

    constructor(oilFilter?: OilFilter) {
        if (!oilFilter) {
            return;
        }
        this.vehicles = oilFilter.vehicle !== Constant.SPACE ? [oilFilter.vehicle] : [];
        this.aggregates = oilFilter.aggregate !== Constant.SPACE ? [oilFilter.aggregate] : [];
        this.viscosities = oilFilter.viscosity !== Constant.SPACE ? [oilFilter.viscosity] : [];
        this.bottle_sizes = oilFilter.bottleSize !== Constant.SPACE ? oilFilter.bottleSize.split(',') : [];

        this.approved_list = oilFilter.approved !== Constant.SPACE ? [oilFilter.approved] : [];
        this.specifications = oilFilter.specification !== Constant.SPACE ? [oilFilter.specification] : [];
        this.brands = oilFilter.brand !== Constant.SPACE ? [oilFilter.brand] : [];
        this.total_elements = oilFilter.totalElements;
    }
}
