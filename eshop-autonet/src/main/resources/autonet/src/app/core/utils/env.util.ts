import { environment } from 'src/environments/environment';
import { AffiliateEnum } from '../enums/affiliate.enum';

export class EnvUtil {

    public static isTechnoCh() {
        return `${environment.affiliate}` === AffiliateEnum.TECHNO_CH.toString();
    }

    public static isDerendCh() {
        return `${environment.affiliate}` === AffiliateEnum.DEREND_CH.toString();
    }

    public static isDerendAt() {
        return `${environment.affiliate}` === AffiliateEnum.DEREND_AT.toString();
    }

    public static isMatikAt() {
        return `${environment.affiliate}` === AffiliateEnum.MATIK_AT.toString();
    }

    public static isMatikCh() {
        return `${environment.affiliate}` === AffiliateEnum.MATIK_CH.toString();
    }

    public static isWbb() {
        return `${environment.affiliate}` === AffiliateEnum.WBB.toString();
    }

    public static isRbe() {
        return `${environment.affiliate}` === AffiliateEnum.RBE.toString();
    }

    public static isAffiliateAT() {
        return EnvUtil.isDerendAt() || EnvUtil.isMatikAt();
    }

    public static isAffiliateCH() {
        return EnvUtil.isDerendCh() || EnvUtil.isTechnoCh() || EnvUtil.isMatikCh() || EnvUtil.isWbb() || EnvUtil.isRbe();
    }

    public static isEhCh() {
        return `${environment.affiliate}` === AffiliateEnum.EH_CH.toString();
    }

    // handle show/hide connect with affiliate
    public static isAllowedConnectWithAffiliate() {
        return !EnvUtil.isMatikCh();
    }

    public static isDevEnv() {
        return environment.env === 'dev';
    }

    public static isFeatureEnv() {
        return environment.env === 'feature';
    }

    public static isTestEnv() {
        return environment.env === 'testing';
    }

    public static isTrainingEnv() {
        return environment.env === 'training';
    }

    public static isProdEnv() {
        return environment.env === 'umbprod';
    }

    public static isPreEnv() {
        return environment.env === 'umbpre';
    }
}