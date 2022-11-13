export class InvoiceSearch {
    dateFrom: string;
    dateTo: string;
    oldInvoice: boolean;

    constructor(data?: any) {
        if (data) {
            this.dateFrom = data.dateFrom;
            this.dateTo = data.dateTo;
            this.oldInvoice = data.oldInvoice;
        }
    }
}
