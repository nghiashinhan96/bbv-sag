import { ArticleReturnUtils } from "../article-return.utils";

export class ReturnArticle {
    transId: string;
    articleId: string;
    articleDetail: {};
    articleName: string;
    articleKeyword: string;
    axPaymentType: string;
    orderNr: string;
    orderDetail: {};
    customerName: string;
    saleName: string;
    quantity: number;
    returnQuantity: number;
    branchId: string;
    paymentType: string;
    termOfPayment: string;
    cashDiscount: string;
    salesUnit: string;
    sourcingType: string;
    isChecked?: boolean;
    isQuarantined?: boolean;
    quarantineText?: string;
    returnQuantityFromUser?: number;
    attachedTransactionReferences?: any;
    editable?: boolean;
    isDepotReturnArticle?: boolean;
    hasDepotReturnArticle?: boolean;
    returnReason?: string;

    constructor(data: any) {
        if (data) {
            this.transId = data.transId;
            this.articleId = data.articleId;
            this.articleDetail = data.articleDetail;
            this.articleName = data.articleName;
            this.articleKeyword = data.articleKeyword;
            this.axPaymentType = data.axPaymentType;
            this.orderNr = data.orderNr;
            this.orderDetail = data.orderDetail;
            this.customerName = data.customerName;
            this.saleName = data.saleName;
            this.quantity = data.quantity;
            this.returnQuantity = data.returnQuantity;
            this.branchId = data.branchId;
            this.paymentType = data.paymentType;
            this.termOfPayment = data.termOfPayment;
            this.cashDiscount = data.cashDiscount;
            this.salesUnit = data.salesUnit;
            this.sourcingType = data.sourcingType;
            this.isChecked = data.isChecked;
            this.isQuarantined = data.isQuarantined;
            this.quarantineText = data.quarantineText;
            this.returnQuantityFromUser = data.returnQuantityFromUser;
            if (data.attachedTransactionReferences) {
                this.attachedTransactionReferences = data.attachedTransactionReferences;
                (this.attachedTransactionReferences || []).forEach(item => {
                    item.isDepotReturnArticle = ArticleReturnUtils.isDepotArticle(item);
                })
                this.hasDepotReturnArticle = (this.attachedTransactionReferences || []).some(item => item.isDepotReturnArticle);
            }
            this.editable = data.editable;
            this.isDepotReturnArticle = ArticleReturnUtils.isDepotArticle(data);
            this.returnReason = data.returnReason;
        }
    }
}
