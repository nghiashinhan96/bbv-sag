export class ReturnArticleItemEvent {
    returnTransId: number;
    returnArticleId: string;
    returnArticleName: string;
    returnBranchId: number;
    returnCashDiscount: string;
    returnOrderNr: string;
    returnAxPaymentType: string;
    returnQuantity: number;
    returnReasonOfReturn: string;
    returnQuarantine: string;

    constructor(data: any) {
        if (data) {
            this.returnTransId = data.returnTransId;
            this.returnArticleId = data.returnArticleId;
            this.returnArticleName = data.returnArticleName;
            this.returnBranchId = data.returnBranchId;
            this.returnCashDiscount = data.returnCashDiscount;
            this.returnOrderNr = data.returnOrderNr;
            this.returnAxPaymentType = data.returnAxPaymentType;
            this.returnQuantity = +data.returnQuantity;
            this.returnReasonOfReturn = data.returnReasonOfReturn;
            this.returnQuarantine = data.returnQuarantine;
        }
    }

    toEventRequest() {
        return {
            return_trans_id: this.returnTransId,
            return_article_id: this.returnArticleId,
            return_article_name: this.returnArticleName,
            return_branch_id: this.returnBranchId,
            return_cash_discount: this.returnCashDiscount,
            return_order_nr: this.returnOrderNr,
            return_ax_payment_type: this.returnAxPaymentType,
            return_quantity: this.returnQuantity,
            return_reason_of_return: this.returnReasonOfReturn,
            return_quarantine: this.returnQuarantine
        };
    }
}
