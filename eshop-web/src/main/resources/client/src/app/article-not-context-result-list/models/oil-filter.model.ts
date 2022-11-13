export class OilFilter {
    vehicles: string[] = [];
    aggregates: string[] = [];
    viscosities: string[] = [];
    bottleSizes: string[] = [];
    approvedList: string[] = [];
    specifications: string[] = [];
    totalElements: number;

    constructor(data?: any) {
        if (data) {
            this.vehicles = data.vehicle ? [...this.vehicles, data.vehicle] : [];
            this.aggregates = data.aggregate ? [...this.aggregates, data.aggregate] : [];
            this.viscosities = data.viscosity ? [...this.viscosities, data.viscosity] : [];
            this.bottleSizes = data.bottleSize ? [...this.bottleSizes, ...data.bottleSize.split(',')] : [];
            this.approvedList = data.approved ? [...this.approvedList, data.approved] : [];
            this.specifications = data.specification ? [...this.specifications, data.specification] : [];
            this.totalElements = data.totalElements;
        }
    }

    setAdditionalFields(isChecked: boolean, fieldName: string, fieldValue: string) {
        if (fieldName === 'vehicles') {
            this.vehicles = isChecked ? [...this.vehicles, fieldValue] : this.vehicles.filter(vehicle => vehicle !== fieldValue);
        } else if (fieldName === 'aggregates') {
            this.aggregates = isChecked ? [...this.aggregates, fieldValue] : this.aggregates.filter(aggregate => aggregate !== fieldValue);
        } else if (fieldName === 'viscosities') {
            this.viscosities = isChecked ? [...this.viscosities, fieldValue] :
                this.viscosities.filter(viscosity => viscosity !== fieldValue);
        } else if (fieldName === 'bottle_sizes') {
            this.bottleSizes = isChecked ? [...this.bottleSizes, fieldValue] :
                this.bottleSizes.filter(bottleSize => bottleSize !== fieldValue);
        } else if (fieldName === 'approved_list') {
            this.approvedList = isChecked ? [...this.approvedList, fieldValue] :
                this.approvedList.filter(approved => approved !== fieldValue);
        } else if (fieldName === 'specifications') {
            this.specifications = isChecked ? [...this.specifications, fieldValue] :
                this.specifications.filter(specification => specification !== fieldValue);
        }
    }
}
