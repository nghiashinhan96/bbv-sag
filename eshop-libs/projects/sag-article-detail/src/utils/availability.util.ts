import { AVAILABILITY_INFO, CZ_AVAIL_STATE, SB_LOCATION_STATE } from '../enums/availability-info.enum';
import * as moment_ from 'moment';
import { AffiliateUtil, SAG_AVAIL_DISPLAY_OPTIONS, SAG_AVAIL_DISPLAY_STATES, SagAvailDisplaySettingModel, SagEditorLanguageModel } from 'sag-common';

import { ArticleAvailabilityModel } from '../models/article-availability.model';
import { ArticleModel } from '../models/article.model';

const moment = moment_;

// @dynamic
export class AvailabilityUtil {

    static isArticle24h(availabilities: any[]) {
        if (!availabilities || availabilities.length === 0) {
            return false;
        }
        return availabilities.some(a => a.availState === AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS);
    }

    static isArticleCon(availabilities: any[]) {
        if (!availabilities || availabilities.length === 0) {
            return false;
        }
        return availabilities.some(a => a.conExternalSource);
    }

    static intialAvailSortState(availabilities: ArticleAvailabilityModel[], isGroup = true, isCz = false) {
        if (!availabilities || availabilities.length === 0) {
            return AVAILABILITY_INFO.IS_ARTICLE_24H_STATE;
        }

        if (availabilities[0].type === 'AUTONET') {
            return availabilities[0].availState;
        }

        if (this.isArticle24h(availabilities)) {
            return AVAILABILITY_INFO.IS_ARTICLE_24H_STATE;
        }

        if (availabilities[0].sofort) {
            return AVAILABILITY_INFO.IS_SOFORT_STATE;
        }

        if (isGroup) {
            if (this.isArticleCon(availabilities)) {
                return AVAILABILITY_INFO.IS_ARTICLE_24H_STATE;
            }
        }

        let availability = isCz && availabilities.length > 1 ? availabilities[1] : availabilities[0];

        if (availability.formattedCETArrivalDate || availability.formattedCETArrivalTime) {
            return AVAILABILITY_INFO.IS_TOUR_STATE;
        } else {
            return AVAILABILITY_INFO.IS_ARTICLE_24H_STATE;
        }
    }

    public static getAvailInText(availabilities: ArticleAvailabilityModel[]) {
        if (!availabilities || !availabilities.length) {
            return AVAILABILITY_INFO.NOT_AVAILABILITY;
        }
        return availabilities[0].sendMethodCode ? availabilities[0].sendMethodCode.toLowerCase() : AVAILABILITY_INFO.NOT_AVAILABILITY;
    }

    public static sortAvailWithLatestTime(availabilities) {
        return (availabilities || []).sort((item1, item2) => {
            if(item1.backOrder) {
                return -1;
            }
            if(item2.backOrder) {
                return 1;
            }
            // latest show first
            return new Date(item2.arrivalTime).getTime() - new Date(item1.arrivalTime).getTime();
        });
    }

    static isArticle24hCz(availabilities: any[]) {
        if (!availabilities || availabilities.length === 0) {
            return false;
        }
        return availabilities.some(a => a.availState === CZ_AVAIL_STATE.NON_ORDERALE || a.availState === AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS);
    }

    public static hasAvailCz(availabilities: any[]) {
        return availabilities &&
            availabilities.length > 0 &&
            !(availabilities || []).some(avail =>
                avail.availState === CZ_AVAIL_STATE.UNAVAILABLE || avail.availState === CZ_AVAIL_STATE.NON_ORDERALE
            );
    }

    public static hasAvailByStock(article: ArticleModel, isAffiliateApplyDeliverableStock?: boolean) {
        let targetStock = article.deliverableStock;
        if (!isAffiliateApplyDeliverableStock) {
            const localBranchStockObj = article.stock;
            if (!localBranchStockObj) {
                return false;
            }
            const localBranchStock = localBranchStockObj.stock;
            targetStock = localBranchStock;
        }
        if (targetStock === null || targetStock === undefined) {
            return false;
        }
        return targetStock >= (article.amountNumber || article.salesQuantity);
    }

    public static articleNoAvail(item: ArticleModel) {
        if (this.isMultipleAvail(item)) {
            return false;
        } else {
            const unsortedAvailabilities = item.availabilities || [];
            const availState = AvailabilityUtil.intialAvailSortState(unsortedAvailabilities, false);

            if (availState === AVAILABILITY_INFO.IS_ARTICLE_24H_STATE) {
                return true;
            }

            return false;
        }
    }

    public static isMultipleAvail(article: ArticleModel) {
        if (article.salesQuantity > 1 && article.availabilities && article.availabilities.length > 1) {
            return true;
        }

        return false;
    }

    public static hasAvailAutonet(availabilities: any[]) {
        return availabilities &&
            availabilities.length > 0 &&
            !(availabilities || []).some(avail =>
                avail.availState === 99
            );
    }

    public static hasAvailSb(availabilities: any[]) {
        return availabilities &&
            availabilities.length > 0 &&
            !(availabilities || []).some(avail =>
                avail.location && avail.location.state === SB_LOCATION_STATE.RED
            );
    }
    
    public static isAvailSameDay(availability: ArticleAvailabilityModel) {
        if (availability && availability.arrivalTime) {
            return this.isSameDay(availability.arrivalTime);
        }
    }

    public static isAvailNextDay(availability: ArticleAvailabilityModel) {
        if (availability && availability.arrivalTime) {
            return this.isNextDay(availability.arrivalTime);
        }
    }

    public static isSameDay(date) {
        return moment(date).isSame(moment(), 'day');
    }

