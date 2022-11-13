/** Webstorage */
export const APP_CONFIGURATION = 'app.configuration';
export const APP_DEFAULT_SETTING = 'app.default.setting';
export const APP_TOKEN = `app.token`;
export const APP_SALE_TOKEN = `app.sale.token`;
export const APP_USER_SETTING = 'app.user.setting';
export const APP_VERSION = 'app.version';
export const APP_VEHICLE_FILTERING = 'app.vehicle.filtering';
export const APP_DEFAULT_PAGE_SIZE = 10;
export const APP_CUSTOMERS = `app.customers`;
export const APP_SELECTED_VEHICLE = 'app.vehicle.selected';
export const APP_REF_TEXT = 'app.ref.text';
export const APP_SALE_INFO = 'app.sale.info';
export const APP_ARTICLE_MODE = 'app.article.mode';
export const APP_FASTSCAN_ART = 'app.fastscan.art';
export const APP_ADVANCE_VEHICLE_SEARCH_MAKE = 'app.advance.vehicle.search.make';
export const APP_ADVANCE_VEHICLE_SEARCH_MODEL = 'app.advance.vehicle.search.model';

export const APP_SUB_ORDER_BASKET = 'APP_SUB_ORDER_BASKET';
export const APP_ORDERED = 'APP_ORDERED';
export const NONVEHICLE = 'NONVEHICLE';
export const APP_RETURN_ARTICLE = 'app.return.article';
export const APP_HEADER_SEARCH_CHANGE_EVENT = 'app.header.search.change.event';
export const APP_SHOPPING_CART_UPDATED_EVENT = 'app.shopping.cart.update.event';
export const APP_VAT_CONFIRM = 'app.vat.confirm';
export const APP_THU_LE_MESSAGE = 'app.thule.message';
export const APP_SALE_USER_NAME = 'app.sale.username';
export const APP_SHOP_CUSTOMER = 'app.shop.customer';
export const APP_DEFAULT_PAGE = 1;
export const SELECTED_FAVORITE_ITEM = 'app.favorite.selected.item';
export const SELECTED_FAVORITE_LEAF = 'app.favorite.selected.leaf';
export const APP_ANALYTIC_EVENT_BASKET_ITEM_SOURCE = 'app.analytic.event.basket.item.source';
export const APP_DIGI_INVOICE_HASH_CODE = 'app.digi.invoice.hash.code';
/** End webstorage */
/** Lib Storage */

/** End lib storage */
/**
 * App Error
 */
export const API_TIMEOUT_EXCEEDED_ERROR = 'API_TIMEOUT';
export const ERP_TIMEOUT_EXCEEDED_ERROR = 'ERP_TIMEOUT';
export const TIMEOUT_EXCEEDED_ERROR = 'TIMEOUT_ERROR';
export const EMAIL_ERROR = 'email_failed';
export const LEGAL_TERM_NOT_ACCEPTED = 'legal_term_not_accepted';

export const USER_SETTING = 'app.setting.user_setting';
export const X_SAG_REQUEST_ID_HEADER_NAME = 'X-SAG-Request-Id';
export const TIME_OUT_MESSAGE = ['ORDER.ERROR.TIMEOUT', 'ORDER.ERROR.AX_TIMEOUT', 'UNKNOWN'];
export const SHOPPING_ORDER_PAGE = 'shopping-basket/order';
export const SHOPPING_BASKET_PAGE = 'shopping-basket/cart';
export const NOT_AVAILABLE = 'N/A';
export const HYPHEN = '-';
export const MESSAGE_SUCCESS = 'SETTINGS.MESSAGE_SUCCESSFUL';

