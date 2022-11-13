import { ArticleAvailLocationModel } from './article-avail-location.model';
export class ArticleAvailabilityModel {
    articleId: string;
    quantity: number;
    backOrder: boolean;
    immediateDelivery: boolean;
    arrivalTime: string;
    tourName: string;
    stockWarehouse: string;
    deliveryWarehouse: string;
    sendMethodCode: string; // "TOUR"
    tourTimeTable: any[];
    availState: number;
    availStateColor: string;
    sofort: boolean;
    formattedCETArrivalTime: string;
    formattedCETArrivalDate: string;
    dateFormat?: string;
    timeFormat?: string;
    presentPath?: string;
    description?: string;
    venExternalSource?: any;
    type?: 'AUTONET' | 'CONNECT';
    rawArrivalTime?: string;
    conExternalSource?: boolean;
    locationState?: number;
    location?: ArticleAvailLocationModel;
    externalVendorId?: number;
    externalVendorName?: string;
}
