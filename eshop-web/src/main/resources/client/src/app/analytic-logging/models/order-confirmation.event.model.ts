export class OrderConfirmationEvent {
    axOrderURL: string;
    frontEndBasketNr: string;
    orderHistoryId: number;
    orderNr: string;
    workIds = [];

    constructor(data: any) {
        if (data) {
            this.axOrderURL = data.axOrderURL;
            this.frontEndBasketNr = data.frontEndBasketNr;
            this.orderHistoryId = data.orderHistoryId;
            this.orderNr = data.orderNr;
            this.workIds = data.workIds;
        }
    }
}