export class Constant {
    public static readonly APP_PREFIX = 'cnt';
    public static readonly LF = '\r\n';
    public static readonly HYPHEN_WITH_SPACES_AROUND = ' - ';
    public static readonly SPACE = ' ';
    public static readonly COMMA_HAS_SPACE = ', ';
    public static readonly SEMICOLON = ';';
    public static readonly SEMICOLON_HAS_SPACE = '; ';
    public static readonly SEPARATE_DOT = ':';
    public static readonly SEPARATE_DOT_HAS_SPACE = ': ';
    public static readonly COLON_SPACE = ': ';
    public static readonly COLON = ':';
    public static readonly SLASH = '/';
    public static readonly DOT = '.';
    public static readonly HASH = '#';
    public static readonly COMMA = ',';
    public static readonly EMPTY_STRING = '';
    public static readonly ASC = 'ASC';
    public static readonly DESC = 'DESC';
    public static readonly ASC_LOWERCASE = 'asc';
    public static readonly DESC_LOWERCASE = 'desc';
    public static readonly UNDERSCORE = '_';
    public static readonly NULL_AS_STRING = 'null';
    public static readonly TOKEN_NAME = '_token';
    public static readonly LANG = 'lang';
    public static readonly KEY_EMITTER_PARTS = 'channel_part';
    public static readonly KEY_EMITTER_SELECTED_CATEGORIES = 'channel_selected_catagories';
    public static readonly KEY_EMITTER_SELECTED_CATEGORIES_QUICKCLICK = 'selected_categories_quickclick';
    public static readonly KEY_EMITTER_UNSELECTED_CATEGORY = 'unselected_category';
    public static readonly KEY_SELECTED_GAIDS_GTMOTIVE = 'selected_GaIds_GTMotive';
    public static readonly KEY_EMITTER_EXPORT_FILE = 'export_file';
    public static readonly KEY_REMOVE_GTMOTIVE_ARTICLES = 'remove_gtmotive_articles';
    public static readonly KEY_ALL_ARTICLES_ARE_NONE_REFERENCED = 'all_articles_NoneReferenced';
    public static readonly KEY_PUBLIC_EVENT = 'public_event';
    public static readonly KEY_SELECTED_GAIDS = 'selected_gaids';
    public static readonly KEY_SELECTED_VEHID = 'selected_vehId';
    public static readonly KEY_EMPLOYEE = 'employee';
    public static readonly DEFAULT_SEARCH_BOX_SIZE = 'small';
    public static readonly KEY_EMITTER_CART_QUANTITY = 'cart_quantity';
    public static readonly KEY_ARTICLE_LIST_SEARCHED = 'article_list_stored';
    public static readonly GROSS = 'GROSS';

    // poll handing related section
    public static readonly KEY_TURNOFF_QUICKVIEW_SERVICE = 'key_TurnOffQuickView';
    public static readonly KEY_TURNON_QUICKVIEW_SERVICE = 'key_TurnOnQuickViewService';
    public static readonly KEY_UNSUBSCRIBE_QUICKVIEW_SERVICE = 'key_UnsubscibeQuickViewService';
    public static readonly KEY_RESET_ARTICLE_FILTER = 'key_reset_article_filter';
    public static readonly HOME_PAGE = '/home';
    public static readonly TYRE_PAGE = '/tyres';
    public static readonly LOGIN_PAGE = '/login';
    public static readonly DVSE_PAGE = '/dvse';
    public static readonly SHOPPING_LIST_PAGE = '/article-list';
    public static readonly AUTH_TOKEN = '/oauth/token';
    public static readonly USER_SETTING_PAGE = '/myaccount/settings';
    public static readonly USER_MY_ORDER_PAGE = '/myaccount/my-order';
    public static readonly FORGOT_PASSWORD_PAGE = '/forgotpassword';
    public static readonly RESET_CODE_PAGE = '/forgotpassword/verifycode';
    public static readonly RESET_PASSWORD_PAGE = '/forgotpassword/reset';
    public static readonly BASKET_PAGE = '/shoppingbasket';
    public static readonly RETURN_ARTICLE_PAGE = '/return/basket';
    public static readonly RESULT_PAGE = '/results';
    public static readonly OFFER_PAGE = '/offers';
    public static readonly POLL_INTERVAL: number = 3000;
    public static readonly CUSTOMER_INFO_REPRESENTED = 'customer_info_represented_by_sale';
    public static readonly FETCH_PRICES_IN_CART = 'fetchPricesQuickviewEmitter';
    public static readonly SELECTED_OLYSLAGER_TYPE_ID = 'SELECTED_OLYSLAGER_TYPE_ID';
    public static readonly BASKET_QUICK_VIEW_AND_TOTAL = 'basketQuickviewEmitter';
    public static readonly HEADER_TOTAL: string = 'MINI_CART_QUANTITY_TOTAL';
    public static readonly LOAD_MINI_CART: string = 'LOADMINICART';
    public static readonly LOAD_REF_TEXT_BASKET: string = 'LOAD_REF_TEXT_BASKET';
    public static readonly KEY_REF_TEXT_BASKET = 'KEY_TEXT_REF_BASKET_';
    public static readonly IS_ORDER_REQUEST = 'isOrderRequest';
    public static readonly ORDER_RESPONSE = 'orderResponse';
    public static readonly ORDER_TYPE = 'orderType';
    public static readonly DELIVERY_TYPE = 'deliveryType';
    public static readonly SALES_USER_NAME = 'salesUsername';
    public static readonly API_TIMEOUT_EXCEEDED_ERROR = 'API_TIMEOUT';
    public static readonly ERP_TIMEOUT_EXCEEDED_ERROR = 'ERP_TIMEOUT';
    public static readonly API_GENERAL_ERROR_MESSAGE = 'Server error!';
    public static readonly ORDER_TIMEOUT = 120000; // 2 minutes for order
    public static readonly VIN_MAX_LENGTH = 17;
    public static readonly VERHICLE_SEARCH_MAX_LENGTH = 6;
    public static readonly TYRE_SEARCH_HISTORY = 'tyre_search_history';
    public static readonly TYRE_SEARCH_HISTORY_MODE = 'tyre_search_history_mode';
    public static readonly MOTOR_TYRE_SEARCH_HISTORY = 'motor_tyre_search_history';
    public static readonly VIN_URL_KEY = 'vinUrl';
    public static readonly NOT_FOUND_PAGE_EMITTER_KEY = 'not_found_page_emitter';
    public static readonly TYRE_RESULT_PAGE = '/results/tyres';
    public static readonly BATTERY_RESULT_PAGE = '/results/batteries';
    public static readonly BULB_RESULT_PAGE = '/results/bulbs';
    public static readonly OIL_RESULT_PAGE = '/results/oil';

