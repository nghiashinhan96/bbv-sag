export const SAG_COMMON_ASC = 'ASC';
export const SAG_COMMON_DESC = 'DESC';
export const SAG_COMMON_ASC_LOWERCASE = 'asc';
export const SAG_COMMON_DESC_LOWERCASE = 'desc';
export const SAG_COMMON_VIN_MAX_LENGTH = 17;
export const SAG_COMMON_EMAIL_REGEX = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
export const SAG_COMMON_LANG_CODE = 'sag.common.lang.code';
export const SAG_COMMON_LOCALE = 'sag.common.locale';
export const SAG_COMMON_CUSTOMER_CURRENCY = 'sag.common.customer.currency';
export const SAG_COMMON_OIL_IDS = 'lib.selected.oil.ids';
export const SAG_COMMON_LANG_DE = 'de';
export const SAG_COMMON_DATETIME_FORMAT = 'dd.MM.yyyy HH:mm';
export const SAG_COMMON_RESPONSE_CODE = {
    BADREQUEST: 400,
    UNAUTHORIZED: 401,
    NOT_FOUND: 404,
    FORBIDDEN: 403,
    INTERNAL_SERVER_ERROR: 500,
    ACCESS_DENIED: 'access_denied',
    INVALID_TOKEN_ERROR: 'invalid_token',
    INVALID_VERSION_ERROR: 'INVALID_VERSION'
};

export const SAG_AVAIL_DISPLAY_OPTIONS = {
    NONE: 'NONE',
    DOT: 'DOT',
    DISPLAY_TEXT: 'DISPLAY_TEXT',
    DROP_SHIPMENT: 'DROP_SHIPMENT'
}

export const SAG_AVAIL_DISPLAY_STATES = {
    SAME_DAY: 'SAME_DAY',
    NEXT_DAY: 'NEXT_DAY',
    PARTIALLY_AVAILABLE: 'PARTIALLY_AVAILABLE',
    NOT_AVAILABLE: 'NOT_AVAILABLE',
    NOT_ORDERABLE: 'NOT_ORDERABLE',
    IN_STOCK: 'IN_STOCK'
}

export const CVP_VALUE_IS_MISSING_ERR_MSG = 'cvp is null';
