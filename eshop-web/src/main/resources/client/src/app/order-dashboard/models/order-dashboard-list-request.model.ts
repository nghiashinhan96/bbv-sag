import { Constant } from 'src/app/core/conts/app.constant';

const OrderDashboardListSortAttrubutesMapping = {
    id: 'orderDescById',
    orderDate: 'orderDescByOrderDate',
    customerInfo: 'orderDescCustomerInfo'
};

export class OrderDashboardListRequestModel {
    statuses: string[] = [];
    customerInfo = '';
    id: number = null;
    dateFrom: string = null;
    dateTo: string = null;

    orderDescCustomerInfo: boolean = null;
    orderDescById: boolean = null;
    orderDescByOrderDate = true;

    page = 0;
    size = Constant.DEFAULT_PAGE_SIZE;

    articleDesc?: string;
    username?: string;
    vehicleDescs?: string;

    constructor(request?) {
        if (request) {
            this.statuses = request.statuses;
            this.customerInfo = request.customerInfo;
            this.id = request.id;
            this.dateFrom = request.dateFrom;
            this.dateTo = request.dateTo;
            this.orderDescCustomerInfo = request.orderDescCustomerInfo;
            this.orderDescById = request.orderDescById;
            this.orderDescByOrderDate = request.orderDescByOrderDate;
            this.page = request.page;
            this.size = request.size || Constant.DEFAULT_PAGE_SIZE;
            this.username = request.username;
            this.vehicleDescs = request.vehicleDescs;
        }
    }

    public resetSearchCondition() {
        this.page = 0;
        this.orderDescById = null;
        this.orderDescByOrderDate = true;
        this.orderDescCustomerInfo = null;
    }

    sort(obj: {
        field: string;
        direction: string;
        force?: boolean;
    }) {
        if (obj && obj.field) {
            this[OrderDashboardListSortAttrubutesMapping[obj.field]] = obj.direction === 'desc';
        }
        return this;
    }

    // get requestBody() {
    //     return  {
    //         basket_filter_mode: this.basketFilterMode,
    //         basket_name: this.basketName,
    //         customer_number: this.customerNr,
    //         customer_name: this.customerName,
    //         last_name: this.lastName,
    //         updated_date: this.savingDate,
    //         customer_ref_text: this.referenceText,
    //         sales_on_behalf_token: this.salesOnBehalfToken,
    //         offset: this.offset,
    //         size: this.size,
    //         order_by_desc_basket_name: this.orderByDescBasketName,
    //         order_by_desc_customer_number: this.orderByDescCustomerNumber,
    //         order_by_desc_customer_name: this.orderByDescCustomerName,
    //         order_by_desc_grand_total_exl_vat: this.orderByDescGrandTotalExlVat,
    //         order_by_desc_last_name: this.orderByDescLastName,
    //         order_by_desc_updated_date: this.orderByDescUpdatedDate,
    //         order_by_desc_customer_ref_text: this.orderByDescCustomerRefText
    //     };
    // }

}
