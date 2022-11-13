import { FinalUserSearchSort } from './final-user-search-sort.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class FinalUserOrdersCriteria {
    articleDesc = '';
    customerInfo = '';
    dateFrom = '';
    dateTo = '';
    id = '';
    page = 0;
    size = Constant.DEFAULT_PAGE_SIZE;
    statuses = [];
    username = '';
    vehicleDescs = '';

    orderDescById: boolean;
    orderDescByOrderDate = true;
    orderDescCustomerInfo: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.articleDesc = data.articleDesc;
        this.customerInfo = data.customerInfo;
        this.dateFrom = data.dateFrom;
        this.dateTo = data.dateTo;
        this.id = data.id;
        this.page = data.page;
        this.size = data.size;
        this.statuses = data.statuses;
        this.username = data.username;
        this.vehicleDescs = data.vehicleDescs;

        this.orderDescById = data.orderDescById;
        this.orderDescByOrderDate = data.orderDescByOrderDate;
        this.orderDescCustomerInfo = data.orderDescCustomerInfo;
    }

    public resetSort() {
        this.orderDescById = null;
        this.orderDescByOrderDate = true;
        this.orderDescCustomerInfo = null;
    }
}
