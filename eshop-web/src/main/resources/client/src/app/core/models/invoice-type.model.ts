export class InvoiceType {
    id: number;
    descCode: string;
    invoiceType: string;
    invoiceTypeDesc: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.descCode = data.descCode;
            this.invoiceType = data.invoiceType;
            this.invoiceTypeDesc = data.invoiceTypeDesc;
        }
    }
}
