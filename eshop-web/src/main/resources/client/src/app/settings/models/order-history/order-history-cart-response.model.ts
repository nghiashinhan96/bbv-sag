export class OrderHistoryCartResponse {
    items: any[];
    numberOfItems: number;
    vatValue: number;
    totalWithDiscount: number;
    newTotal: number;
    discount: number;
    couponCode: string;
    subTotalWithVat: number;
    netTotalExclVat: number;
    grossTotalExclVat: number;
    subTotalWithNet: number;
    subTotal: number;
    subTotalWithNetAndVat: number;
    vatTotal: number;
    vatTotalWithNet: number;
    newTotalWithVat: number;
    newTotalWithNetAndVat: number;
    numberOfOrderPos: number;

    constructor(data?: any) {
        if (data) {
            this.items = data.items;
            this.numberOfItems = data.numberOfItems;
            this.vatValue = data.vatValue;
            this.totalWithDiscount = data.totalWithDiscount;
            this.newTotal = data.newTotal;
            this.discount = data.discount;
            this.couponCode = data.couponCode;
            this.subTotalWithVat = data.subTotalWithVat;
            this.netTotalExclVat = data.netTotalExclVat;
            this.grossTotalExclVat = data.grossTotalExclVat;
            this.subTotalWithNet = data.subTotalWithNet;
            this.subTotal = data.subTotal;
            this.subTotalWithNetAndVat = data.subTotalWithNetAndVat;
            this.vatTotal = data.vatTotal;
            this.vatTotalWithNet = data.vatTotalWithNet;
            this.newTotalWithVat = data.newTotalWithVat;
            this.newTotalWithNetAndVat = data.newTotalWithNetAndVat;
            this.numberOfOrderPos = data.numberOfOrderPos;
        }
    }
}
