export class BrandFilterItem {
    key: string;
    gaID: string;
    brands: { name: string, checked: boolean }[];

    constructor(data?: any) {
        if (data) {
            this.key = data.key;
            this.gaID = data.gaID;
            this.brands = data.brands;
        }
    }
}
