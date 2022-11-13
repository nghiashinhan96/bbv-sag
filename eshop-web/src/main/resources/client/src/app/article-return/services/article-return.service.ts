import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { ReturnBasketState, ReturnBasketPositionState } from '../models/return-basket.state';
import { ReturnArticle } from '../models/return-article.model';

@Injectable()
export class ArticleReturnService {

    private baseUrl = environment.baseUrl;
    constructor(
        private http: HttpClient
    ) {

    }

    getReturnReasonCode() {
        const url = `${this.baseUrl}return/order/reason`;
        return this.http.get(url);
    }

    getReturnOderBatchjob(journalId: string) {
        const url = `${this.baseUrl}return/order/journals?journalId=${journalId}`;
        return this.http.get(url);
    }

    createReturnOrder(request: ReturnBasketState) {
        const url = `${this.baseUrl}return/order/create`;
        return this.http.post(url, request);
    }

    processInBackground(batchJobId: string, journalId: string) {
        const url = `${this.baseUrl}return/order/process-in-background?batchJobId=${batchJobId}&journalId=${journalId}`;
        return this.http.post(url, {});
    }

    asyncReturnOrderProcess(batchJobId: string) {
        const url = `${this.baseUrl}return/order/batch-jobs?batchJobId=${batchJobId}`;
        return this.http.get(url);
    }

    buildReturnBasketPositions(returnItemInBasket: ReturnArticle[]) {
        return returnItemInBasket.map(item => {
            return {
                quantity: +item.returnQuantity,
                quarantine: item.isQuarantined,
                quarantineReason: item.quarantineText,
                reasonCode: item.returnReason,
                transactionId: item.transId,
                cashDiscount: item.cashDiscount,
                axPaymentType: item.axPaymentType
            } as ReturnBasketPositionState;
        });
    }

    getSubmitReturnOrderErrorCode(error): string {
        if (!error.code) {
            return 'RETURN_ARTICLE_BASKET.ERRORS.SUBMIT';
        }
        return 'RETURN_ORDER.MESSAGE_ERROR_FROM_API';
    }
}
