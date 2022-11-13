export const OPENING_DAY_ROUTE = 'home/opening-day';
export const FILTER_DATE_TODAY = 'OPENING_DAY.FILTER_DATE.TODAY';
export const FILTER_DATE_2_DAYS = 'OPENING_DAY.FILTER_DATE.2_DAYS';
export const FILTER_DATE_7_DAYS = 'OPENING_DAY.FILTER_DATE.7_DAYS';
export const FILTER_DATE_31_DAYS = 'OPENING_DAY.FILTER_DATE.31_DAYS';

export const INTERVAL_THIRTY_ONE_DAYS = 30;
export const DELIVERY_ID: RegExp = /^[0-9]{1,19}$|^$/;


export const GET_ALL_COUNTRIES = 'wss/opening-days/countries/';
export const GET_ALL_AFFILIATES = 'admin/affiliates/short-infos';
export const GET_BRANCHES = 'wss/branch/branches';
export const GET_WORKING_DAY_CODE = 'wss/opening-days/working-day-code';
export const CREATE_OPENING_DAY = 'wss/opening-days/create';
export const UPDATE_OPENING_DAY = 'wss/opening-days/update';
export const GET_OPENING_DAY_ITEM = 'wss/opening-days/';
export const REMOVE_OPENING_ITEM = 'wss/opening-days/remove/';
export const GET_OPENING_DAY_LIST = 'wss/opening-days/search';
export const UPLOAD_FILE = 'wss/opening-days/import';
export const UPDATE_SHOW_NET_PRICE = 'wss/margin-by-article-group/update-show-net-price';
export const GET_CUSTOMER_SETTING_URL = 'customer/settings/default/';
export const GET_TOUR = 'wss/tour';
export const SEARCH_TOUR = 'wss/tour/search';
export const REMOVE_TOUR = 'wss/tour/remove';
export const UPDATE_TOUR = 'wss/tour/update';
export const CREATE_TOUR = 'wss/tour/create'

export const GET_DP = 'wss/delivery-profile';
export const SEARCH_DP = 'wss/delivery-profile/search';
export const REMOVE_DP = (wssDeliveryProfileId) => `wss/delivery-profile/${wssDeliveryProfileId}/remove`;
export const UPDATE_DP = 'wss/delivery-profile/update';
export const CREATE_DP = 'wss/delivery-profile/create'
export const ADD_DP_TOUR = 'wss/delivery-profile/tour/add'
export const UPDATE_DP_TOUR = 'wss/delivery-profile/tour/update'
export const REMOVE_DP_TOUR = (tourId) => `wss/delivery-profile/tour/${tourId}/remove`;

export const SORT_COLUMN_BRANCH_NUMBER = 'SORT_COLUMN_BRANCH_NUMBER';
export const SORT_COLUMN_BRANCH_CODE = 'SORT_COLUMN_BRANCH_CODE';
export const SORT_COLUMN_OPENING_TIME = 'SORT_COLUMN_OPENING_TIME';
export const SORT_COLUMN_LUNCH_OPENING_TIME = 'SORT_COLUMN_LUNCH_OPENING_TIME';
export const SORT_COLUMN_LUNCH_CLOSING_TIME = 'SORT_COLUMN_LUNCH_CLOSING_TIME';
export const SORT_COLUMN_CLOSING_TIME = 'SORT_COLUMN_CLOSING_TIME';

export const BRANCHES_SORT_MAP = {
    branchNr: 'orderDescByBranchNr',
    branchCode: 'orderDescByBranchCode',
    openingTime: 'orderDescByOpeningTime',
    lunchStartTime: 'orderDescByLunchStartTime',
    lunchEndTime: 'orderDescByLunchEndTime',
    closingTime: 'orderDescByClosingTime',
};

export const TIME_REGEX: RegExp = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
export const LUNCH_TIME_REGIX: RegExp = /^$|^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
export const BRANCH_NUMBER_REGEX: RegExp = /^[0-9]{1,10}$/;
export const ORG_ID_REGEX: RegExp = /^$|^[0-9]{1,10}$/;

export const GET_BRANCHES_URL = 'wss/branch/search';
export const GET_BRANCH_URL = 'wss/branch/';
export const CREATE_BRANCH_URL = 'wss/branch/create';
export const DELETE_BRANCH_URL = 'wss/branch/remove/';
export const UPDATE_BRANCH_URL = 'wss/branch/update';
export const GET_COUNTRY_LIST_FOR_BRANCH_URL = 'admin/country/shortcode';

export const PASTED_BRANCH_NUMBER_FIELD = 'PASTED_BRANCH_NUMBER_FIELD';
export const PASTED_ORG_FIELD = 'PASTED_ORG_FIELD';
export const PASTED_EMAIL_FIELD = 'PASTED_EMAIL_FIELD';
export const OPENING_HOUR = 'OPENING_HOUR';
export const CLOSING_HOUR = 'CLOSING_HOUR';
export const LUNCH_OPENING_HOUR = 'LUNCH_OPENING_HOUR';
export const LUNCH_CLOSING_HOUR = 'LUNCH_CLOSING_HOUR';

export const TIMEOUT = 1000;
export const NUMBER_ONLY = 'NUMBER_ONLY';
export const ALPHANUMBERIC = 'ALPHANUMBERIC';
export const TEXT = 'TEXT';

export const PROFILE_EDIT_MODE = {
    NEW_PROFILE: 'NEW_PROFILE',
    EDIT_PROFILE: 'EDIT_PROFILE',
    NEW_TOUR: 'NEW_TOUW',
    EDIT_TOUR: 'EDIT_TOUR'
};

export const NO_DEFAULT_ARTICLE_GROUP_SETTING = "NO_DEFAULT_ARTICLE_GROUP_SETTING";
export const NO_DEFAULT_BRAND_SETTING = "NO_DEFAULT_BRAND_SETTING";

export const ARTGROUP_EXPORT_TYPE = {
    ALL_ARTGROUP: 'SAG_ARTICLE_GROUP',
    MARGIN_DEF_AND_ALL_ARTGROUP: 'MARGIN_SAG_ARTICLE_GROUP',
    MARGIN_DEF: 'MARGIN_DEF'
}