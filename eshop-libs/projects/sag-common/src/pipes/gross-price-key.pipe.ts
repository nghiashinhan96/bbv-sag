import { Pipe, PipeTransform } from '@angular/core';
import { AffiliateUtil } from '../utils/affiliate.util';

@Pipe({
    name: 'grossPriceKey'
})
export class GrossPriceKeyPipe implements PipeTransform {

    transform(affiliateCode: string): any {
        if (AffiliateUtil.isEhCh(affiliateCode)) {
            return 'COMMON_LABEL.PRICE.GROSS_PRICE_OTHER';
        }

        if (AffiliateUtil.isAffiliateCH(affiliateCode)) {
            return 'COMMON_LABEL.PRICE.GROSS_PRICE_CH';
        } else {
            return 'COMMON_LABEL.PRICE.GROSS_PRICE_AT';
        }
    }
}
