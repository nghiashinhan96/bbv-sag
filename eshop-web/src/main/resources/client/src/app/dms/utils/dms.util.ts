import { DmsConstant } from '../constants/dms.constant';
import { Params } from '@angular/router';
import { DmsInfo } from '../models/dms-info.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';

export class DmsUtil {

    public static isDmsUrl(url: string): boolean {
        return url.indexOf(DmsConstant.PARAM_ORIGIN + Constant.EQUAL) > -1
            && url.indexOf(DmsConstant.PARAM_SEARCH + Constant.EQUAL) > -1
            && url.indexOf(DmsConstant.PARAM_REQUEST_TYPE + Constant.EQUAL) > -1;
    }

    public static isDmsQuery(params: Params): boolean {
        if (!params) {
            return false;
        }

        const requestType = params[DmsConstant.PARAM_REQUEST_TYPE];
        const searchType = params[DmsConstant.PARAM_SEARCH];
        const orderFlag = params[DmsConstant.PARAM_QUICK_ORDER];

        return DmsUtil.hasParam(params[DmsConstant.PARAM_ORIGIN])
            && DmsUtil.hasParam(params[DmsConstant.PARAM_SEARCH])
            && (requestType === DmsConstant.REQUEST_TYPE_ORDER || requestType === DmsConstant.REQUEST_TYPE_OFFER)
            && DmsUtil.hasParam(searchType) && (Number(searchType) === 1 || (Number(searchType) === 0 && Number(orderFlag) === 1))
            && (DmsUtil.isMetBrowser(params) || DmsUtil.isCloudDms(params));
    }

    private static isMetBrowser(params: Params): boolean {
        return DmsUtil.hasParam(params[DmsConstant.PARAM_USERNAME])
            && DmsUtil.hasParam(params[DmsConstant.PARAM_PASSWORD])
            && DmsUtil.hasParam(params[DmsConstant.PARAM_BASEPATH])
            && DmsUtil.hasParam(params[DmsConstant.PARAM_FILENAME]);
    }

    private static isCloudDms(params: Params): boolean {
        return DmsUtil.hasParam(params[DmsConstant.PARAM_TOKEN]) && DmsUtil.hasParam(params[DmsConstant.PARAM_HOOK_URL]);
    }

    public static getVersion(params: Params): string {
        const version = params[DmsConstant.PARAM_VERSION];
        if ((version && version.trim() === DmsConstant.VERSION_3) || DmsUtil.isCloudDms(params)) {
            return DmsConstant.VERSION_3;
        }
        // #3022
        // The "base" decision should be that the paramater V = 3.0, but we could make that decicion if U7, P7, P8, R are there
        if (DmsUtil.hasParam(params[DmsConstant.PARAM_ORDER_NUMBER])
            || DmsUtil.hasParam(params[DmsConstant.PARAM_OFFER_ID])
            || DmsUtil.hasParam(params[DmsConstant.PARAM_KM_SERVICE_PLAN])
            || DmsUtil.hasParam(params[DmsConstant.PARAM_VIN])) {
            return DmsConstant.VERSION_3;
        }

        return '';
    }

    public static isValidCloudDms(token: string, hookUrl: string): boolean {
        return !!token && !!hookUrl;
    }

    public static isValidMetBrowser(userName: string, password: string, basePath: string, fileName: string): boolean {
        return !!userName && !!password && !!basePath && !!fileName;
    }

    private static hasParam(param: any) {
        return param !== null && param !== undefined;
    }

    public static isDmsOfferPresent(dmsInfo: DmsInfo) {
        const offerId = dmsInfo.offerId;
        return offerId === '' || offerId === Constant.SPACE
            || (offerId && offerId.toLowerCase() === Constant.NULL_AS_STRING);
    }

    public static getDMSSalesOrgin() {
        const env = environment.affiliate;
        const prefixDms = 'dms-'
        if (AffiliateUtil.isAffiliateCH(env)) {
            return `${prefixDms}ch`
        }
        if (AffiliateUtil.isBaseAT(env)) {
            return `${prefixDms}at`
        }
        if (AffiliateUtil.isAffiliateCZ(env)) {
            return `${prefixDms}cz`
        }
        if (AffiliateUtil.isSb(env)) {
            return `${prefixDms}rs`
        }
        return null;
    }
}
