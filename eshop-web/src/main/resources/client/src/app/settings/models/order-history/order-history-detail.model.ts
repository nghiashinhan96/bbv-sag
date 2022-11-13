import { OrderItem } from './order-item.model';

export interface OrderHistoryDetailRequest {
    orderId: string;
    orderNumber: string;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;
}

export class OrderHistoryDetail {
    branchRemark: string;
    customerNr: number;
    date: string;
    id: number;
    invoiceTypeCode: string;
    nr: string;
    orderItems: OrderItem[];
    reference: string;
    sendMethod: string;
    sendMethodCode: string;
    status: string;
    statusCode: string;
    type: string;
    typeDesc: string;
    username: string;
    vehicleInfos: string[];
    source: string;
    showPriceType: boolean;
    goodsReceiverName: string;
    dateNumber?: number;
    sendMethodDesc?: string;

    constructor(data: any) {
        if (data) {
            this.branchRemark = data.branchRemark;
            this.customerNr = data.customerNr;
            this.date = data.date;
            this.id = data.id;
            this.invoiceTypeCode = data.invoiceTypeCode;
            this.nr = data.nr;
            this.orderItems = (data.orderItems || []).map((item: any) => new OrderItem(item, data.sendMethodDesc || data.sendMethodCode));
            this.reference = data.reference;
            this.sendMethod = data.sendMethod;
            this.sendMethodCode = data.sendMethodCode;
            this.status = data.status;
            this.statusCode = data.statusCode;
            this.type = data.type;
            this.typeDesc = data.typeDesc;
            this.username = data.username;
            this.vehicleInfos = data.vehicleInfos || [];
            this.source = data.id ? 'ESHOP' : 'ERP_SYSTEM';
            this.showPriceType = data.showPriceType;
            this.goodsReceiverName = data.goodsReceiverName;
            this.dateNumber = new Date(data.date).getTime();
        }
    }
}