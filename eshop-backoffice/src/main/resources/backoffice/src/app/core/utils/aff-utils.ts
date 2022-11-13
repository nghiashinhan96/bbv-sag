import { environment } from 'src/environments/environment';
import { AffiliateEnum, AFFILIATE_CODE } from '../enums/enums';

export default class AffUtils {
    public static isTechnoCh(affName: string) {
        return affName === AffiliateEnum.TECHNO_CH.toString();
    }

    public static isDerendCh(affName: string) {
        return affName === AffiliateEnum.DEREND_CH.toString();
    }

    public static isDerendAt(affName: string) {
        return affName === AffiliateEnum.DEREND_AT.toString();
    }

    public static isMatikAt(affName: string) {
        return affName === AffiliateEnum.MATIK_AT.toString();
    }

    public static isAffiliateCH(affName) {
        return AffUtils.isTechnoCh(affName) || AffUtils.isDerendCh(affName);
    }

    public static isAffiliateAT(affName) {
        return AffUtils.isDerendAt(affName) || AffUtils.isMatikAt(affName);
    }

    /**
     * Get country code AT or CH
     * @return String represents for country code
     */
    public static getCountryCode(): string {
        return environment.countryCode;
    }

    public static isAT(): boolean {
        return environment.affiliate === AFFILIATE_CODE.AT;
    }

    public static isCH(): boolean {
        return environment.affiliate === AFFILIATE_CODE.CH;
    }

    public static isCZAX10(): boolean {
        return environment.affiliate === AFFILIATE_CODE.AXCZ;
    }

    public static isCZ(): boolean {
        return environment.affiliate === AFFILIATE_CODE.CZ;
    }

    public static isAffiliateApplyForPDP(): boolean {
        return AffUtils.isAT() ||  AffUtils.isCH() ||  AffUtils.isCZAX10();
    }

    public static isSB(): boolean {
        return environment.countryCode === 'sb';
    }
}
