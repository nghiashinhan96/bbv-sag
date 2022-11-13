export class Availability {
    articleId: string;
    quantity: number;
    backOrder: boolean;
    immediateDelivery: boolean;
    arrivalTime: string;
    tourName: string;
    stockWarehouse: string;
    deliveryWarehouse: string;
    sendMethodCode: string;
    tourTimeTable: [] = [];
    availState: number;
    availStateColor: string;
    sofort: boolean;
    externalSource: boolean;
    venExternalSource: boolean;
    formattedCETArrivalTime: string;
    formattedCETArrivalDate: string;

    constructor(data?: any) {
        if (data) {
            this.articleId = data.articleId;
            this.quantity = data.quantity;
            this.backOrder = data.backOrder;
            this.immediateDelivery = data.immediateDelivery;
            this.arrivalTime = data.arrivalTime;
            this.tourName = data.tourName;
            this.stockWarehouse = data.stockWarehouse;
            this.deliveryWarehouse = data.deliveryWarehouse;
            this.sendMethodCode = data.sendMethodCode;
            this.tourTimeTable = data.tourTimeTable;
            this.availStateColor = data.availStateColor;
            this.sofort = data.sofort;
            this.externalSource = data.externalSource;
            this.venExternalSource = data.venExternalSource;
            this.formattedCETArrivalTime = data.formattedCETArrivalTime;
            this.formattedCETArrivalDate = data.formattedCETArrivalDate;
        }
    }
}
