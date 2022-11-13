import { ExportOrderDetail } from './export-order-detail.model';

export class ExportOrder {
    basePath: string;
    fileName: string;
    orders: ExportOrderDetail[];
    customerNr: string;
    requestType: string;
    orderNumber: string;
    orderDate: string;
    totalPriceInclVat: string;
    totalPrice: string;
    deliveryType: string; // pickup or tour
    paymentMethod: string; // cash or credit
    companyName: string; // LAdresseFirma
    street: string; // LAdresseStrasse
    postCode: string; // LAdressePLZ
    city: string; // LAdresseOrt
    note: string; // Bemerkung
    version: string;
    hookUrl: string;
    dmsCommand: string;
}
