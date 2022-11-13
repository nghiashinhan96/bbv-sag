export enum SHOPPING_BASKET_ENUM {
    DEFAULT = 'DEFAULT_SHOPPING_CART',
    FINAL = 'SUB_FINAL_SHOPPING_CART'
}

export enum DELIVERY_TYPE {
    TOUR = 'TOUR',
    PICKUP = 'PICKUP',
    COURIER = 'COURIER',
    COUNTER = 'COUNTER'
}

export enum ORDER_TYPE {
    ORDER = 'ORDER',
    COUNTER = 'COUNTER',
    ABS = 'ABS',
    STD = 'STD',
    KSO_AUT = 'KSO_AUT'
}

export enum ADDRESS_TYPE {
    DEFAULT = 0,
    INVOICE = 1,
    DELIVERY = 2
}

export enum INVOICE_TYPE {
    SINGLE_INVOICE = 'EINZELFAKT',
    SINGLE_INVOICE_WITH_CREDIT_SEPARATION = 'EINZELFAGT',
    WEEKLY_INVOICE = 'WOCHENFAKT'
}

export enum PAYMENT_METHOD {
    CREDIT = 'CREDIT',
    CASH = 'CASH',
    CARD = 'CARD',
    DIRECT_INVOICE = 'DIRECT_INVOICE',
    BANK_TRANSFER = 'BANK_TRANSFER',
    EUR_PAYMENT = 'EUR_PAYMENT',
    WHOLESALE = 'WHOLESALE'
}

export enum AX_SEND_METHOD {
    TOUR = 'TOUR'
}

export enum AX_PAYMENT_TYPE {
    SOFORT = 'Sofort',
    BAR = 'Bar'
}

export enum PAYMENT_INFO_TYPE {
    CREDIT = 'Credit',
    CASH = 'Cash',
    CARD = 'Card',
    DIRECT_INVOICE = 'Direct invoice',
    EUR_PAYMENT = 'EUR Payment',
    BANK_TRANSFER = 'Bank Transfer',
    WHOLESALE = 'Wholesale'
}
