export const NUMBER_ONLY = 'NUMBER_ONLY';
export const ALPHANUMBERIC = 'ALPHANUMBERIC';
export const TEXT = 'TEXT';
export const TIMEOUT = 1000;
export const OPENING_HOUR = 'OPENING_HOUR';
export const CLOSING_HOUR = 'CLOSING_HOUR';
export const LUNCH_OPENING_HOUR = 'LUNCH_OPENING_HOUR';
export const LUNCH_CLOSING_HOUR = 'LUNCH_CLOSING_HOUR';
export const BRANCH_NUMBER_REGEX: RegExp = /^[0-9]{1,10}$/;
export const ORG_ID_REGEX: RegExp = /^$|^[0-9]{1,10}$/;
export const TIME_REGEX: RegExp = /^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
export const LUNCH_TIME_REGIX: RegExp = /^$|^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$/;
/* tslint:disable */
export const EMAIL_REGEX: RegExp = /^$|^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/;
export const PASTED_BRANCH_NUMBER_FIELD = 'PASTED_BRANCH_NUMBER_FIELD';
export const PASTED_ORG_FIELD = 'PASTED_ORG_FIELD';
export const PASTED_EMAIL_FIELD = 'PASTED_EMAIL_FIELD';

export const GET_BRANCHES_URL = 'admin/branch/search';
export const GET_BRANCH_URL = 'admin/branch/';
export const CREATE_BRANCH_URL = 'admin/branch/create';
export const DELETE_BRANCH_URL = 'admin/branch/remove/';
export const UPDATE_BRANCH_URL = 'admin/branch/update';
export const GET_COUNTRY_LIST_FOR_BRANCH_URL = 'admin/country/shortcode';

export const CREATE_NEW_BRANCH = '1';
export const EDIT_SELECTED_BRANCH = '0';
export const BRANCH_ROUTE = '/home/branches';
export const CREATE_BRANCH_ROUTE = '/home/branches/create';
export const EDIT_BRANCH_ROUTE = '/home/branches/edit';

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
}

export const DAY_IN_WEEK: string[] = [
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY',
    'SATURDAY',
    'SUNDAY',
];