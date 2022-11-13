import { isEmpty } from 'lodash';
import { StringUtil } from 'src/app/dms/utils/string.util';
import { Constant } from 'src/app/core/conts/app.constant';

const SORT_FIELD_MAP = {
    customerName: 'orderDescByCustomerName',
    customerNr: 'orderDescByCustomerNumber',
    orderNumber: 'orderDescByOrderNumber',
    createdDateDisp: 'orderDescByOrderDate',
    saleName: 'orderDescBySaleName',
    type: 'orderDescByType',
    totalPrice: 'orderDescByTotalPrice'
};

export class SalesOrderHistoryFilter {
    customerName: string;
    customerNumber: string;
    orderDate: string;
    orderDescByCustomerName: boolean;
    orderDescByCustomerNumber: boolean;
    orderDescByOrderDate: boolean;
    orderDescByOrderNumber: boolean;
    orderDescBySaleName: boolean;
    orderDescByType: boolean;
    orderDescByTotalPrice: number;
    orderNumber: string;
    page: number;
    saleName: string;
    type: string;
    size: number;
    totalPrice: string;

    constructor(data?) {
        if (!data) {
            return;
        }
        this.page = data.page;
        this.size = data.size;
        this.customerName = StringUtil.isBlank(data.customerName) ? Constant.EMPTY_STRING : data.customerName;
        this.customerNumber = StringUtil.isBlank(data.customerNr) ? Constant.EMPTY_STRING : data.customerNr;
        this.orderDate = StringUtil.isBlank(data.orderDate) ? Constant.EMPTY_STRING : data.orderDate;
        this.orderNumber = StringUtil.isBlank(data.orderNumber) ? Constant.EMPTY_STRING : data.orderNumber;
        this.saleName = StringUtil.isBlank(data.saleName) ? Constant.EMPTY_STRING : data.saleName;
        this.totalPrice = StringUtil.isBlank(data.totalPrice) ? Constant.EMPTY_STRING : data.totalPrice;
        this.type = StringUtil.isBlank(data.type) ? Constant.EMPTY_STRING : data.type;
    }

    public resetSort(sortColumn?: any): SalesOrderHistoryFilter {
        this.orderDescByCustomerName = null;
        this.orderDescByCustomerNumber = null;
        this.orderDescByOrderDate = null;
        this.orderDescByOrderNumber = null;
        this.orderDescBySaleName = null;
        this.orderDescByTotalPrice = null;
        this.orderDescByType = null;
        this[SORT_FIELD_MAP[sortColumn.column]] = sortColumn.value;
        return this;
    }
}
