import { ARTICLE_LIST_TYPE, SEARCH_MODE } from 'sag-article-list';

export const SEARCH_TYPE = {
    [SEARCH_MODE.TYRES_SEARCH]: ARTICLE_LIST_TYPE.TYPRE,
    [SEARCH_MODE.BULB_SEARCH]: ARTICLE_LIST_TYPE.BULB,
    [SEARCH_MODE.BATTERY_SEARCH]: ARTICLE_LIST_TYPE.BATTERY,
    [SEARCH_MODE.OIL_SEARCH]: ARTICLE_LIST_TYPE.OIL,
}