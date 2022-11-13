export class SaleOrderHistoryDetail {
    quantity: string;
    articleNr: string;
    info: string;
    vehicle: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.quantity = data.quantity;
        this.articleNr = data.articleNr;
        this.info = data.info;
        this.vehicle = data.vehicle;
    }
}
