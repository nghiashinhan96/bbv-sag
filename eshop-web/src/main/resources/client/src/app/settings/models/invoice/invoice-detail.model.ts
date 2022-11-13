import { InvoicePosition } from './invoice-position.model';
import { InvoiceAddress } from './invoice-address.model';

export class InvoiceDetail {
    invoiceNr: string;
    invoiceDate: number;
    name: string;
    customerNr: string;
    zipcode: string;
    city: string;
    country: string;
    termOfPayment: string;
    paymentType: string;
    address: InvoiceAddress | InvoiceAddress[];
    positions: InvoicePosition[];
    deliveryNoteNr: string;
    orderNr: string;
    orderNrs: string[];
    amount: number;
    deliveryNoteNrs: string[];
    orderHistoryId: number;

    constructor(data?: any) {
        if (data) {
            this.invoiceNr = data.invoiceNr;
            this.invoiceDate = data.invoiceDate;
            this.name = data.name;
            this.customerNr = data.customerNr;
            this.zipcode = data.zipcode;
            this.city = data.city;
            this.country = data.country;
            this.termOfPayment = data.termOfPayment;
            this.paymentType = data.paymentType;
            this.address = data.address && data.address.length ?
                (data.address || []).map((addr: any) => new InvoiceAddress(addr)) :
                new InvoiceAddress(data.address);
            this.positions = (data.positions || []).map((position: InvoicePosition) => new InvoicePosition(position));
            this.deliveryNoteNr = data.deliveryNoteNr;
            this.orderNr = data.orderNr;
            this.orderNrs = data.orderNrs || [];
            this.amount = data.amount;
            this.deliveryNoteNrs = data.deliveryNoteNrs || [];
            this.orderHistoryId = data.orderHistoryId;
        }
    }
}
