import { environment } from 'src/environments/environment';
import { AffiliateEnum } from 'sag-common';

export class EnvUtil {
    // handle show/hide connect with affiliate
    // public static isAllowedConnectWithAffiliate() {
    //     return !EnvUtil.isMatikCh();
    // }

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
        return environment.env === 'prod';
    }

    public static isPreEnv() {
        return environment.env === 'umbpre';
    }
}
