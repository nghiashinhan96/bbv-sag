import { SAG_COMMON_DATETIME_FORMAT } from 'sag-common';
import * as moment from 'moment';
export class SaleOrderHistory {
    id: string;
    customerNr: string;
    customerName: string;
    saleName: string;
    orderNumber: string;
    axOrderURL: string;
    type: string;
    totalPrice: string;
    workIds: string[];
    createdDate: string;
    closedDate: string;

    createdDateDisp: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.id = data.id;
        this.customerNr = data.customerNr;
        this.customerName = data.customerName;
        this.orderNumber = data.nr;
        this.axOrderURL = data.axOrderURL;
        this.createdDate = data.createdDate;
        this.closedDate = data.closedDateDisp;
        this.type = data.type;
        this.totalPrice = data.totalPrice;
        this.workIds = data.workIds;

        this.createdDateDisp = data.createdDateDisp || '';
    }
}