    public static readonly LANG_CODE_DE = 'de';
    public static readonly LANG_CODE_IT = 'it';
    public static readonly LANG_CODE_FR = 'fr';
    public static readonly ID_PREFIX = 'ID: ';
    public static readonly DEFAULT_PAGE_SIZE = 10;
    public static readonly DEFAULT_OFFSET = 0;
    public static readonly BADREQUEST_CODE = 400;
    public static readonly UNAUTHORIZED_CODE = 401;
    public static readonly NOT_FOUND_CODE = 404;
    public static readonly FORBIDDEN_CODE = 403;
    public static readonly ACCESS_DENIED_CODE = 'access_denied';
    public static readonly INTERNAL_SERVER_ERROR_CODE = 500;
    public static readonly INVALID_TOKEN_ERROR_CODE = 'invalid_token';
    public static readonly INVALID_VERSION_ERROR_CODE = 'INVALID_VERSION';
    public static readonly APP_VERSION_KEY = 'appVersion';
    public static readonly FIRST_PAGE_INDEX = 0;
    public static readonly SALE_COMPANY_INFO = '000000 - SAG';
    public static readonly MESSAGE_SUCCESS = 'SETTINGS.MESSAGE_SUCCESSFUL';
    public static readonly POSITION_BOTTOM_TEXT = 'bottom';
    public static readonly DEFAULT_URL_KEY = 'default_url';
    // search key text in homepage -> move to list part
    public static readonly SEARCH_TEXT = 'searchText[list-part]';
    // Offers
    public static readonly TYPE_WORK = 'WORK';
    public static readonly TYPE_ARTICLE = 'ARTICLE';
    public static readonly SELECTED_OFFER = 'selectedOfferDetail';
    public static readonly START_POLL = 'START_POLL';
    public static readonly PKW_TYRE = 'pkw';
    public static readonly MOTOR_TYRE = 'motor';
    public static readonly EQUAL = '=';
    public static readonly QUESTION_MARK = '?';
    public static readonly ADDITIONAL_TEXTDOC_PRINTER = 'Bel_Position';
    public static readonly TRUE_AS_STRING = 'true';
    public static NUMBER_ITEMS_REQUEST_AVAILABILITY = 5;
    public static AVAIL_STATE_INORDER_24_HOURS = 10;
    // File extension
    public static CSV_FILE_EXT = '.csv';
    public static EXCEL_FILE_EXT = '.xls';
    public static WORD_FILE_EXT = '.docx';
    public static RTF_FILE_EXT = '.rtf';
    public static readonly TH1_ORDER_TYPE = 'TH1';
    public static readonly SAG_ARTICLE_NR_LENGTH = 10;
    public static readonly EAN_CODE_13_DIGITS = 13;
    public static readonly EAN_CODE_12_DIGITS = 12;
    public static readonly MESSAGE_TYPE_BANNER = 'BANNER';
    public static readonly MESSAGE_METHOD_ONCE = 'ONCE';
    public static readonly MESSAGE_METHOD_SECTION_HIDING = 'UNTIL_CLOSE_AS_USER_SECTION';
    public static readonly SECTION_HIDDEN_MESSAGES_KEY = 'hidden_messages';
    public static readonly LOADING_SHOPPING_BASKET_KEY = 'loading_shopping_basket';
    public static readonly CLASSIC_CATS_EMITTER_KEY = 'classicCatsEmitter';
    public static readonly SELECTED_CLASSIC_CATS_EMITTER_KEY = 'selectedClassicCatsEmitter';
    public static readonly CLASSIC_CATE_VIEW_KEY = 'classicCategoryView';
    public static readonly KEY_SELECTED_CATEGORIES = 'selected_categories';
    public static readonly VEHICLE_HISTORY_EMITTER_KEY = 'vehicleHistoriesEmitter';
    public static readonly ORDER_CONDITION_MESG_KEY = 'orderConditionMesgKey_';
    public static readonly SINGLE_SELECT_MODE_KEY = 'currentStateSingleSelectMode';

