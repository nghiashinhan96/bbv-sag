import { AffiliateUtil } from 'sag-common';
import { APP_LANG_CODE_CS,
    APP_LANG_CODE_SR,
    DEFAULT_LANG_CODE,
    SUPPORTED_LANG_CODES_CZ,
    SUPPORTED_LANG_CODES_SB } from '../conts/app-lang-code.constant';
import { SUPPORTED_LANG_CODES } from 'sag-auth';


export class CountryUtil {

    static getSupportedLangCodes(affiliate: string) {
        if (AffiliateUtil.isAffiliateCZ9(affiliate) || AffiliateUtil.isAffiliateCZ10(affiliate)) {
            return SUPPORTED_LANG_CODES_CZ;
        }

        if (AffiliateUtil.isSb(affiliate)) {
            return SUPPORTED_LANG_CODES_SB;
        }
        return SUPPORTED_LANG_CODES;
    }

    static getDefaultLangCode(affiliate: string) {
        if (AffiliateUtil.isAffiliateCZ9(affiliate) || AffiliateUtil.isAffiliateCZ10(affiliate)) {
            return APP_LANG_CODE_CS;
        }

        if (AffiliateUtil.isSb(affiliate)) {
            return APP_LANG_CODE_SR;
        }
        return DEFAULT_LANG_CODE;
    }
}
