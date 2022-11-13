import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { DATE_FILTER } from 'src/app/core/enums/date-filter.enum';

const BasketHistorySortAttrubutesMapping = {
    savingDate: 'orderByDescUpdatedDate',
    basketName: 'orderByDescBasketName',
    customerNr: 'orderByDescCustomerNumber',
    customerName: 'orderByDescCustomerName',
    total: 'orderByDescGrandTotalExlVat',
    lastName: 'orderByDescLastName',
    referenceText: 'orderByDescCustomerRefText'
};

export class BasketHistoryRequestModel {
    basketFilterMode: string;
    salesOnBehalfToken: string;

    customerNr?: string;
    customerName?: string;
    saleName?: string;
    salesUserId?: string;
    basketName?: string;
    referenceText?: string;
    savingDate?: any;
    total?: string;
    lastName?: string;

    offset?: number;
    size?: number;

    orderByDescBasketName?: boolean;
    orderByDescCustomerNumber?: boolean;
    orderByDescCustomerName?: boolean;
    orderByDescGrandTotalExlVat?: boolean;
    orderByDescLastName?: boolean;
    orderByDescUpdatedDate?: boolean;
    orderByDescCustomerRefText?: boolean;

    constructor(data: BasketHistoryRequestModel | any) {
        if (data) {
            this.total = data.grandTotalExcludeVat || data.total;
            this.basketFilterMode = data.basketFilterMode;
            this.basketName = data.basketName;
            this.customerNr = data.customerNr;
            this.customerName = data.customerName;
            this.lastName = data.saleName;

            this.savingDate = this.convertSavingDate(data.savingDate);

            this.salesOnBehalfToken = data.salesOnBehalfToken || '';
            this.referenceText = data.referenceText;
            this.offset = data.offset || 0;
            this.size = data.size || APP_DEFAULT_PAGE_SIZE;
            this.orderByDescBasketName = data.orderByDescBasketName;
            this.orderByDescCustomerNumber = data.orderByDescCustomerNumber;
            this.orderByDescCustomerName = data.orderByDescCustomerName;
            this.orderByDescGrandTotalExlVat = data.orderByDescGrandTotalExlVat;
            this.orderByDescLastName = data.orderByDescLastName;
            this.orderByDescUpdatedDate = data.orderByDescUpdatedDate;
            this.orderByDescCustomerRefText = data.orderByDescCustomerRefText;
        }
    }

    sort(obj: {
        field: string;
        direction: string;
        force?: boolean;
    }) {
        if (obj && obj.field) {
            this[BasketHistorySortAttrubutesMapping[obj.field]] = obj.direction === 'desc';
        }
        return this;
    }

    get requestBody() {
        const obj = {
            basket_filter_mode: this.basketFilterMode,
            basket_name: this.basketName,
            customer_number: this.customerNr,
            customer_name: this.customerName,
            last_name: this.lastName,
            updated_date: this.savingDate,
            customer_ref_text: this.referenceText,
            sales_on_behalf_token: this.salesOnBehalfToken,
            offset: this.offset,
            size: this.size,
            order_by_desc_basket_name: this.orderByDescBasketName,
            order_by_desc_customer_number: this.orderByDescCustomerNumber,
            order_by_desc_customer_name: this.orderByDescCustomerName,
            order_by_desc_grand_total_exl_vat: this.orderByDescGrandTotalExlVat,
            order_by_desc_last_name: this.orderByDescLastName,
            order_by_desc_updated_date: this.orderByDescUpdatedDate,
            order_by_desc_customer_ref_text: this.orderByDescCustomerRefText
        };
        return obj;
    }

    convertSavingDate(value) {
        const date = new Date();
        date.setHours(0); // Start from begining of the day.
        switch (value) {
            case DATE_FILTER.ALL:
                return null;
            case DATE_FILTER.TODAY:
                return date.getTime(); // Today
            case DATE_FILTER.TWO_DAYS:
                return date.setDate(date.getDate() - 2); // Two days ago
            case DATE_FILTER.LAST_WEEK:
                return date.setDate(date.getDate() - 7); // Last week
        }
        return '';
    }
}
