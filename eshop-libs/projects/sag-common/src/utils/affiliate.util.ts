import { ProjectId } from './../enums/project-id.enum';
import { AffiliateEnum } from '../enums/affiliate.enum';

// @dynamic
export class AffiliateUtil {

    public static isTechnoCh(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.TECHNO_CH.toString();
    }

    public static isDerendCh(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.DEREND_CH.toString();
    }

    public static isDerendAt(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.DEREND_AT.toString();
    }

    public static isMatikAt(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.MATIK_AT.toString();
    }

    public static isMatikCh(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.MATIK_CH.toString();
    }

    public static isWbb(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.WBB.toString();
    }

    public static isRbe(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.RBE.toString();
    }

    public static isCz(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.CZ.toString();
    }

    public static isEhCz(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.EH_CZ.toString() || AffiliateUtil.isEhAxCz(affiliateCode);
    }

    public static isSb(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.SB.toString();
    }

    public static isAffiliateAT(affiliateCode: string) {
        return AffiliateUtil.isDerendAt(affiliateCode) ||
            AffiliateUtil.isMatikAt(affiliateCode) ||
            AffiliateUtil.isEhAt(affiliateCode);
    }

    public static isAffiliateCH(affiliateCode: string) {
        return AffiliateUtil.isDerendCh(affiliateCode) ||
            AffiliateUtil.isTechnoCh(affiliateCode) ||
            AffiliateUtil.isMatikCh(affiliateCode) ||
            AffiliateUtil.isWbb(affiliateCode) ||
            AffiliateUtil.isRbe(affiliateCode) ||
            AffiliateUtil.isEhCh(affiliateCode);
    }

    public static isAffiliateCZ(affiliateCode: string) {
        return AffiliateUtil.isCz(affiliateCode) ||
            AffiliateUtil.isEhCz(affiliateCode);
    }

    public static isAffiliateCZ9(affiliateCode: string) {
        return AffiliateUtil.isCz(affiliateCode) || AffiliateUtil.isEhCz9(affiliateCode);
    }

    public static isAffiliateCZ10(affiliateCode: string) {
        return AffiliateUtil.isAxCz(affiliateCode) || AffiliateUtil.isEhAxCz(affiliateCode);
    }

    public static isEhCz9(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.EH_CZ.toString();
    }

    public static isEhCh(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.EH_CH.toString();
    }

    public static isEhAt(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.EH_AT.toString();
    }

    public static isAxCz(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.AXCZ.toString();
    }

    public static isBaseAT(affiliateCode: string) {
        return this.isAffiliateAT(affiliateCode) || this.isAxCz(affiliateCode);
    }

    public static isAffiliateHasFilterNoAvail(affiliateCode: string, projectId: string) {
        return AffiliateUtil.isAffiliateCZ(affiliateCode) || AffiliateUtil.isAxCz(affiliateCode) || AffiliateUtil.isSb(affiliateCode) || projectId === ProjectId.AUTONET;
    }

    public static isAffiliateATApplyPDP(affiliateCode: string) {
        return AffiliateUtil.isDerendAt(affiliateCode) ||
            AffiliateUtil.isMatikAt(affiliateCode);
    }

    public static isAffiliateCHApplyPDP(affiliateCode: string) {
        return AffiliateUtil.isDerendCh(affiliateCode) ||
            AffiliateUtil.isTechnoCh(affiliateCode) ||
            AffiliateUtil.isMatikCh(affiliateCode) ||
            AffiliateUtil.isWbb(affiliateCode) ||
            AffiliateUtil.isRbe(affiliateCode);
    }

    public static isAffiliateApplyPDP(affiliateCode: string) {
        if(this.isAffiliateATApplyPDP(affiliateCode) || this.isAffiliateCHApplyPDP(affiliateCode) || this.isAxCz(affiliateCode)) {
            return true;
        }

        return false;
    }

    public static isEhAxCz(affiliateCode: string) {
        return affiliateCode === AffiliateEnum.EH_AX_CZ.toString();
    }

    public static isAffiliateApplyToGetCustomerCurrency(affiliateCode: string) {
        return this.isAffiliateApplyPDP(affiliateCode) || this.isEhAxCz(affiliateCode) || this.isEhCh(affiliateCode);
    }

    public static isAffiliateApplyArticleExtendedMode(affiliateCode: string) {
        return this.isAffiliateCZ(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAxCz(affiliateCode);
    }

    public static isAffiliateApplyMotorbikeShop(affiliateCode: string) {
        return this.isAffiliateCZ10(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAffiliateCH(affiliateCode);
    }

    public static isAffiliateApplyFgasAndDeposit(affiliateCode: string) {
        return this.isAffiliateCZ10(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAffiliateCH(affiliateCode);
    }

    public static isAffiliateShowPlanTour(affiliateCode: string) {
        return this.isAffiliateCZ10(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAffiliateCH(affiliateCode);
    }
    
    public static isAffiliateApplyInStock(affiliateCode: string) {
        return this.isAffiliateCZ10(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAffiliateCH(affiliateCode);
    }

    public static isFinalCustomerAffiliate (affiliateCode: string) {
        return this.isEhCh(affiliateCode) || this.isEhAt(affiliateCode) || this.isEhAxCz(affiliateCode);
    }

    public static isAffiliateApplyDeliverableStock(affiliateCode: string) {
        return this.isAffiliateCZ10(affiliateCode) || this.isAffiliateAT(affiliateCode) || this.isAffiliateCH(affiliateCode);
    }

}
