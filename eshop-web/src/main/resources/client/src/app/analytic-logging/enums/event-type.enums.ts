export enum AnalyticEventType {
    LOGIN_LOGOUT_EVENT = 'login_page',
    BATTERIES_EVENT = 'batteriessearch',
    BULB_EVENT = 'bulbssearch',
    OIL_EVENT = 'oilsearch',
    TYRE_EVENT = 'tyressearch',
    COUPON_EVENT = 'coupon',
    ARTICLE_LIST_EVENT = 'article_list',
    SHOPPING_ORDER_EVENT = 'order',
    TRANSFER_AX_BASKET_EVENT = 'transfer_basket',
    OFFER_AX_BASKET_EVENT = 'offer_basket',
    SHOPPING_BASKET_EVENT = 'shopping_basket',
    FULL_TEXT_SEARCH_EVENT = 'fulltextsearch',
    FULL_TEXT_SEARCH_RETURN_EVENT = 'fulltextsearch_return',
    ARTICLE_CATEGORY_EVENT = 'article_category',
    VEHICLE_SEARCH_EVENT = 'fahrzeugsuche',
    FULL_TEXT_SEARCH_ARTICLE_EVENT = 'teilesuche',
    ARTICLE_RESULTS = 'article_results',
    WSP_CATALOG_EVENT = 'wspcatalog'
}

export enum ShoppingBasketEventType {
    OFFER = 'offers',
    ORDER_HISTORY = 'orders',
    SAVED_BASKETS = 'saved baskets',
    INVOICE = 'invoices',
    ARTICLE_LIST = 'article list',
    GTMOTIVE = 'gtmotive',
    HAYNESPRO = 'haynespro',
    BASKET = 'basket',
    SHOPPING_BASKET_PAGE = 'shopping_basket_page',
    THULE = 'thule',
    FAST_SCAN = 'fast scan',
    DVSE = 'dvse',
    WSP = 'wsp-catalog',
    OCI = 'oci-adopt'
}

export enum WspEventType {
    SEARCH_BOX = 'search_box',
    LEAF_NODE = 'leaf_node',
    LINK = 'link',
    GENART = 'genart',
    SHOW_MORE = 'show_more'
}

export enum LIST_NAME_TYPE {
    ARTICLE_SEARCH_RESULTS = 'Article Search Results',
    VEHICLE_SEARCH_RESULTS = 'Vehicle Search Results',
    TYRE_SEARCH_RESULTS = 'Tyre Search Results',
    UNIPARTS_CATALOG_RESULTS = 'Uniparts Catalog Results',
    SHOPPING_LIST_RESULTS = 'Shopping List Results',
    BATTERY_SEARCH_RESULTS = 'Battery Search Results',
    BULB_SEARCH_RESULTS = 'Bulb Search Results',
    OIL_SEARCH_RESULTS = 'Oil Search Results',
    CART_ITEM = 'Cart Items',
}

export enum SUB_LIST_NAME_TYPE {
    PARTS_LIST_ITEMS = 'Parts List Items',
    ACCESSORY_LIST_ITEMS= 'Accessory List Items',
    REPLACED_BY_ITEMS = 'Replaced by Items',
    CROSS_REFERENCE_ITEMS = 'Cross Reference Items'
}

export enum POP_UP_NAME {
    PARTS_LIST = 'Parts list popup',
    ACCESSORY_List= 'Accessory List Items',
    REPLACED = 'Replaced by popup',
    CROSS = 'Cross reverences popup'
}