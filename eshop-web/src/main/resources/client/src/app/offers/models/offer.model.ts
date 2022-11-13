export class Offer {
    id: number;
    customerName = '';
    offerNumber: number;
    price: number;
    remark = '';
    status = '';
    username = '';
    offerDate = '';
    vehicleDescs: Array<string> = [];

    constructor(data?) {
        if (!data) {
            return;
        }
        this.customerName = data.customerName;
        this.remark = data.remark;
        this.status = data.status;
        this.username = data.username;
        this.offerDate = data.offerDate;
        this.vehicleDescs = data.vehicleDescs;
    }
}
