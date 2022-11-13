import { OilFilter } from './oil-filter.model';

export class OilFilterRequest {
    vehicles: string[] = [];
    aggregates: string[] = [];
    viscosities: string[] = [];
    // tslint:disable-next-line: variable-name
    bottle_sizes: string[] = [];
    // tslint:disable-next-line: variable-name
    approved_list: string[] = [];
    specifications: string[] = [];
    // tslint:disable-next-line: variable-name
    total_elements: number;

    constructor(data?: OilFilter) {
        if (data) {
            this.vehicles = [...this.vehicles, ...data.vehicles];
            this.aggregates = [...this.aggregates, ...data.aggregates];
            this.viscosities = [...this.viscosities, ...data.viscosities];
            this.bottle_sizes = [...this.bottle_sizes, ...data.bottleSizes];
            this.approved_list = [...this.approved_list, ...data.approvedList];
            this.specifications = [...this.specifications, ...data.specifications];
            this.total_elements = data.totalElements;
        }
    }
}
