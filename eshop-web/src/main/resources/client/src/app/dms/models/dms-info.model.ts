import { Params } from '@angular/router';
import { DmsConstant } from '../constants/dms.constant';
import { DmsUtil } from '../utils/dms.util';

export class DmsInfo {
    basePath: string;
    fileName: string;
    origin: string;
    searchFlag: string;
    vehicleText: string;
    typenschein: string;
    articleNumbers: string[];
    articleQuantities: number[];
    orderFlag: string;
    vin: string;
    offerId: string;
    version: string;
    searched: boolean;
    reference: string;
    token: string;
    hookUrl: string;
    username: string;
    userPass: string;


    constructor(data?) {
        if (!data) {
            return;
        }

        this.basePath = data.basePath;
        this.fileName = data.fileName;
        this.origin = data.origin;
        this.searchFlag = data.searchFlag;
        this.vehicleText = data.vehicleText;
        this.typenschein = data.typenschein;
        this.articleNumbers = data.articleNumbers;
        this.articleQuantities = data.articleQuantities;
        this.orderFlag = data.orderFlag;
        this.vin = data.vin;
        this.offerId = data.offerId;
        this.version = data.version;
        this.searched = data.searched;
        this.reference = data.reference;
        this.token = data.token;
        this.hookUrl = data.hookUrl;
        this.username = data.username;
        this.userPass = data.userPass;
    }

    static extractDmsInfo(params: any): DmsInfo {
        const pArtNrs = decodeURIComponent(params[DmsConstant.PARAM_ARTICLE_NUMBERS] || '');
        const pArtQty = decodeURIComponent(params[DmsConstant.PARAM_ARTICLE_QUANTITIES] || '');
        const articleNumbers = pArtNrs ? pArtNrs.split(DmsConstant.PARAM_ITEM_DELIMETER) : [];
        const articleQuantities = pArtQty ? pArtQty.split(DmsConstant.PARAM_ITEM_DELIMETER) : [];

        const dmsInfo = new DmsInfo();
        dmsInfo.origin = params[DmsConstant.PARAM_ORIGIN];
        dmsInfo.basePath = params[DmsConstant.PARAM_BASEPATH];
        dmsInfo.fileName = params[DmsConstant.PARAM_FILENAME];
        dmsInfo.vehicleText = params[DmsConstant.PARAM_VEHICLE];
        dmsInfo.typenschein = params[DmsConstant.PARAM_TYPENSCHEIN];
        dmsInfo.orderFlag = params[DmsConstant.PARAM_QUICK_ORDER];
        dmsInfo.articleNumbers = articleNumbers;
        dmsInfo.articleQuantities = articleQuantities.map(n => Number(n));
        dmsInfo.searchFlag = params[DmsConstant.PARAM_SEARCH];
        dmsInfo.vin = params[DmsConstant.PARAM_VIN];
        dmsInfo.offerId = params[DmsConstant.PARAM_OFFER_ID];
        dmsInfo.version = DmsUtil.getVersion(params);
        dmsInfo.reference = params[DmsConstant.PARAM_ORDER_NUMBER];
        dmsInfo.token = params[DmsConstant.PARAM_TOKEN];
        dmsInfo.hookUrl = params[DmsConstant.PARAM_HOOK_URL];
        dmsInfo.username = params[DmsConstant.PARAM_USERNAME];
        dmsInfo.userPass = params[DmsConstant.PARAM_PASSWORD];
        return dmsInfo;
    }
}
