export enum INVOICE_TYPE_CODE {
    SINGLE_INVOICE = 1,
    TWO_WEEKLY_INVOICE = 2,
    WEEKLY_INVOICE = 3,
    DAILY_INVOICE = 4,
    ACCUMULATIVE_INVOICE = 5,
    MONTHLY_INVOICE = 6
}

export enum ERP_INVOICE_TYPE {
    SINGLE_INVOICE = 'EINZELFAKT',
    SINGLE_INVOICE_WITH_CREDIT_SEPARATION = 'EINZELFAGT',
    WEEKLY_INVOICE = 'WOCHENFAKT'
}

export enum DEFAULT_INVOICE_TYPE {
    AGREEMENT_INVOICE = 0,
    SINGLE_INVOICE = 1
}
