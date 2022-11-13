export class LicenseRequestModel {
    affiliate: string;
    customerNr: string;
    packName: string;
    beginDate: string;
    endDate: string;
    typeOfLicense?: string;
    quantity?: number;
    quantityUsed?: number;
    sorting?: LicenseRequestModelSorting;
    page?: number;
    size?: number;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.affiliate = data.affiliate || '';
        this.customerNr = data.customerNr || '';
        this.packName = data.packName || '';
        this.beginDate = data.beginDate || '';
        this.endDate = data.endDate || '';
        this.typeOfLicense = data.typeOfLicense || '';
        this.quantity = parseInt(data.quantity) || null;
        this.quantityUsed = parseInt(data.quantityUsed) || null;
        this.sorting = new LicenseRequestModelSorting(data.sorting);
        this.page = data.page || null;
        this.size = data.size || null;
    }
}

export class LicenseRequestModelSorting {
    orderByTypeOfLicenseDesc?: boolean;
    orderByCustomerNrDesc?: boolean;
    orderByPackNameDesc?: boolean;
    orderByBeginDateDesc?: boolean;
    orderByEndDateDesc?: boolean;
    orderByQuantityDesc?: boolean;
    orderByQuantityUsedDesc?: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.orderByTypeOfLicenseDesc = data.orderByTypeOfLicenseDesc || null;
        this.orderByCustomerNrDesc = data.orderByCustomerNrDesc || null;
        this.orderByPackNameDesc = data.orderByPackNameDesc || null;
        this.orderByBeginDateDesc = data.orderByBeginDateDesc || null;
        this.orderByEndDateDesc = data.orderByEndDateDesc || null;
        this.orderByQuantityDesc = data.orderByQuantityDesc || null;
        this.orderByQuantityUsedDesc = data.orderByQuantityUsedDesc || null;
    }
}