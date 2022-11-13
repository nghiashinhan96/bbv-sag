import { Constant } from 'src/app/core/conts/app.constant';
import { OciConstant } from '../constants/oci.constants';
import { Params } from '@angular/router';

export class OciUtil {

    public static isOciUrl(url: string): boolean {
        return url.indexOf(OciConstant.PARAM_OCI_VERSION + Constant.EQUAL) > -1
            && url.indexOf(OciConstant.PARAM_LANGUAGE + Constant.EQUAL) > -1
            && url.indexOf(OciConstant.PARAM_USERNAME + Constant.EQUAL) > -1
            && url.indexOf(OciConstant.PARAM_PASSWORD + Constant.EQUAL) > -1;
    }

    public static isOciQuery(params: Params): boolean {
        if (!params) {
            return false;
        }

        return this.hasParam(params[OciConstant.PARAM_OCI_VERSION])
            && this.hasParam(params[OciConstant.PARAM_LANGUAGE])
            && this.hasParam(params[OciConstant.PARAM_USERNAME])
            && this.hasParam(params[OciConstant.PARAM_PASSWORD]);
    }

    private static hasParam(param: any) {
        return param !== null && param !== undefined;
    }
}
