export interface ReturnBasketState {
    branchId?: string;
    positions?: ReturnBasketPositionState[];
    printConfirmDoc?: boolean;
    salesPersonalNumber?: string;
    validToExecute?: boolean;
    returnOrderJournalPostingInBatch?: boolean;
}

export interface ReturnBasketPositionState {
    axPaymentType?: string;
    cashDiscount?: string;
    quantity?: number;
    quarantine?: boolean;
    quarantineReason?: string;
    reasonCode?: string;
    transactionId?: string;
}

export const ReturnBasketPositionStateRecord = {
    quantity: 0,
    quarantine: false,
    quarantineReason: '',
    reasonCode: '',
    transactionId: '',
};