    public static readonly SEARCH_BY_BARCODE_KEY = 'search_by_barcode';
    public static readonly ONBEHALF_BY_SHOP_CUSTOMER_KSL_KEY = 'onbehalf_by_shop_cust_ksl';
    public static readonly ENABLE_FASTCAN_SEARCH_MODE_FOR_SHOP_CUST_KEY = 'enable_fastscan_search_mode_for_shop_cust';
    public static readonly HANDLE_MESSAGE_ADD_BY_FAST_SCAN = 'handle_message_add_by_fastscan';

    public static readonly BULB_PAGE = '/bulbs';
    public static readonly BULBS_SEARCH_HISTORY = 'bulbs_search_history';

    public static readonly BATTERY_PAGE = '/batteries';
    public static readonly BATTERY_SEARCH_HISTORY = 'battery_search_history';
    public static readonly INTERCONNECTION_PREFIX = 'Schema';
    public static readonly VOLTAGE_DEFAULT = '12';
    public static readonly TYPE_OF_POLE_PREFIX = 'Polart';

    // oil
    public static readonly OIL_SEARCH_HISTORY = 'oil_search_history';
    public static readonly OIL_PAGE = '/oil';

    // At vehicle search
    public static readonly VEHICLE_SEARCH_FREETEXT = 'freeText';
    public static readonly VEHICLE_SEARCH_DESC_YEAR = 'vehiclesDescAndYear';
    public static readonly VEHICLE_SEARCH_VIN = 'vin';

    // for feedback
    public static readonly FEEDBACK_TECHNICAL_DATA_KEY = 'FEEDBACK_TECHNICAL_DATA';
    public static readonly AFFILIATE_STORE_KEY = 'AFFILIATE_STORE';
    // Time out message
    public static readonly TIME_OUT_MESSAGE = ['ORDER.ERROR.TIMEOUT', 'ORDER.ERROR.AX_TIMEOUT'];

    // for date range filter
    public static readonly EPOCH_TIME_IN_DAY = 86400;
    public static readonly FILTER_RANGE_DATE = 31;
    public static readonly MILLISECOND = 1000;
    public static readonly errorMessageInFilterDateRange = 'SEARCH.ERROR_MESSAGE.EXCEED_31_DAYS';
    public static readonly errorMessageToDateChanged = 'SEARCH.ERROR_MESSAGE.TO_DATE_CHANGED';
    public static readonly errorMessageFromDateChanged = 'SEARCH.ERROR_MESSAGE.FROM_DATE_CHANGED';

    // Return Item Search
    public static readonly RETURN_ITEM_FROM_SERVER = 'RETURN_ITEM_FROM_SERVER';
    public static readonly RETURN_ITEM_ADDED_IN_BASKET = 'RETURN_ITEM_ADDED_IN_BASKET';
    public static readonly RETURN_ARTICLE_RESPONSE = 'RETURN_ARTICLE_RESPONSE';
    public static readonly RETURN_ORDER_CONFIRMATION_PAGE = '/return/order-confirmation';
    public static readonly CHECK_ALL_ARTICLES_FOR_DELETE = 'CHECK_ALL_ARTICLES_FOR_DELETE';
    public static readonly UNCHECK_ALL_ARTICLES_FOR_DELETE = 'UNCHECK_ALL_ARTICLES_FOR_DELETE';
    public static readonly REMOVE_ALL_CHECKED_ARTICLES_FOR_DELETE = 'REMOVE_ALL_CHECKED_ARTICLES_FOR_DELETE';
    public static readonly UPDATE_REASON = 'UPDATE_REASON';
    public static readonly UPDATE_RETURN_QUANTITY = 'UPDATE_RETURN_QUANTITY';
    public static readonly CHECKED_FOR_DELETE_ALL = 'CHECKED_FOR_DELETE_ALL';
    public static readonly DELETE_SELECTED_ITEM = 'DELETE_SELECTED_ITEM';
    public static readonly EMIT_RETURN_PAGE_SELECTED = 'EMIT_RETURN_PAGE_SELECTED';

