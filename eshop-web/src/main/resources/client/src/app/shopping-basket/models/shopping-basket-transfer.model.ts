import { ShoppingBasketContextModel } from './shopping-basket-context.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { FinalCustomerModel } from 'src/app/final-customer/models/final-customer.model';
import { ShoppingOrderConditionContextModel } from './shopping-order-condition-context.model';
import { DateUtil } from 'src/app/core/utils/date.util';
import { SerbiaShoppingOrderConditionContextModel } from './sb/shopping-order-condition-context-sb.model';
import { SB_LOCATION_TYPES } from 'sag-article-detail';

export class ShoppingBasketTransferModel {

    // orderCondition: OrderConditionContext;
    // partialDelivery: boolean;
    // singleInvoice: boolean;
    // saleId: number;
    // personalNumber: string;
    // totalInclVat: number;
    // items: OrderItem[];
    // customerRefText: string;
    // message: string;
    // timezone: string;
    // finalCustomer: FinalCustomer;
    // finalCustomerOrderId: number;
    // requestDateTime: string;

    private context: ShoppingBasketContextModel;
    private total: number;
    private msg: string;
    private custRefTxt: string;
    private hasBasketItemRefText: boolean;
    private user: UserDetail;
    private finalCustomer: FinalCustomerModel;
    private finalCustomerOrderId?: number;
    private saleInfo: any;
    private items?: {
        additionalTextDoc: string,
        pimId: string
    }[];
    requestDateTime?: string;
    private timezone: string;
    private orderFrom: string;

    constructor(json) {
        if (json) {
            this.context = json.context;
            this.total = json.total;
            this.msg = json.msg;
            this.custRefTxt = json.custRefTxt;
            this.hasBasketItemRefText = json.hasBasketItemRefText;
            this.user = json.user;
            this.finalCustomer = json.finalCustomer;
            this.saleInfo = json.saleInfo;
            this.finalCustomerOrderId = json.finalCustomerOrderId;
            this.items = json.items;
            this.requestDateTime = json.requestDateTime;
            this.timezone = json.timezone;
            this.orderFrom = json.orderFrom
        }
    }

    get orderRequest() {
        return {
            orderCondition: new ShoppingOrderConditionContextModel(this.context),
            timezone: this.timezone,
            customerRefText: this.custRefTxt || '',
            message: this.msg || '',
            personalNumber: this.saleInfo && this.saleInfo.id || '',
            finalCustomer: this.finalCustomer,
            finalCustomerOrderId: this.finalCustomerOrderId,
            items: this.items,
            requestDateTime: this.requestDateTime,
            orderFrom: this.orderFrom
        };
    }

    get serbiaOrderRequest() {
        return {
            orderCondition: null,
            orderConditionByLocation: (this.context.eshopBasketContextByLocation || []).map(item => new SerbiaShoppingOrderConditionContextModel(item)),
            timezone: this.timezone,
            customerRefText: this.getSerbiaCustomerRefText(this.context),
            message: this.msg || '',
            personalNumber: this.saleInfo && this.saleInfo.id || '',
            finalCustomer: this.finalCustomer,
            finalCustomerOrderId: this.finalCustomerOrderId,
            items: this.items,
            requestDateTime: this.requestDateTime
        };
    }

    getSerbiaCustomerRefText(context: ShoppingBasketContextModel) {
        const locationTypesForGetCustomerRef = [
            SB_LOCATION_TYPES.PRIMARY,
            SB_LOCATION_TYPES.SECONDARY,
            SB_LOCATION_TYPES.THIRD
        ];
        let customerRefText = '';

        if(context.eshopBasketContextByLocation.length > 0) {
            const contextHasReferenceText = context.eshopBasketContextByLocation.filter(item => !!item.referenceTextByLocation);
            if(contextHasReferenceText.length > 0) {
                locationTypesForGetCustomerRef.forEach(type => {
                    if(!customerRefText) {
                        const item = contextHasReferenceText.find(con => con.location.locationType === type);
                        if(item) {
                            customerRefText = item.referenceTextByLocation;
                        }
                    }
                });
            }
        }

        return customerRefText;
    }
}
