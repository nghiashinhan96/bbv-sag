export class MarginBrandRequestModel {
    brandName: string = null;
    orderDescByBrandName: boolean = false;

    constructor (data = null) {
        if (data) {
            this.brandName = data.brandName;
            this.orderDescByBrandName = data.orderDescByBrandName;
        }
    }
}