import { BATCH_JOB_STATUS } from "../enums/return-order.enum";

export interface ReturnOrderRequest {
    branchId?: string;
    persionalNumbeer: string;
    returnOrderName: string;
    returnOrderDocumentConfirmationPrint: boolean;
    returnOrderJournalPostingInBatch: boolean;
    returnOrderRequestPositions: ReturnOrderRequestPositions[];
}

export interface ReturnOrderRequestPositions {
    transId: string;
    quantity: number;
    isQuarantine: boolean;
    quarantineReason: string;
    returnReasonCodeId: string;
}

export interface ReturnOrderResponse {
    message: string;
    returnOrder: ReturnOrder;
    status: string;
}

export interface ReturnOrder {
    journalId: string;
    batchJobId: string;
    returnOrderUrl: string;
    returnOrderPositionList: ReturnOrderPosition[]
}

export interface ReturnOrderPosition {
    orderNr: string;
    orderUrl: string;
    quarantineOrder: boolean;
}

export interface ReturnOrderBatchJobs {
    batchJobs: BatchJobs[];
}

export interface BatchJobs {
    batchJobId: string;
    status: string;
}

export interface OrderBatchJobResponse {
    orderNumbers: OrderNumber[]
}

export interface OrderNumber {
    journalId: string,
    orderId: string
}
