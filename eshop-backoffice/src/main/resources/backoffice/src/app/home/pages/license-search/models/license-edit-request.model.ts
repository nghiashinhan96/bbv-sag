export class LicenseEditRequestModel {
    id: number;
    packName: string;
    beginDate: string;
    endDate: string;
    quantity: number

    constructor(data?) {
        if (!data) {
            return;
        }

        this.id = data.id;
        this.packName = data.packName;
        this.beginDate = data.beginDate;
        this.endDate = data.endDate;
        this.quantity = data.quantity;
    }
}