    // article displayed mode
    public static readonly SIMPLE_MODE = 'SIMPLE_MODE';
    public static readonly DETAIL_MODE = 'DETAIL_MODE';
    public static readonly ARTICLE_DISPLAYED_MODE = 'ARTICLE_DISPLAYED_MODE';

    // header module
    public static readonly HEADER_LOAD_TEMP_TOTAL_QUANTITY = 'HEADER_LOAD_TEMP_TOTAL_QUANTITY';
    public static readonly KEY_SECURE_CODE = 'key_secureCode';
    public static readonly KEY_RESETPASSWORD_HASH_USERNAME_CODE = 'key_ResetPasswordHashUsernameCode';

    // Order dashboard
    public static readonly ORDER_DASHBOARD_NEW_ORDERS_PAGE = '/order-dashboard/new-orders';
    public static readonly ORDER_DASHBOARD_MY_CUSTOMER_ORDERS_PAGE = '/order-dashboard/my-customer-orders';
    public static readonly ORDER_DASHBOARD_ORDERED_PAGE = '/order-dashboard/ordereds';
    public static readonly HAS_WHOLESALER_PERMISSION = 'has_wholesaler_permission';
    public static readonly CURRENT_ORDER_PLACE_ORDER = 'current_order_place_order';
    public static readonly ORDERED_QUANTITY = 'ordered_quantity';

    // Goods receiver
    public static readonly GOOD_RECEIVER = 'goods_receiver';

    // Place order for final customer
    public static readonly SHOP_TYPE = 'shop_type';

    public static readonly SUB_BASKET_TEXT = 'sub_basket';

    public static readonly ALL_OPTION = ' ';
    public static readonly ORDER_STATUS_NEW = 'NEW';
    public static readonly ORDER_STATUS_OPEN = 'OPEN';
    public static readonly ORDER_STATUS_ORDERED = 'ORDERED';

    public static readonly SELECTED_FINAL_USER_ORDER_HISTORY = 'selected_final_user_order_history';
    public static readonly FILTER_ORDER_HISTORY = 'filter_order_history';
    public static readonly SUB_ORDER_BASKET = 'SUB_ORDER_BASKET';

    public static readonly DEMO = ' - Demo';

    // Json event
    public static readonly JSON_EVENT_LIST = 'event_list';
    public static readonly LOGIN_LOGOUT_EVENT = 'login_page';
    public static readonly TYRES_EVENT = 'tyressearch';
    public static readonly TYRES_EVENT_PKW = 'PKW/LNFZ';
    public static readonly TYRES_EVENT_MOTORRAD = 'Motorrad';

    public static readonly BATTERIES_EVENT = 'batteriessearch';
    public static readonly OIL_EVENT = 'oilsearch';
    public static readonly SOURCING_TYPE_KSO = 'KSO';

    public static readonly SELECTED_ARTICLE_CATEGORY = 'selected_article_category';
    public static readonly QUICK_CLICK_EVENT = 'quick_click_event';
    public static readonly SELECTED_MAIN_ARTICLE = 'SELECTED_MAIN_ARTICLE';

    // Translation key
    public static readonly FILTER_OPTION_ALL_KEY = 'Alle';
    public static readonly KEY_INVALID_CODE = 'FORGOT_PASSWORD.INVALID_CODE';
    public static readonly KEY_EXPIRED = 'FORGOT_PASSWORD.TOKEN_EXPIRED';


    public static readonly CHECKOUT_PAGE = '/shopping-basket/checkout';

    // Permission
    public static readonly PERMISSIONS = { UNIPARTS: 'UNIPARTS', DVSE: 'DVSE'}
}

export const REGEX = {
    EMAIL_REGEX: /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/
};


export const SALE_ACCESSIBLE_URLS: string[] = [
    '/home',
    '/settings/profile',
    '/settings/order-history',
];

export const DAY_IN_WEEK: string[] = [
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY',
];