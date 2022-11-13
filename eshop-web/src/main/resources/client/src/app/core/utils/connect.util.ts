import { environment } from 'src/environments/environment';
import { AffiliateUtil } from 'sag-common';

export class ConnectUtil {
    static getExportGrossPriceTranslationKey() {
        const keyForAt = 'SHOPPING_BASKET.EXPORT_HEADER.UVPE_PRICE';
        const keyForChAndWss = 'SHOPPING_BASKET.EXPORT_HEADER.GROSS_PRICE';
        // #5246: [CH-AX]: WSS- Show only Brutto price
        // 2) Change the column heading to Brutto (as before)
        if (AffiliateUtil.isEhCh(environment.affiliate)) {
            return keyForChAndWss;
        }
        return AffiliateUtil.isBaseAT(environment.affiliate) ? keyForAt : keyForChAndWss;
    }
}
