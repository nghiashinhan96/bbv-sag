export class InvoicePosition {
    invoiceNr: string;
    articleId: string;
    articleNr: string;
    articleTitle: string;
    amount: number;
    vehicleInfo: string;
    quantity: number;
    orderNr: string;
    deliveryNoteNr: string;

    constructor(data?: any) {
        if (data) {
            this.invoiceNr = data.invoiceNr;
            this.articleId = data.articleId;
            this.articleNr = data.articleNr;
            this.articleTitle = data.articleTitle;
            this.amount = data.amount;
            this.vehicleInfo = data.vehicleInfo;
            this.quantity = data.quantity;
            this.orderNr = data.orderNr;
            this.deliveryNoteNr = data.deliveryNoteNr;
        }
    }
}
