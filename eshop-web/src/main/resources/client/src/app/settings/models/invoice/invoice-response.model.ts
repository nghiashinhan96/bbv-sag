import { InvoiceDetail } from './invoice-detail.model';

export class InvoiceResponse {
    totalItems: number;
    invoiceDetailDtos: InvoiceDetail[] = [];

    constructor(data?: any) {
        if (data) {
            this.totalItems = data.totalItems;
            this.invoiceDetailDtos = (data.invoiceDetailDtos || []).map((detail: InvoiceDetail) => new InvoiceDetail(detail));
        }
    }
}