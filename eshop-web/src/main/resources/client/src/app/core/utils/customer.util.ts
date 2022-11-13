import { AffiliateEnum } from 'sag-common';

export class CustomerUtil {

    static buildCustomerInfo(userDetail) {
        if (userDetail) {
            const info = [userDetail.custNr || '', userDetail.customer && userDetail.customer.companyName || ''].join(' - ');
            const address = userDetail.defaultAddress && userDetail.defaultAddress.city || '';
            if (userDetail.customer && userDetail.customer.demoCustomer) {
                return `${info}, ${address} - Demo`;
            }
            return `${info}, ${address}`;
        }
        return '';
    }

    static buildSaleInfo(sale) {
        if (sale && sale.id) {
            return `${sale.id} - ${sale.name}`;
        }
        return '';
    }

    public static isTechnoChCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.TECHNO_CH.toString();
    }

    public static isDerendChCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.DEREND_CH.toString();
    }

    public static isDerendAtCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.DEREND_AT.toString();
    }

    public static isWbbCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.WBB.toString();
    }

    public static isRbeCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.RBE.toString();
    }

    public static isMatikChCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.MATIK_CH.toString();
    }

    public static isMatikAtCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.MATIK_AT.toString();
    }

    public static isAxCzCustomer(affiliateShortName) {
        return affiliateShortName === AffiliateEnum.AXCZ.toString();
    }

    public static isAtCustomer(affiliateShortName) {
        return this.isMatikAtCustomer(affiliateShortName) || this.isDerendAtCustomer(affiliateShortName);
    }

    public static isDvseCustomer(affiliateShortName: string): boolean {
        return this.isMatikAtCustomer(affiliateShortName) || this.isMatikChCustomer(affiliateShortName)
            || this.isAxCzCustomer(affiliateShortName);
    }

}