    public static isNextDay(date) {
        return moment(date).isAfter(moment(), 'day');
    }

    public static getAvailSettingByAvailState(availDisplaySettings: SagAvailDisplaySettingModel[], availState: string) {
        return availDisplaySettings.find(item => item.availState === availState);
    }

    public static getAvailColorByAvailState(availDisplaySettings: SagAvailDisplaySettingModel[], affiliateCode: string, availState: string) {
        if(AffiliateUtil.isAffiliateCZ9(affiliateCode)) {
            return '';
        }

        const availSetting = availDisplaySettings.find(item => item.availState === availState);
        if (availSetting) {
            return availSetting.color;
        }

        return '';
    }

    public static getAvailConfirmTextColor(availDisplaySettings: SagAvailDisplaySettingModel[]) {
        const availSetting = availDisplaySettings.find(item => item.availState === SAG_AVAIL_DISPLAY_STATES.NOT_AVAILABLE);
        if (availSetting) {
            return availSetting.confirmColor;
        }

        return '';
    }

    public static getAvailContentByLanguageCode(availText: SagEditorLanguageModel[], languageCode: string) {
        if (availText.filter(item => !item.content).length === 0) {
            const lang = availText.find(item => item.langIso.toLocaleLowerCase() === languageCode);

            return lang && lang.content || '';
        }

        return '';
    }

    public static initInStockNoteForTour(availDisplaySettings: SagAvailDisplaySettingModel[], availabilities: ArticleAvailabilityModel[], stock: any, affiliateCode: string, languageCode: string) {
        if(!AffiliateUtil.isAffiliateApplyInStock(affiliateCode)) {
            return '';
        }

        if (!availabilities || availabilities.length === 0 || availabilities.length > 1) {
            return '';
        }

        const avail = availabilities[0];

        if (avail.sendMethodCode !== AVAILABILITY_INFO.TOUR) {
            return '';
        }

        const deliveryWarehouse = (avail.deliveryWarehouse || '').toString();
        const branchId = (stock && stock.branchId || '').toString();

        if (!avail.stockWarehouse && deliveryWarehouse && deliveryWarehouse === branchId && stock.stock >= avail.quantity) {
            const availSetting = AvailabilityUtil.getAvailSettingByAvailState(availDisplaySettings, SAG_AVAIL_DISPLAY_STATES.IN_STOCK);
            if (!availSetting) {
                return '';
            }
            return AvailabilityUtil.getAvailContentByLanguageCode(availSetting.listAvailText, languageCode);
        }

        return '';
    }

    public static initSpecialNotesWithDeliveryTour(availDisplaySettings: SagAvailDisplaySettingModel[], availability: ArticleAvailabilityModel, affiliateCode: string, ignoreBoConfig?: boolean) {
        if(!AffiliateUtil.isAffiliateCZ10(affiliateCode)) {
            return '';
        }

        if(!availability) {
            return '';
        }

        let AVAIL_STATE_24_HOURS;
        let AVAIL_STATE_24_HOURS_CZ;
        const AVAIL_STATE_NO_STOCK = AVAILABILITY_INFO.AVAIL_STATE_NO_STOCK;

        if (AffiliateUtil.isEhAxCz(affiliateCode)) {
            AVAIL_STATE_24_HOURS = CZ_AVAIL_STATE.NON_ORDERALE;
            AVAIL_STATE_24_HOURS_CZ = AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS
        } else {
            AVAIL_STATE_24_HOURS = AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS;
        }

        if (availability.availState !== AVAIL_STATE_24_HOURS && availability.availState !== AVAIL_STATE_24_HOURS_CZ && availability.availState !== AVAIL_STATE_NO_STOCK) {
            let availState = AvailabilityUtil.getAvailStateWhenHaveTime(availability);

            if (availState && availability.sendMethodCode === AVAILABILITY_INFO.TOUR) {
                const availSetting = AvailabilityUtil.getAvailSettingByAvailState(availDisplaySettings, availState);
                if ((!ignoreBoConfig && availSetting && availSetting.displayOption !== SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT) || ignoreBoConfig) {
                    return 'AVAIL_DISPLAY.DEPARTURE_TIME_FROM_BRANCH';
                }
            }
        }

        return '';
    }

    public static getAvailStateWhenHaveTime(availability: ArticleAvailabilityModel) {
        if (availability) {
            if (AvailabilityUtil.isAvailSameDay(availability)) {
                return SAG_AVAIL_DISPLAY_STATES.SAME_DAY;
            }

            if (AvailabilityUtil.isAvailNextDay(availability)) {
                return SAG_AVAIL_DISPLAY_STATES.NEXT_DAY;
            }
        }

        return '';
    }

    public static getAvailTextDisplayWhenHaveTime(availDisplaySettings: SagAvailDisplaySettingModel[], availability: ArticleAvailabilityModel, languageCode: string, affiliateCode: string, state?: string) {
        if(!AffiliateUtil.isAffiliateCZ9(affiliateCode)) {
            if(!state) {
                state = this.getAvailStateWhenHaveTime(availability);
            }

            if (state) {
                const avilSetting = AvailabilityUtil.getAvailSettingByAvailState(availDisplaySettings, state);

                if (avilSetting && avilSetting.displayOption === SAG_AVAIL_DISPLAY_OPTIONS.DISPLAY_TEXT) {
                    return AvailabilityUtil.getAvailContentByLanguageCode(avilSetting.listAvailText, languageCode);
                }
            }
        }

        return `
            ${availability.formattedCETArrivalDate}
            ${availability.formattedCETArrivalTime}`;
    }
}
