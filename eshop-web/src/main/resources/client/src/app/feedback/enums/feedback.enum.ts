export enum FeedbackUserType {
    CUSTOMER,
    SALES_NOT_ON_BEHALF,
    SALES_ON_BEHALF
}

export enum FeedbackDataItemKey {
    // for user data
    SALES_INFO = 'salesInfo',
    CUSTOMER_INFO = 'customerInfo',
    CUSTOMER_EMAIL = 'customerEmail',
    CUSTOMER_PHONE = 'customerPhone',
    CUSTOMER_TOWN = 'customerTown',
    CUSTOMER_CONTACT = 'customerContact',
    DEFAULT_BRANCH = 'defaultBranch',
    USER_PHONE = 'userPhone',
    // for technical data
    WEBSITE = 'website',
    FREE_TEXT_SEARCH_KEY = 'freeTextSearch',
    VEHICLE_SEARCH = 'vehicleSearch',
    ARTICLE_SEARCH = 'articleSearch',
    OFFER_NR = 'offerNr',
    VEHICLE = 'vehicle',
    USER_SETTINGS = 'userSettings',
    ARTICLE_RESULTS = 'articleResults',
    ALLOW_VIEW_BILLING_CHANGED = 'allowViewBillingChanged',
    ALLOW_NET_PRICE_CHANGED = 'allowNetPriceChanged',
    NET_PRICE_CONFIRM = 'netPriceConfirm',
    CATEGORY_VIEW_MODE = 'categoryViewMode',
    EMAIL_NOTIFICATION_ORDER = 'emailNotification',
    INVOICE_TYPE = 'invoiceType',
    PAYMENT_METHOD = 'paymentMethod',
    DELIVERY_TYPE = 'deliveryType',
    COLLECTION_DELIVERY = 'collectionDelivery',
    DELIVERY_ADDRESS = 'deliveryAddress',
    BILLING_ADDRESS = 'billingAddress',
    SHOPPING_CART = 'shoppingCart',
    SHOPPING_CART_ITEM = 'shoppingCartItem'
}
